# Programa Fidelidade App

Aplicativo Android para gerenciamento de programa de fidelidade, permitindo consulta de saldo de pontos, acompanhamento de extrato, resgate de recompensas e visualização de ofertas.

---

# 📖 Visão Geral

O Programa Fidelidade App foi desenvolvido para oferecer aos clientes uma experiência simples e intuitiva na gestão dos seus pontos de fidelidade.

### Identidade Visual e Componentes
<img width="563" height="844" alt="image" src="https://github.com/user-attachments/assets/99a522f1-40d9-4f0b-a687-661ff7dad53d" />

## Principais Funcionalidades

- Login e cadastro de usuários
- Consulta de saldo de pontos
- Visualização de ofertas e produtos
- Consulta de extrato de movimentações (ganhos e gastos)
- Resgate de recompensas (Carrinho de compras)
- Atualização de dados cadastrais e senha
- Funcionamento offline com armazenamento local (Room)
- Exibição de QR Code para identificação do cliente

---

# 🎯 Objetivo do Sistema

Permitir que participantes do programa de fidelidade possam:

- Acumular pontos em compras realizadas em estabelecimentos parceiros;
- Visualizar saldo e histórico de movimentações detalhado;
- Resgatar produtos e benefícios utilizando o saldo acumulado;
- Receber ofertas personalizadas na tela inicial;
- Acessar informações da conta mesmo sem conexão com a internet através de cache local.

---
# 🏛️ Arquitetura

O projeto segue o padrão arquitetural **MVVM (Model-View-ViewModel)** com separação de responsabilidades, consumo de APIs REST e persistência local.

## Arquitetura Geral

<img width="1724" height="2244" alt="image" src="https://github.com/user-attachments/assets/616e6b19-9623-40b0-8d4f-a9d0f5622521" />


## Estrutura de Camadas

### View
Responsável pela interface com o usuário desenvolvida em Jetpack Compose.

### ViewModel
Responsável pelas regras de apresentação, gerenciamento de estados reativos (StateFlow) e comunicação com o Repository.

### Repository
Centraliza o acesso aos dados, decidindo entre a fonte local e remota.

### Remote
Comunicação com APIs REST através do Retrofit e OkHttp.

### Local
Persistência local utilizando Room Database para suporte offline.

---

## 🛠 Stack Tecnológica

| Componente | Tecnologia |
| :--- | :--- |
| **Linguagem** | Kotlin 2.2.10 |
| **Interface** | Jetpack Compose (Material 3) |
| **Arquitetura** | MVVM |
| **Persistência** | Room Database 2.7.1 |
| **Rede** | Retrofit 2.11.0 + OkHttp |
| **Imagens** | Coil 2.6.0 |
| **Injeção de Dependência** | AppContainer (Manual DI) |
---

# 📂 Estrutura do Projeto

Conforme a estrutura atual do projeto:

```text
com.treinamento.app_fidelidade
│
├── data
│   ├── local          # Banco de Dados Room
│   │
│   ├── remote         # Retrofit Services e DTOs
│   │   ├── api
│   │   │   └── FidelidadeApi.kt
│   │   │
│   │   ├── dto
│   │   │
│   │   ├── service
│   │   │
│   │   └── RetrofitInstance.kt
│   │
│   └── repository     # Implementações dos repositórios
│
├── model              # Modelos de domínio
│
├── feature            # Funcionalidades organizadas por módulos (Catalogo, Perfil)
│
├── view               # Telas e componentes Compose
│
├── viewmodel          # ViewModels da aplicação
│
└── MainActivity.kt    # Ponto de entrada e NavHost
```

---

# 🔄 Fluxo de Navegação

<img width="4568" height="2888" alt="image" src="https://github.com/user-attachments/assets/07efd163-b18e-477c-807c-8fd9477a1392" />

# Fluxo de Login

<img width="1764" height="3448" alt="image" src="https://github.com/user-attachments/assets/b583d32d-392f-49d6-9386-1a6d3d65f9d1" />

---

# 📱 Telas do Aplicativo

### Fluxo de Onboarding e Perfil
<img width="1447" height="678" alt="image" src="https://github.com/user-attachments/assets/46e07a3b-b05b-4b49-9edb-b5cd7e47b8e2" />

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

- Login persistente com armazenamento de token;
- Recuperação automática de sessão;
- Funcionamento offline para visualização de dados cacheados.

---

## Cadastro

Permite criar uma nova conta.

### Campos

- Nome
- E-mail
- Senha
- Confirmação de Senha

---

## Home

Tela principal da aplicação com visão geral da conta.

<img width="1511" height="752" alt="image" src="https://github.com/user-attachments/assets/7afb6a14-c194-4c1b-bbd4-ee5422851495" />

### Funcionalidades

- Visualização do saldo atual de pontos;
- Banner de ofertas e novidades;
- Atalhos rápidos para funcionalidades;
- Resumo das últimas movimentações.

### Modo Offline

Exibe um banner informativo caso não exista conexão:
```text
Os dados podem estar desatualizados devido estar sem internet.
```
Os dados exibidos são provenientes do último sincronismo bem-sucedido.

---

## Extrato de Pontos

Exibe todas as movimentações (créditos e débitos) do usuário.

### Filtros Disponíveis

- **Por Tipo**: Todos, Ganhos (Créditos) ou Gastos (Débitos);
- **Por Período**: Todo o período, Últimos 7 dias ou Últimos 30 dias.

---

## Catálogo de Recompensas

Lista completa de produtos disponíveis para resgate.

<img width="1063" height="680" alt="image" src="https://github.com/user-attachments/assets/29ed29b3-eb45-435d-b0f0-112af292a147" />

### Recursos e Filtros

- **Busca**: Pesquisa textual por nome do produto;
- **Categorias**: Filtragem por departamentos (ex: Medicamentos, Bem-estar, Higiene);
- **Ordenação**: Menor pontuação, Maior pontuação ou Ordem Alfabética;
- **Indicadores**: Exibição de pontos necessários e quanto falta para o resgate.

---

## Resgate (Carrinho)

Gerenciamento dos itens selecionados para troca de pontos.

### Funcionalidades

- Adição de múltiplos produtos com controle de quantidade;
- Cálculo em tempo real do total de pontos e saldo restante;
- Validação de saldo insuficiente antes da finalização;
- Confirmação do resgate com atualização automática do saldo.

---

## Perfil

Área de gerenciamento da conta.

### Funcionalidades

- Visualização de dados pessoais;
- Edição de Nome e E-mail;
- Alteração de Senha;
- Exibição de QR Code exclusivo para identificação rápida em parceiros.

---

# 🧩 Modelo de Dados

## Usuario

```json
{
  "id": 1,
  "name": "Cliente",
  "email": "cliente@email.com",
  "pontosSaldo": 1000,
  "qrCode": "ABC123",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

## Movimentacao (Extrato)

```json
{
  "id": 1,
  "tipo": "debito",
  "valorPontos": 500,
  "descricao": "Resgate de Produto",
  "data": "2024-08-24",
  "idProduto": 10
}
```

## Produto

```json
{
  "id": 1,
  "name": "Smartwatch",
  "descricao": "Relógio Inteligente",
  "valorPontos": 2000,
  "imagemUrl": "http://...",
  "idCategoria": 1
}
```

---

# 🚀 Instalação e Execução

Siga os passos abaixo para configurar o ambiente e rodar a aplicação.

### 1. Configuração do Backend (Mockoon)
O aplicativo consome uma API REST simulada.
1.  Instale o **Mockoon**.
2.  Importe o arquivo `fidelidade-api.json` (localizado na pasta `backend`) para o Mockoon.
3.  Inicie o ambiente e certifique-se de que o servidor está rodando na porta **3000**.

### 2. Ajuste de IP (Retrofit)
Para que o app se comunique com o servidor local:
1.  Localize o arquivo `RetrofitInstance.kt` no projeto.
2.  Configure a constante `BASE_URL`:
    *   **Emulador Android:** Use `http://10.0.2.2:3000/api/`
    *   **Dispositivo Físico:** Use o IP do seu computador na rede Wi-Fi (ex: `http://192.168.1.5:3000/api/`).

### 3. Execução no Android Studio
1.  Clone o projeto e abra no Android Studio.
2.  Sincronize o Gradle (botão de elefante).
3.  Clique em **Run 'app'** para instalar no dispositivo ou emulador.

---

# ⚙️ Tecnologias Utilizadas

- **Kotlin**: Linguagem oficial para desenvolvimento Android.
- **Jetpack Compose**: UI declarativa moderna.
- **Material Design 3**: Padrão visual e componentes.
- **MVVM**: Arquitetura robusta e testável.
- **Retrofit & OkHttp**: Consumo de APIs REST com interceptadores de log.
- **Room Database**: Banco de dados local para cache e suporte offline.
- **Coroutines & Flow**: Processamento assíncrono e fluxos de dados reativos.
- **Navigation Component**: Navegação estruturada entre telas.
- **Coil**: Carregamento eficiente de imagens via rede.

---

# 📋 Requisitos do Projeto

- **Android Studio**: Ladybug (ou superior)
- **JDK**: 17
- **Gradle**: 8.x
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 35

---

# 💾 Persistência Local

O projeto utiliza o **Room** para persistir:
- Dados básicos do usuário logado;
- Estado do carrinho de compras (permite continuar compras após fechar o app);
- Cache do catálogo e extrato para acesso imediato em modo offline.
