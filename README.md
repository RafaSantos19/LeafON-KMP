# Leaf.ON KMP

Aplicacao Kotlin Multiplatform com Compose Multiplatform para autenticacao, monitoramento e gerenciamento de Smart Pots no ecossistema Leaf.ON.

O projeto integra:
- Supabase Auth para login, registro e sessao
- API LeafON para Smart Pots, telemetria, alertas, rotinas e perfil
- UI compartilhada em `commonMain` para Android, Desktop JVM, JavaScript e Wasm

## Integrantes
- Rafael Ferreira dos Santos
- Miguel Gomes de Lima Coyado Vieira

## Visao geral

O Leaf.ON e um sistema de monitoramento ambiental para estufas e hortas urbanas. A aplicacao KMP centraliza:
- autenticacao do usuario
- dashboard com dados reais
- CRUD de Smart Pots
- consulta e envio de telemetria
- alertas e marcacao como lido
- rotinas de irrigacao e iluminacao
- perfil do usuario

## Estado atual

O frontend esta integrado ao backend real.

Funcionalidades implementadas:
- Login e registro com Supabase Auth
- Restauracao de sessao e injecao automatica de Bearer Token no `HttpClient`
- Perfil com leitura e atualizacao de `GET /users/me` e `PUT /users/me`
- CRUD real de Smart Pots
- Tela de detalhe do vaso com telemetria mais recente
- Envio e consulta de telemetria
- Lista de alertas globais e por vaso
- Marcacao de alertas como lidos
- CRUD real de rotinas
- Dashboard Home com dados reais e graficos simples em `Canvas`
- Bluetooth Classic no Android para receber telemetria de modulos HC-05/ZS-040

Ao carregar a Home, o app consulta vasos e alertas nao lidos e, para o vaso
selecionado, busca a leitura mais recente e no maximo 30 pontos do historico.
Carregamentos anteriores sao cancelados em refresh ou troca de vaso para evitar
requisicoes concorrentes e uso desnecessario de memoria no mobile.

## Stack

- Kotlin `2.3.0`
- Compose Multiplatform `1.10.0`
- Material 3
- Navigation3
- Ktor Client `3.1.3`
- kotlinx.serialization
- kotlinx.coroutines
- kotlinx.datetime
- Supabase Auth `3.4.0`

## Targets

Configurados no projeto:
- Android
- Desktop JVM
- JavaScript
- Wasm

## Arquitetura

Principais camadas:
- `core/`: configuracoes, auth, rede, tempo e utilitarios
- `data/`: cliente HTTP, DTOs, mapeadores e repositorios
- `presentation/`: telas, componentes, estados e view models

Repositorios em uso:
- `SmartPotRepository`
- `TelemetryRepository`
- `AlertRepository`
- `RoutineRepository`

O ponto central de composicao de dependencias e `composeApp/src/commonMain/kotlin/kmp/edu/leafon_kmp/AppDependencies.kt`.

Por padrao, `AppDependencies.usarRepositorioHttp = true`, entao o app usa a API real. Para testes locais de algumas features, ainda existe fallback em memoria para parte dos dados de Smart Pots e telemetria.

## Estrutura do projeto

```text
LeafON-KMP/
|-- composeApp/
|   |-- build.gradle.kts
|   `-- src/
|       |-- commonMain/
|       |   `-- kotlin/kmp/edu/leafon_kmp/
|       |       |-- App.kt
|       |       |-- AppDependencies.kt
|       |       |-- core/
|       |       |-- data/
|       |       |   |-- auth/
|       |       |   |-- mapper/
|       |       |   |-- remote/
|       |       |   `-- repository/
|       |       `-- presentation/
|       |           |-- components/
|       |           |-- home/
|       |           |-- login/
|       |           |-- navigation/
|       |           |-- pots/
|       |           |   |-- alerts/
|       |           |   |-- create/
|       |           |   |-- detail/
|       |           |   |-- edit/
|       |           |   `-- routines/
|       |           |-- profile/
|       |           `-- register/
|       |-- androidMain/
|       |-- jvmMain/
|       |-- jsMain/
|       `-- wasmJsMain/
|-- gradle/
|-- build.gradle.kts
|-- settings.gradle.kts
`-- README.md
```

## Endpoints integrados

Autenticacao e perfil:
- `GET /users/me`
- `PUT /users/me`
- `POST /users`

Smart Pots:
- `GET /smart-pots`
- `GET /smart-pots/{id}`
- `POST /smart-pots`
- `PUT /smart-pots/{id}`
- `DELETE /smart-pots/{id}`

Telemetria:
- `POST /telemetry`
- `GET /telemetry?smartPotId=<id>`
- `GET /telemetry/latest?smartPotId=<id>`

O endpoint de ultima telemetria retorna um resumo com `soilHumidity`,
`airHumidity`, `temperature` e `luminosityStatus`. O timestamp `readAt` e
opcional.

Alertas:
- `GET /alerts`
- `GET /alerts/unread`
- `PATCH /alerts/{id}/read`

Rotinas:
- `GET /routines`
- `GET /routines/{id}`
- `POST /routines`
- `PUT /routines/{id}`
- `PATCH /routines/{id}/activate`
- `PATCH /routines/{id}/deactivate`
- `PATCH /routines/{id}/simulate-execution`
- `DELETE /routines/{id}`

## Configuracao

### 1. Requisitos

- JDK 17 ou superior
- Android Studio para o target Android
- Android SDK configurado
- Node e navegador moderno para os targets Web

No Windows, use `.\gradlew.bat`. No macOS/Linux, use `./gradlew`.

### 2. Supabase

As configuracoes atuais ficam em `composeApp/src/commonMain/kotlin/kmp/edu/leafon_kmp/core/auth/SupabaseConfig.kt`:
- `projectUrl`
- `publishableKey`

### 3. Base URL da API

A URL base default varia por target:
- Android (dispositivo fisico na rede local): `http://192.168.1.2:8080`
- Desktop JVM: `http://127.0.0.1:8080`
- JS/Wasm: `http://localhost:8080`

Para o Android acessar a API, o dispositivo e a maquina que executa o backend
devem estar na mesma rede. O backend tambem deve aceitar conexoes externas na
porta `8080`, em vez de escutar apenas em `localhost`.

No emulador Android, caso seja necessario acessar um backend executado na
maquina host, troque a URL em runtime para `http://10.0.2.2:8080` usando
`AppDependencies.atualizarBaseUrl(...)`.

Arquivos:
- `composeApp/src/commonMain/kotlin/kmp/edu/leafon_kmp/core/network/ApiConfig.kt`
- `composeApp/src/androidMain/kotlin/kmp/edu/leafon_kmp/core/network/ApiConfig.android.kt`
- `composeApp/src/jvmMain/kotlin/kmp/edu/leafon_kmp/core/network/ApiConfig.jvm.kt`
- `composeApp/src/jsMain/kotlin/kmp/edu/leafon_kmp/core/network/ApiConfig.js.kt`
- `composeApp/src/wasmJsMain/kotlin/kmp/edu/leafon_kmp/core/network/ApiConfig.wasmJs.kt`

Tambem e possivel trocar a base URL em runtime via `AppDependencies.atualizarBaseUrl(...)`.

## Como rodar

### Android

Antes de abrir o detalhe de um Smart Pot, pareie o HC-05/ZS-040 nas
configuracoes Bluetooth do Android. Em Android 12 ou superior, aceite a
permissao de dispositivos proximos solicitada pelo app.

O modulo deve enviar uma leitura JSON por linha:

```json
{
  "soilHumidity": 92,
  "soilHumidityRaw": 352,
  "airHumidity": 68.4,
  "temperature": 21.4,
  "luminosityStatus": "CLARO",
  "luminosityDigital": 0
}
```

No detalhe do vaso, selecione o dispositivo pareado, conecte e use
`Sincronizar Telemetria` para enviar apenas a ultima leitura valida para
`POST /telemetry?smartPotId=<id>`.

Build debug:

```powershell
.\gradlew.bat :composeApp:assembleDebug
```

Instalacao no emulador/dispositivo:

```powershell
.\gradlew.bat :composeApp:installDebug
```

### Desktop JVM

Executar:

```powershell
.\gradlew.bat :composeApp:run
```

Alternativa:

```powershell
.\gradlew.bat :composeApp:jvmRun
```

### Web com Wasm

```powershell
.\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
```

### Web com JavaScript

```powershell
.\gradlew.bat :composeApp:jsBrowserDevelopmentRun
```

## Validacao

Compilacao JVM:

```powershell
.\gradlew.bat --no-daemon :composeApp:compileKotlinJvm
```

Compilacao completa usada nas ultimas entregas:

```powershell
.\gradlew.bat --no-daemon compileKotlinMetadata :composeApp:compileKotlinJvm :composeApp:compileDebugKotlinAndroid
```

## Fluxos principais da UI

- `Home`: cards da leitura atual e graficos de umidade do solo, umidade do ar e temperatura; luminosidade e exibida como status
- `Pots`: listagem, criacao, edicao, exclusao e detalhe do Smart Pot
- `Pot Detail`: telemetria mais recente, acoes rapidas, acesso a rotinas e alertas do vaso
- `Alerts`: lista global ou filtrada por `smartPotId`, com filtro de nao lidos
- `Routines`: criacao, edicao, ativacao, desativacao, simulacao de execucao e exclusao
- `Profile`: leitura e atualizacao dos dados do usuario autenticado

## Observacoes

- O projeto usa Navigation3 com destinos tipados em `AppDestination`.
- O `HttpClient` adiciona automaticamente o token Bearer quando ha sessao valida.
- Os graficos da Home sao implementados com `Compose Canvas`, sem biblioteca externa.
- Parte do codigo legado em memoria ainda existe para fallback e transicao, mas o fluxo principal do app esta baseado na API.

## Links

- Frontend: [LeafON-KMP](https://github.com/RafaSantos19/LeafON-KMP)
- Backend: [LeafON-API](https://github.com/RafaSantos19/LeafON-API)
- Documento do projeto: [Google Docs](https://docs.google.com/document/d/1GGbEGgVE6KhAxyz87omWVD5X1HY0fGU79IRKmRMV-Ec/edit?usp=sharing)
