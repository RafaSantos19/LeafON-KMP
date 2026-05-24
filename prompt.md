Você é um engenheiro sênior Kotlin Multiplatform trabalhando no projeto LeafON-KMP.

Contexto:
A API LeafON está finalizada.
O frontend KMP já possui:
- Supabase Auth funcionando
- AuthTokenProvider com token real
- HttpClientFactory com Bearer Token automático
- LeafOnApiClient funcionando
- Tela de perfil integrada
- CRUD real de SmartPots implementado e testado
- Telemetry via API implementada e testada
- Alerts via API implementado/testado
- Routines via API implementado/testado

Agora vamos implementar a Home/Dashboard com dados reais e gráficos simples.

Objetivo:
Transformar a tela Home em um dashboard funcional usando dados reais da API:
- SmartPots reais
- Última telemetria
- Histórico de telemetria
- Alertas não lidos
- Gráficos simples de umidade, temperatura e luminosidade

Escopo:
- Integrar Home com SmartPotRepository
- Integrar Home com TelemetryRepository
- Integrar Home com AlertRepository
- Exibir cards de resumo
- Exibir seletor de SmartPot
- Exibir gráfico simples usando Compose Canvas
- Não implementar Bluetooth
- Não implementar MQTT
- Não alterar backend
- Não adicionar biblioteca de gráfico externa, a menos que já exista no projeto
- Não criar dashboard analítico complexo

Backend disponível:
- GET /smart-pots
- GET /telemetry?smartPotId=<id>
- GET /telemetry/latest?smartPotId=<id>
- GET /alerts/unread

Regras:
- userId é extraído do JWT.
- O frontend não envia userId.
- GET /telemetry retorna leituras ordenadas por readAt DESC.
- GET /telemetry/latest retorna a leitura mais recente.
- GET /alerts/unread retorna alertas pendentes/não lidos.

Tarefas:

1. Localizar a Home atual

Procurar arquivos em:

composeApp/src/commonMain/kotlin/kmp/edu/leafon_kmp/presentation/home/

Identificar:
- HomeScreen
- HomeViewModel, se existir
- HomeUiState, se existir
- componentes de cards existentes

Não reescrever a tela inteira se já houver estrutura aproveitável.

2. Criar ou ajustar HomeUiState

Criar/adaptar estado:

data class HomeUiState(
val smartPots: List<SmartPot> = emptyList(),
val selectedSmartPotId: String? = null,
val latestTelemetry: TelemetryReading? = null,
val telemetryHistory: List<TelemetryReading> = emptyList(),
val unreadAlertsCount: Int = 0,
val isLoading: Boolean = false,
val isTelemetryLoading: Boolean = false,
val errorMessage: String? = null,
)

Se o projeto usa nomes diferentes, adaptar ao padrão existente.

3. Criar ou ajustar HomeViewModel

Criar/adaptar:

class HomeViewModel(
private val smartPotRepository: SmartPotRepository,
private val telemetryRepository: TelemetryRepository,
private val alertRepository: AlertRepository,
)

Responsabilidades:
- loadDashboard()
- selectSmartPot(id: String)
- refresh()
- loadTelemetryForSelectedPot()

Fluxo do loadDashboard():
1. carregar SmartPots reais
2. carregar alertas não lidos
3. se houver SmartPot:
  - selecionar o primeiro vaso por padrão
  - buscar latest telemetry
  - buscar histórico de telemetry
4. se não houver SmartPot:
  - mostrar estado vazio sem erro

4. Seleção de SmartPot

Na Home, adicionar um seletor simples.

Pode ser:
- Dropdown, se já existir componente no projeto
- Lista horizontal de chips/cards
- Botões simples

Ao selecionar outro vaso:
- atualizar selectedSmartPotId
- buscar getLatestTelemetry(selectedSmartPotId)
- buscar getTelemetry(selectedSmartPotId)

5. Cards de resumo

Exibir cards:

A. Total de vasos:
- smartPots.size

B. Alertas não lidos:
- unreadAlertsCount

C. Umidade atual:
- latestTelemetry?.soilHumidity
- se null: "--"

D. Temperatura atual:
- latestTelemetry?.temperature
- se null: "--"

E. Luminosidade atual:
- latestTelemetry?.luminosity
- se null: "--"

Não inventar dados fake se a API não retornar leitura.
Se não houver leitura, mostrar placeholder.

6. Criar componente de gráfico simples

Criar componente compartilhado usando Compose Canvas:

LineChartCard(
title: String,
values: List<Float>,
labels: List<String> = emptyList(),
valueSuffix: String = ""
)

Ou algo equivalente.

Requisitos:
- funcionar em commonMain
- usar Canvas do Compose
- não depender de Android específico
- lidar com lista vazia
- lidar com lista com 1 ponto
- normalizar valores entre min e max
- desenhar linha simples
- mostrar título
- mostrar valor mais recente
- não quebrar se todos os valores forem iguais

7. Preparar dados dos gráficos

A API retorna telemetry em ordem DESC.
Para gráfico, inverter para ordem cronológica:

val chronological = telemetryHistory.reversed()

Gerar séries:

Umidade:
chronological.map { it.soilHumidity.toFloat() }

Temperatura:
chronological.map { it.temperature.toFloat() }

Luminosidade:
chronological.map { it.luminosity.toFloat() }

Limitar para os últimos 20 ou 30 pontos para não poluir a tela:

val chartData = telemetryHistory.take(30).reversed()

8. Gráficos da Home

Renderizar três gráficos:

- Umidade do solo
  - valores: soilHumidity
  - sufixo: "%"

- Temperatura
  - valores: temperature
  - sufixo: "°C"

- Luminosidade
  - valores: luminosity
  - sufixo: "" ou " lux" se o projeto usa essa unidade

Se não houver telemetria:
mostrar:
"Nenhuma leitura registrada para este vaso."

9. Tratamento de erros

Mapear erros:
- 401: "Sessão inválida. Faça login novamente."
- 403: "Você não tem permissão para acessar estes dados."
- timeout/rede: "Não foi possível conectar à API."
- erro inesperado: "Não foi possível carregar o dashboard."

Se falhar apenas telemetria, não derrubar a Home inteira.
Exemplo:
- SmartPots carregaram
- alertas carregaram
- telemetria falhou
  Então mostrar cards básicos e erro apenas na área de telemetria.

10. Estado vazio

Se não houver SmartPots:
mostrar mensagem:

"Você ainda não cadastrou nenhum vaso."

E manter a Home funcional.

Se houver SmartPot, mas não houver Telemetry:
mostrar:

"Nenhuma leitura registrada ainda."

11. Não implementar nesta fase

Não criar:
- BluetoothGateway
- permissões Bluetooth
- conexão HC-05
- parser serial
- MQTT
- scheduler
- gráficos complexos
- exportação de dados
- filtros por período
- agregação por múltiplos vasos

12. Validação manual

Testar:

A. Usuário sem vasos:
- Home mostra estado vazio
- não crasha

B. Usuário com vaso sem telemetria:
- Home mostra total de vasos
- seletor mostra vaso
- cards de leitura mostram "--"
- gráficos mostram placeholder

C. Usuário com vaso e telemetria:
- Home mostra última umidade, temperatura e luminosidade
- gráficos aparecem com histórico

D. Múltiplos vasos:
- trocar vaso selecionado
- gráficos e cards mudam

E. Alertas:
- gerar alerta via Telemetry abaixo de humidityMin
- Home mostra contagem de alertas não lidos

13. Validação Gradle

Rodar:

.\gradlew.bat --no-daemon compileKotlinMetadata :composeApp:compileKotlinJvm :composeApp:compileDebugKotlinAndroid

Corrigir todos os erros.

Resultado esperado:
- Home usando dados reais
- Cards de resumo funcionando
- Seletor de SmartPot funcionando
- Gráficos simples renderizando telemetryHistory
- Estado vazio funcionando
- Erros tratados
- Profile continua funcionando
- SmartPot continua funcionando
- Telemetry continua funcionando
- Alerts continua funcionando
- Routines continua funcionando
- Nenhum código de Bluetooth implementado

Ao finalizar, listar:
- arquivos criados
- arquivos alterados
- componentes de gráfico criados
- endpoints usados
- testes manuais realizados
- resultado dos comandos Gradle