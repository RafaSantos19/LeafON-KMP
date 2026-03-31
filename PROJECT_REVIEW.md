# PROJECT REVIEW

## 1. Avaliação Geral do Projeto

### Estado atual vs especificação
- O repositório atual é, na prática, um **protótipo frontend em Kotlin Multiplatform**, e não o ecossistema completo descrito em `Contexto_Projeto_ Leaf.ON.md`.
- A especificação define três frentes principais:
  - IoT com ESP32 + sensores + MQTT
  - Backend com Kotlin + Spring Boot + JWT + consumidor MQTT + persistência
  - Frontends com KMP para mobile/desktop e **React para web**
- O que existe hoje no repositório:
  - UI KMP para login, cadastro, dashboard e perfil
  - Estados locais e vieclearwmodels com dados simulados
  - Navegação Compose para login/register/home
  - Nenhum módulo backend
  - Nenhuma integração MQTT
  - Nenhuma aplicação web em React
  - Nenhuma camada de API com Ktor
  - Nenhuma separação consistente entre domínio, dados e apresentação

### Coerência arquitetural
- A arquitetura interna do frontend está coerente para um protótipo visual:
  - `presentation/*` está razoavelmente separada por feature
  - As telas já usam objetos de estado e viewmodels
  - Componentes visuais compartilhados começaram a ser centralizados
- Ela **não está coerente com a arquitetura completa do produto** descrita na especificação, porque faltam os limites reais do sistema:
  - camada backend
  - contrato de autenticação JWT
  - fluxo de ingestão de telemetria
  - fluxo de comando de irrigação
  - estratégia de persistência
  - processamento orientado a eventos

### Alinhamento com as responsabilidades da especificação

#### Fluxo IoT + MQTT
- Não implementado.
- Não existem:
  - tópicos MQTT definidos
  - modelos de payload
  - configuração de broker
  - política de reconexão
  - validação de mensagens
  - camada de consumo/publicação

#### Responsabilidades de backend
- Não implementadas.
- Faltam:
  - controllers
  - services
  - repositories
  - autenticação JWT
  - OpenAPI/Swagger
  - persistência
  - geração de alertas
  - engine de regras
  - integração MQTT

#### Responsabilidades de frontend
- Implementadas apenas parcialmente.
- Existe:
  - login/cadastro
  - dashboard
  - perfil
- Não existe:
  - autenticação real
  - gerenciamento de SmartPot
  - histórico de telemetria
  - central de alertas
  - configuração de regras
  - CRUD dos domínios principais
  - integração com API

### O que falta para o MVP
- O projeto ainda está **longe do MVP** descrito na especificação.
- O principal bloqueio não é interface; é ausência de infraestrutura de produto.

### O que adicionar exatamente para atingir o MVP
- Criar um módulo backend com, no mínimo:
  - `auth`
  - `smartpot`
  - `telemetry`
  - `irrigation`
  - `alerts`
  - `rules`
- Implementar:
  - endpoints JWT de login e cadastro
  - consumidor MQTT de telemetria
  - publisher MQTT para comando de irrigação
  - persistência para usuário, smart pot, telemetria, evento de irrigação e alerta
- Criar um contrato compartilhado de API:
  - DTOs de request/response
  - modelo de erro
  - modelo de token/sessão
  - paginação para histórico
- Criar a camada de dados no frontend:
  - repositories
  - cliente Ktor
  - armazenamento de sessão/token
  - mapeadores de DTO para domínio/UI

---

## 2. Análise de UI / Estrutura de Telas

### Telas existentes
- `LoginScreen`
- `RegisterScreen`
- `DashboardScreen`
- `ProfileScreen`

### Avaliação da navegação
- A navegação atual é simples e compreensível para protótipo:
  - `login -> register -> home`
  - perfil renderizado dentro do shell do `home`
- Problemas:
  - Só existem 3 rotas globais
  - A sidebar mostra `History`, `Plant & Pot`, `Alerts` e `Profile`, mas apenas `Profile` realmente existe
  - Isso cria expectativa de funcionalidades inexistentes
  - O perfil ficou meio rota, meio destino interno; o modelo de navegação está inconsistente

### Separação de responsabilidades das telas
- Pontos positivos:
  - login/register/home/profile estão separados por feature
  - existe estado por feature
- Pontos fracos:
  - os viewmodels ainda são quase só mutadores de estado visual
  - `DashboardScreen` concentra muita composição de produto
  - ainda não existe separação clara entre shell de aplicação e páginas internas
  - não existe modelo de rota/estado para os destinos ainda não implementados

### Alinhamento com as features do MVP
- Login/Register:
  - visualmente alinhados ao RF01
  - funcionalmente não implementados
- Dashboard:
  - representa visualmente RF06, RF07, RF09, RF14 e RF18
  - ainda sem integração real
- Perfil:
  - não é feature central do MVP na especificação, mas é aceitável como suporte à conta
- Telas de MVP que faltam:
  - gestão do SmartPot
  - configuração de regras
  - histórico de telemetria
  - central de alertas
  - histórico de irrigação
  - status/conectividade do dispositivo

### Responsividade
- Pontos positivos:
  - login, cadastro, dashboard e perfil já possuem variações compactas e expandidas
  - o uso de Compose compartilhado ajuda na consistência
- Riscos:
  - a responsividade está baseada mais em breakpoint de layout do que em adaptação completa de interação
  - não há evidência de validação em mobile estreito, tablet e desktop amplo
  - sidebar e topbar ainda são muito desktop-first

### Consistência entre plataformas
- A consistência entre Android/Desktop/Web via KMP é alta porque a mesma UI está sendo reaproveitada.
- Isso entra em conflito com a especificação, que define **React para web**.
- Hoje a situação é:
  - mobile e desktop podem compartilhar KMP sem problema
  - web atual está em KMP JS/WASM, não em React
  - a estratégia de plataforma está desalinhada com o documento de contexto

### Hierarquia de UX e clareza
- Pontos positivos:
  - dashboard é fácil de entender
  - perfil está mais bem estruturado
  - login/cadastro têm direção visual clara
- Problemas:
  - o dashboard tenta representar monitoramento, irrigação, alertas e navegação antes de existirem os fluxos completos
  - faltam telas de detalhe para tornar a home acionável
  - não há estados reais de erro/sucesso de integração
  - não existe onboarding do primeiro dispositivo

### O que adicionar exatamente na UI
- Criar `HistoryScreen`
  - gráfico histórico
  - filtros de período
  - lista paginada de leituras
- Criar `SmartPotScreen`
  - nome da planta
  - status do dispositivo
  - limites/configurações
  - cooldown e duração da irrigação
- Criar `AlertsScreen`
  - lido/não lido
  - severidade
  - timestamp
- Criar `RulesScreen` ou incorporar isso em SmartPot/configuração
  - limiar de umidade
  - duração
  - cooldown
  - rotinas por dia/horário

---

## 3. Estrutura de Dados e Prontidão para Backend

### Prontidão do frontend para integração
- A estrutura atual **não está pronta** para integrar backend de forma limpa.
- Ela é suficiente para seguir com prototipação visual, mas conectar API real agora geraria acoplamento e retrabalho.

### Avaliação de alinhamento com as entidades esperadas

#### Usuário
- Existe apenas como campos de formulário em login/cadastro/perfil.
- Faltam:
  - `UserDto`
  - modelo de sessão/token
  - `userId`
  - permissões/role
  - estratégia de refresh token, se adotada

#### SmartPot
- Não existe como entidade de primeira classe.
- O dashboard usa `PlantStatusUi`, que é modelo de tela, não modelo de domínio.
- Faltam:
  - `SmartPot`
  - `SmartPotSettings`
  - metadados de conectividade
  - estado do atuador/bomba

#### Telemetria
- Hoje só existe como dado visual simulado.
- Faltam:
  - modelo de telemetria com `type`, `value`, `timestamp`, `smartPotId`
  - modelo agregado para gráficos
  - paginação e filtros

#### Irrigação
- Existe apenas como item de lista visual.
- Faltam:
  - modelo de evento de irrigação com `tipo`, `duracao`, `timestamp`, `origem`
  - DTO de request para irrigação manual

#### Regras
- Não modeladas.
- É um gap direto em relação ao RF04 e RF11.

### Escalabilidade do gerenciamento de estado
- O gerenciamento atual funciona para 4 telas isoladas.
- Não escala bem para o produto completo porque:
  - o estado mistura valor de formulário, mock e status de negócio
  - não existe camada de use case ou repository
  - não existe abstração padrão de resultado assíncrono
  - não existe estado global de sessão
  - loading/error/success não seguem um padrão compartilhado

### Prontidão para integração de API
- Baixa.
- Evidências:
  - `composeApp/build.gradle.kts` não inclui Ktor Client
  - não existe `data/` ou `domain/`
  - não existem DTOs
  - não existem interfaces de repository
  - não existe configuração de cliente HTTP
  - não existe persistência de token

### Acoplamentos fortes e falta de abstração
- `dashboardPreviewState()` sustenta o fluxo principal do produto
- `HomeViewModel` simula irrigação diretamente no estado de UI
- login/cadastro não separam validação de formulário de transporte
- perfil simula salvar/logout dentro do próprio viewmodel

### O que adicionar exatamente
- Criar `data/remote/dto` com:
  - `LoginRequestDto`
  - `LoginResponseDto`
  - `RegisterRequestDto`
  - `UserDto`
  - `SmartPotDto`
  - `TelemetryReadingDto`
  - `IrrigationEventDto`
  - `AlertDto`
  - `RuleDto`
- Criar `domain/model` com:
  - `User`
  - `SmartPot`
  - `TelemetryReading`
  - `IrrigationEvent`
  - `Alert`
  - `Rule`
- Criar `data/repository` com interfaces:
  - `AuthRepository`
  - `SmartPotRepository`
  - `TelemetryRepository`
  - `IrrigationRepository`
  - `AlertsRepository`
- Criar camada de mapeamento:
  - `dto -> domain -> ui`

---

## 4. Análise de Adaptação Multiplataforma

### Mobile (KMP)
- Pontos fortes:
  - Compose compartilhado acelera a entrega do UI
  - já existe adaptação de layout para larguras menores
- Riscos:
  - a navegação vai ficar difícil de manter se continuar centralizada em `App.kt`
  - o padrão `remember { ViewModel() }` não é robusto para ciclo de vida e restauração
  - não existe persistência de sessão local
  - não existe estratégia de refresh/sincronização de telemetria

### O que mudar no mobile
- Criar abstração de navigation graph
- Criar estado global de sessão
- Introduzir repositories e carregamento assíncrono real
- Adicionar refresh periódico ou manual para dashboard
- Persistir token/autenticação localmente

### Web (React)
- Existe um problema arquitetural central:
  - a especificação define React
  - o repositório atual usa KMP JS/WASM
- Impacto:
  - se React for obrigatório, o frontend web ainda não existe
  - se KMP Web for aceitável, a especificação precisa ser atualizada

### O que fazer na web
- Escolher uma direção imediatamente:
  - **Opção A:** manter a especificação como fonte de verdade e criar um app web separado em React
  - **Opção B:** revisar a especificação para assumir Compose Multiplatform também na web
- Recomendação para MVP:
  - escolher uma stack agora
  - não seguir com React e KMP Web em paralelo sem capacidade real de equipe

### Desktop
- KMP é uma boa escolha para desktop.
- O desktop deve ser mais simples que mobile/web:
  - foco em monitoramento
  - configurações
  - alertas
  - irrigação manual
- Não vale criar complexidade específica de desktop antes da existência do backend.

### O que adaptar no desktop
- Manter desktop como console de monitoramento e controle
- Priorizar layouts mais largos para gráficos e listas
- Evitar features exclusivas de desktop no MVP

---

## 5. Gaps de Arquitetura e Integração

### Prontidão para MQTT
- Não está pronta.
- Faltam:
  - estratégia de tópicos
  - formato de payload de telemetria
  - formato de payload de comando
  - QoS/retry
  - monitoramento online/offline
  - consumidor/publisher backend

### Fluxo de comunicação com backend
- Não está pronto.
- Faltam:
  - endpoints REST
  - autenticação com JWT
  - versionamento de API
  - convenção de erros
  - paginação para histórico

### Comportamento orientado a eventos
- Não implementado.
- A especificação exige:
  - ingestão de telemetria
  - disparo de irrigação automática
  - geração de alerta para umidade crítica
  - monitoramento de conectividade

### Camadas ausentes
- Backend:
  - controller
  - service
  - repository
  - integração MQTT
- Frontend:
  - domain
  - data
  - repository
  - cliente de rede
  - sessão/persistência

### Pontos fracos de acoplamento
- Modelos de UI estão ocupando o papel de entidades de negócio
- Destinos de navegação ainda estão misturados com decisão de renderização
- Dados mockados vivem dentro dos fluxos principais, não isolados em preview/provider

### Áreas de risco
- Continuar refinando UI sem travar contratos e domínio vai gerar retrabalho
- A indefinição da stack web pode duplicar esforço
- Não existe cobertura de teste para regras reais porque as regras ainda não foram separadas
- MQTT e backend, que são o núcleo do produto, ainda não foram validados

### O que adicionar exatamente na integração
- Definir tópicos backend/MQTT:
  - `leafon/{smartPotId}/telemetry`
  - `leafon/{smartPotId}/command/irrigation`
  - `leafon/{smartPotId}/status`
- Criar endpoints:
  - `POST /api/v1/auth/login`
  - `POST /api/v1/auth/register`
  - `GET /api/v1/smartpots`
  - `GET /api/v1/smartpots/{id}`
  - `PATCH /api/v1/smartpots/{id}/settings`
  - `GET /api/v1/smartpots/{id}/telemetry`
  - `POST /api/v1/smartpots/{id}/irrigations/manual`
  - `GET /api/v1/smartpots/{id}/irrigations`
  - `GET /api/v1/alerts`
  - `PATCH /api/v1/alerts/{id}/read`

---

## 6. Melhorias Priorizadas

### Críticas (bloqueiam o MVP)
- Criar o backend previsto na especificação.
- Definir contrato de API e DTOs antes de expandir mais telas.
- Implementar autenticação JWT ponta a ponta.
- Criar modelos reais para SmartPot, telemetria, irrigação, alertas e regras.
- Decidir se a web será React ou KMP e alinhar o código com essa decisão.
- Implementar as telas que faltam para o MVP: histórico, alertas, gestão de SmartPot e regras.

### Importantes (devem melhorar)
- Introduzir camadas `data/`, `domain/`, `presentation/` no frontend.
- Remover mock principal dos viewmodels e substituir por repositories.
- Padronizar loading/error/success entre features.
- Tornar explícito o modelo de navegação para todos os destinos da sidebar.
- Adicionar testes para viewmodels, mapeadores e transições de estado.

### Nice-to-have (Fase 2)
- Análise preditiva para sugestão de irrigação
- Push notifications
- Dashboard multi-dispositivo
- Painéis desktop mais ricos
- Cache offline de telemetria recente

---

## 7. Refatorações Sugeridas

### Estrutura de pastas
- Evoluir de:
  - `presentation/login`
  - `presentation/register`
  - `presentation/home`
  - `presentation/profile`
- Para:
  - `data/remote/dto`
  - `data/remote/api`
  - `data/repository`
  - `domain/model`
  - `domain/usecase`
  - `presentation/components`
  - `presentation/navigation`
  - `presentation/auth/login`
  - `presentation/auth/register`
  - `presentation/dashboard`
  - `presentation/history`
  - `presentation/alerts`
  - `presentation/smartpot`
  - `presentation/profile`

### Convenções de nome
- Não usar `UiState` como substituto de entidade de domínio.
- Padronizar:
  - `Dto` para transporte
  - nomes de domínio para regra de negócio
  - `UiState` apenas para estado de renderização
- Renomear `home` para `dashboard` se esse for o significado real da feature

### Quebra de componentes
- Manter em global/shared apenas:
  - sistema de cores
  - shell de layout
  - botões/cards/fields reutilizáveis
- Manter em feature:
  - cards e gráficos do dashboard
  - lógica de formulário de auth
  - seções específicas do perfil

### Estrutura sugerida de API
- Frontend:
  - `AuthApi`
  - `SmartPotApi`
  - `TelemetryApi`
  - `IrrigationApi`
  - `AlertsApi`
- Backend:
  - `auth`
  - `smartpot`
  - `telemetry`
  - `irrigation`
  - `alerts`
  - `rules`
  - `mqtt`

---

## Avaliação Final

- Como **protótipo de interface**, o projeto está evoluindo bem.
- Como **implementação do MVP Leaf.ON**, ele ainda está em estágio inicial e muito concentrado em frontend.
- O maior risco hoje é continuar refinando UI antes de travar:
  - modelos de domínio
  - contratos de API
  - fluxo MQTT
  - decisão definitiva da plataforma web

## Próxima sprint recomendada

1. Congelar novas telas, exceto as rotas mínimas faltantes do MVP.
2. Definir contratos de backend e MQTT.
3. Implementar backend de autenticação, SmartPot, telemetria e irrigação manual.
4. Adicionar repositories e Ktor Client no frontend.
5. Substituir fluxos mockados de login/dashboard/perfil por integração real.
