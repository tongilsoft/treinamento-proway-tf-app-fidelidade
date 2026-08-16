
# Programa Fidelidade App

Aplicativo Android para gerenciamento de programa de fidelidade, permitindo consulta de saldo de pontos, acompanhamento de extrato, envio e recebimento de pontos, resgate de recompensas e visualização de promoções de parceiros.

---

# 📖 Visão Geral

O Programa Fidelidade App foi desenvolvido para oferecer aos clientes uma experiência simples e intuitiva na gestão dos seus pontos de fidelidade.

## Principais Funcionalidades

- Login e cadastro de usuários
- Consulta de saldo de pontos
- Visualização de promoções e ofertas
- Consulta de extrato de movimentações
- Resgate de recompensas
- Transferência de pontos entre clientes
- Consulta de parceiros participantes
- Atualização de dados cadastrais
- Funcionamento offline com armazenamento local
- Exibição de QR Code para identificação do cliente

---

# 🎯 Objetivo do Sistema

Permitir que participantes do programa de fidelidade possam:

- Acumular pontos em compras realizadas nos estabelecimentos parceiros;
- Visualizar saldo e histórico de movimentações;
- Resgatar produtos e benefícios;
- Transferir pontos para outros usuários;
- Receber ofertas personalizadas;
- Consultar parceiros próximos através de geolocalização;
- Acessar informações mesmo sem conexão com a internet.

---

# 🏛️ Arquitetura

O projeto segue o padrão arquitetural **MVVM (Model-View-ViewModel)** com separação de responsabilidades, consumo de APIs REST e persistência local.

## Arquitetura Geral

```text
┌─────────────────────┐
│       View          │
│      Screens        │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│     ViewModel       │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│     Repository      │
└───────┬─────┬───────┘
        │     │
        ▼     ▼
 Remote API   Local Database
```

## Estrutura de Camadas

### View
Responsável pela interface com o usuário.

### ViewModel
Responsável pelas regras de apresentação, gerenciamento de estados e comunicação com o Repository.

### Repository
Centraliza o acesso aos dados locais e remotos.

### Remote
Comunicação com APIs REST através do Retrofit.

### Local
Persistência local utilizando Room Database.

---

# 📂 Estrutura do Projeto

Conforme a estrutura atual do projeto:

```text
com.treinamento.app_fidelidade
│
├── data
│   ├── local
│   │
│   ├── remote
│   │   ├── api
│   │   │   └── FidelidadeApi.kt
│   │   │
│   │   ├── dto
│   │   │
│   │   ├── service
│   │   │
│   │   └── RetrofitInstance.kt
│   │
│   └── repository
│
├── model
│
├── ui.theme
│
├── view
│
├── viewmodel
│
└── MainActivity.kt
```

---

# 🔄 Fluxo de Navegação

```text
Splash
   ↓
Login
   ↓
Cadastro
   ↓
Home
   │
   ├── Extrato de Pontos
   ├── Promoções
   ├── Parceiros
   ├── Catálogo de Recompensas
   ├── Resgate
   └── Perfil
```

---

# 📱 Telas do Aplicativo

## Splash Screen

Responsável por:

- Inicialização do aplicativo;
- Carregamento de configurações iniciais;
- Verificação de sessão ativa;
- Redirecionamento automático para Login ou Home.

---

## Login

Permite autenticação do usuário.

### Campos

- E-mail
- Senha

### Funcionalidades

- Login persistente;
- Recuperação da última sessão;
- Funcionamento offline para visualização de dados locais.

---

## Cadastro

Permite criar uma nova conta.

### Campos

- Nome
- E-mail
- Senha
- QR Code

---

## Home

Tela principal da aplicação.

### Funcionalidades

- Visualização de saldo de pontos;
- Promoções em destaque;
- Últimas movimentações;
- Acesso rápido às funcionalidades principais.

### Modo Offline

Caso não exista conexão com a internet será exibida a mensagem:

```text
Os dados podem estar desatualizados devido estar sem internet.
```

Além disso, serão exibidos os últimos dados sincronizados.

---

## Extrato de Pontos

Exibe movimentações realizadas pelo usuário:

- Pontos acumulados;
- Pontos utilizados;
- Data da movimentação;
- Estabelecimento responsável.

### Filtros

- Período;
- Parceiro;
- Tipo de movimentação.

---

## Parceiros

Lista de estabelecimentos participantes.

### Recursos

- Pesquisa por nome;
- Filtros;
- Geolocalização;
- Parceiros próximos.

---

## Catálogo de Recompensas

Lista de produtos e benefícios disponíveis para resgate.

### Recursos

- Filtro por relevância;
- Exibição dos pontos necessários;
- Exibição dos pontos faltantes;
- Carrinho de resgates.

### Exemplo

```text
Saldo Atual: 500 pontos

Produto:
Smartwatch - 700 pontos

Faltam:
200 pontos
```

---

## Resgate

Responsável pela confirmação dos produtos selecionados.

### Funcionalidades

- Carrinho de confirmação;
- Consumo de pontos;
- Registro no histórico;
- Integração futura com backend.

---

## Perfil

Permite visualizar e atualizar:

- Nome;
- E-mail;
- Senha;
- QR Code.

---

# 🧩 Modelo de Dados

## Usuario

```json
{
  "id": 1,
  "name": "Cliente",
  "email": "cliente@email.com",
  "password": "***",
  "pontosSaldo": 1000,
  "qrCode": "ABC123",
  "createdAt": "",
  "updatedAt": ""
}
```

## PontosBonificados

```json
{
  "idUsuario": 1,
  "idEstabelecimento": 1,
  "pontos": 200,
  "createdAt": "",
  "updatedAt": ""
}
```

## HistoricoPontos

```json
{
  "id": 1,
  "idEstabelecimento": 10,
  "pontoGasto": 100,
  "idProduto": 30,
  "createdAt": "",
  "updatedAt": ""
}
```

## Produto

```json
{
  "id": 1,
  "name": "Smartwatch",
  "descricao": "Relógio Inteligente",
  "valorPontos": 2000,
  "idCategoria": 1
}
```

## Categoria

```json
{
  "id": 1,
  "name": "Eletrônicos",
  "descricao": "Produtos eletrônicos"
}
```

## Estabelecimento

```json
{
  "id": 1,
  "name": "Loja Exemplo",
  "endereco": "Rua Exemplo",
  "latitude": -12.9714,
  "longitude": -38.5014
}
```

---

# ⚙️ Tecnologias Utilizadas

## Linguagem

- Kotlin

## Interface

- Jetpack Compose
- Material Design 3
- Material Icons Extended
- Compose Animation

## Arquitetura

- MVVM (Model View ViewModel)

## Comunicação com APIs

- Retrofit 2.11.0
- Gson Converter
- OkHttp
- Logging Interceptor

## Processamento Assíncrono

- Kotlin Coroutines

## Persistência

- Room Database

## Navegação

- Navigation Component

## Imagens

- Coil

## Outros

- Android Splash Screen API
- Mockoon (Servidor Mock)

---

# 📋 Requisitos do Projeto

Com base no arquivo `build.gradle.kts`.

## Ambiente

- Android Studio Hedgehog ou superior
- JDK 11
- Gradle 8+
- Kotlin
- Android SDK

## SDKs

```text
Compile SDK: 37
Target SDK : 36
Min SDK    : 24
```

---

# 🔌 API Mock

O backend utilizado durante o desenvolvimento é disponibilizado através do Mockoon.

## Servidor

```text
Mockoon
```

## Endpoints

### Usuário

```http
POST /login
POST /cadastro
GET  /meusPontos
GET  /meusPontosGastosFiltro
GET  /extratoUltimos10
GET  /buscarOfertas
GET  /parceirosApp
GET  /meusDados
```

### Produtos

```http
GET /listagemFiltro
```

### Pontuação

```http
POST /consumirPontosUsuarios
POST /bonificarPontosUsuario
```

---

# 💾 Persistência Local

O projeto utiliza Room Database para armazenamento local de:

- Dados do usuário;
- Último login;
- Extrato sincronizado;
- Produtos visualizados;
- Resgates pendentes;
- Cache de promoções.

