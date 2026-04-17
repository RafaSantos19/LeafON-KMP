# Leaf.ON KMP

Projeto Kotlin Multiplatform com Compose Multiplatform. O app Leaf.ON centraliza telas de autenticacao, home, perfil e gerenciamento de Smart Pots, incluindo listagem, cadastro, edicao, detalhe, rotinas e alertas.

projeto **Leaf.ON**, um sistema inteligente de monitoramento ambiental para estufas e hortas urbanas.

## Integrates
- Rafael Ferreira dos Santos
- Miguel Gomes de Lima Coyado Vieira

---

## Visão Geral
O **Leaf.ON** integra dispositivos de Internet das Coisas (IoT) com aplicações multiplataforma para automatizar a coleta de dados ambientais (temperatura, umidade, luminosidade) e o controle de irrigação.

- **Objetivo Geral:** Desenvolver um ecossistema integrado para monitoramento, armazenamento e análise de dados de estufas inteligentes.
- **Público-alvo:** Pequenos produtores e entusiastas de agricultura urbana.
- **Diferencial:** Solução acessível que une hardware (ESP32) e software multiplataforma (Mobile, Web, Desktop) com análise preditiva básica.


## Estado atual

- Kotlin Multiplatform + Compose Multiplatform.
- Targets configurados: Android, Desktop JVM, JavaScript e Wasm.
- UI compartilhada em `commonMain`.
- Navegacao com Navigation3 usando destinos tipados.
- Feature principal: CRUD local em memoria de Smart Pots.
- Camada `data` com `RepositorioRemoto` e `RepositorioRemotoEmMemoria`, simulando operacoes REST localmente.
- Backend real ainda nao integrado.

## Estrutura atual do projeto

```text
LeafON-KMP/
|-- composeApp/
|   |-- build.gradle.kts
|   `-- src/
|       |-- commonMain/
|       |   |-- composeResources/
|       |   |   |-- drawable/
|       |   |   |-- files/
|       |   |   `-- font/
|       |   `-- kotlin/kmp/edu/leafon_kmp/
|       |       |-- App.kt
|       |       |-- Platform.kt
|       |       |-- core/
|       |       |-- data/
|       |       |   |-- RepositorioRemoto.kt
|       |       |   `-- RepositorioRemotoEmMemoria.kt
|       |       |-- domain/
|       |       `-- presentation/
|       |           |-- components/
|       |           |   |-- global/
|       |           |   `-- layout/
|       |           |-- home/
|       |           |   |-- components/
|       |           |   `-- model/
|       |           |-- login/
|       |           |-- navigation/
|       |           |   |-- AppDestination.kt
|       |           |   `-- AppNavigator.kt
|       |           |-- pots/
|       |           |   |-- alerts/
|       |           |   |-- components/
|       |           |   |-- create/
|       |           |   |-- detail/
|       |           |   |-- edit/
|       |           |   |-- model/
|       |           |   `-- routines/
|       |           |-- profile/
|       |           `-- register/
|       |-- commonTest/
|       |   `-- kotlin/
|       |-- androidMain/
|       |   |-- kotlin/kmp/edu/leafon_kmp/
|       |   |   |-- MainActivity.kt
|       |   |   `-- Platform.android.kt
|       |   `-- res/
|       |-- jvmMain/
|       |   `-- kotlin/kmp/edu/leafon_kmp/
|       |       |-- main.kt
|       |       `-- Platform.jvm.kt
|       |-- jsMain/
|       |   `-- kotlin/kmp/edu/leafon_kmp/
|       |       `-- Platform.js.kt
|       |-- wasmJsMain/
|       |   `-- kotlin/kmp/edu/leafon_kmp/
|       |       `-- Platform.wasmJs.kt
|       `-- webMain/
|           |-- kotlin/kmp/edu/leafon_kmp/
|           |   `-- main.kt
|           `-- resources/
|               |-- index.html
|               `-- styles.css
|-- gradle/
|   |-- libs.versions.toml
|   `-- wrapper/
|-- build.gradle.kts
|-- settings.gradle.kts
|-- gradle.properties
|-- gradlew
`-- gradlew.bat
```

## Organizacao das principais pastas

- `composeApp/`: modulo principal da aplicacao multiplataforma.
- `composeApp/src/commonMain/`: codigo compartilhado entre Android, Desktop e Web.
- `data/`: contrato e implementacao local do repositorio usado pelo app.
- `presentation/navigation/`: destinos tipados e centralizacao da navegacao com Navigation3.
- `presentation/components/`: componentes globais e layout compartilhado, como top bar e sidebar.
- `presentation/home/`: tela inicial/dashboard e seus componentes.
- `presentation/login/` e `presentation/register/`: telas de autenticacao.
- `presentation/pots/`: feature de Smart Pots com lista, formulario, detalhe, rotinas e alertas.
- `presentation/profile/`: tela de perfil.
- `androidMain/`: entrada e recursos especificos do Android.
- `jvmMain/`: entrada da aplicacao Desktop JVM.
- `jsMain/`, `wasmJsMain/` e `webMain/`: targets web.

## Requisitos

Antes de executar o projeto, tenha no ambiente:

- JDK 17 ou superior.
- Android Studio para rodar a versao Android.
- Android SDK configurado localmente.
- Navegador moderno para rodar os targets Web.

No Windows, execute os comandos com `.\gradlew.bat`. No macOS/Linux, use `./gradlew`.

## Como rodar

### Android

Gerar o build debug:

```powershell
.\gradlew.bat :composeApp:assembleDebug
```

macOS/Linux:

```bash
./gradlew :composeApp:assembleDebug
```

Para instalar em dispositivo ou emulador Android:

```powershell
.\gradlew.bat :composeApp:installDebug
```

macOS/Linux:

```bash
./gradlew :composeApp:installDebug
```

Tambem e possivel abrir o projeto no Android Studio e executar o modulo Android pela IDE.

### Desktop JVM

Executar a aplicacao desktop:

```powershell
.\gradlew.bat :composeApp:run
```

macOS/Linux:

```bash
./gradlew :composeApp:run
```

Alternativa direta do target JVM:

```powershell
.\gradlew.bat :composeApp:jvmRun
```

Gerar pacote nativo para o sistema atual:

```powershell
.\gradlew.bat :composeApp:packageDistributionForCurrentOS
```

O projeto tambem possui tasks para `packageDmg`, `packageMsi` e `packageDeb`.

### Web com Wasm

Executar servidor de desenvolvimento WebAssembly:

```powershell
.\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
```

macOS/Linux:

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

### Web com JavaScript

Executar servidor de desenvolvimento JavaScript:

```powershell
.\gradlew.bat :composeApp:jsBrowserDevelopmentRun
```

macOS/Linux:

```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

## Comandos de validacao

Compilar o target Desktop JVM:

```powershell
.\gradlew.bat --no-daemon :composeApp:compileKotlinJvm
```

Rodar verificacoes gerais do Gradle:

```powershell
.\gradlew.bat :composeApp:check
```

Listar todas as tasks disponiveis:

```powershell
.\gradlew.bat :composeApp:tasks --all
```

## Observacoes

- O CRUD de Smart Pots usa dados locais em memoria.
- `RepositorioRemoto` expoe metodos com semantica REST (`GET`, `POST`, `PUT`, `DELETE`), mas ainda nao faz chamadas HTTP reais.
- A navegacao atual usa Navigation3 com `NavDisplay`, `NavEntry`, `AppDestination` e `AppNavigator`.
- O backend real e a persistencia permanente ainda nao foram implementados.


## Links

- Repositórios do projeto: [Frontend](https://github.com/RafaSantos19/LeafON-KMP)
- Repositórios do projeto: [Backend](https://github.com/RafaSantos19/LeafON-API)
- Documentação do Projeto (Parcial): [Link do Docs](https://docs.google.com/document/d/1GGbEGgVE6KhAxyz87omWVD5X1HY0fGU79IRKmRMV-Ec/edit?usp=sharing)
