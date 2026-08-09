# 📋 Sistema Kanban — Spring Boot API

> Evolução do projeto acadêmico original (JDBC puro + DAO) para uma API REST moderna com Spring Boot, JPA, autenticação JWT e testes unitários.

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com)
[![JWT](https://img.shields.io/badge/JWT-0.12.5-yellow)](https://github.com/jwtk/jjwt)
[![JUnit](https://img.shields.io/badge/JUnit-5-red)](https://junit.org/junit5)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

---

## 📌 Sobre o projeto

Este projeto é a evolução do [Sistema Kanban original](../../tree/main) — desenvolvido inicialmente com JDBC puro e padrão DAO — migrado para uma API REST completa com Spring Boot.

O sistema permite gerenciar projetos e tarefas no modelo Kanban, com dois tipos de tarefa (**Bug** e **Feature**), autenticação via JWT e relatórios de backlog priorizado.

---

## ✨ Funcionalidades

- ✅ Autenticação com JWT — registro e login de usuários
- ✅ Senha criptografada com BCrypt
- ✅ CRUD completo de Projetos
- ✅ Cadastro de Bugs com severidade e reprodutibilidade
- ✅ Cadastro de Features com valor de negócio e complexidade
- ✅ Movimentação de tarefas entre colunas (FAZER → FAZENDO → FEITO)
- ✅ Relatório de backlog priorizado por prioridade calculada
- ✅ Relatório de tarefas por responsável
- ✅ Arquivamento de projetos (soft delete)
- ✅ 15 testes unitários com JUnit 5 e Mockito

---

## 🚀 Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 4.1.0 | Framework principal |
| Spring Data JPA | — | Persistência de dados |
| Spring Security | — | Segurança e autenticação |
| MySQL | 8.0 | Banco de dados |
| JJWT | 0.12.5 | Geração e validação de tokens JWT |
| Lombok | — | Redução de boilerplate |
| JUnit 5 | — | Testes unitários |
| Mockito | — | Mock de dependências nos testes |
| Maven | — | Gerenciamento de dependências |

---

## 🏗️ Arquitetura

```
src/main/java/br/com/giovaniramires/kanban_spring/
├── config/
│   └── SecurityConfig.java       # Configuração do Spring Security
├── controller/
│   ├── AuthController.java       # Endpoints de autenticação
│   ├── ProjetoController.java    # Endpoints de projetos
│   └── TarefaController.java     # Endpoints de tarefas
├── dto/
│   ├── CadastrarBugDTO.java
│   ├── CadastrarFeatureDTO.java
│   ├── CriarProjetoDTO.java
│   ├── LoginDTO.java
│   ├── MoverStatusDTO.java
│   ├── RegistroDTO.java
│   └── TokenDTO.java
├── model/
│   ├── Bug.java                  # Extends Tarefa
│   ├── Feature.java              # Extends Tarefa
│   ├── Projeto.java
│   ├── Tarefa.java               # Classe abstrata (SINGLE_TABLE)
│   └── Usuario.java              # Implements UserDetails
├── repository/
│   ├── ProjetoRepository.java
│   ├── TarefaRepository.java
│   └── UsuarioRepository.java
├── security/
│   ├── JwtFilter.java            # Intercepta e valida tokens
│   └── JwtUtil.java              # Gera e valida tokens JWT
└── service/
    ├── ProjetoService.java
    └── TarefaService.java
```

---

## 📡 Endpoints

### 🔐 Autenticação (pública)

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/auth/register` | Cadastrar novo usuário |
| POST | `/auth/login` | Login e geração de token JWT |

### 📁 Projetos (requer token)

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/projeto` | Criar projeto |
| GET | `/projeto` | Listar projetos ativos |
| GET | `/projeto/{id}` | Buscar projeto por id |
| PUT | `/projeto/{id}` | Atualizar projeto |
| DELETE | `/projeto/{id}` | Deletar projeto |
| DELETE | `/projeto/{id}/arquivar` | Arquivar projeto |

### 📋 Tarefas (requer token)

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/tarefa/bug` | Cadastrar Bug |
| POST | `/tarefa/feature` | Cadastrar Feature |
| GET | `/tarefa/projeto/{projetoId}` | Listar tarefas por projeto |
| GET | `/tarefa/{id}` | Buscar tarefa por id |
| PUT | `/tarefa/{id}/status` | Mover status da tarefa |
| DELETE | `/tarefa/{id}` | Excluir tarefa |
| GET | `/tarefa/responsavel/{responsavel}` | Tarefas por responsável |
| GET | `/tarefa/backlog` | Backlog priorizado |

---

## ▶️ Como rodar localmente

### Pré-requisitos

- Java 21
- Maven
- MySQL 8.0

### Passo a passo

**1. Clone o repositório e acesse a branch:**
```bash
git clone https://github.com/GiovaniRamires/sistema-kanban-java.git
cd sistema-kanban-java
git checkout spring-boot
```

**2. Crie o banco de dados:**
```sql
CREATE DATABASE kanban_db;
```

**3. Configure o `application.properties`:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/kanban_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
api.security.token.secret=sua_chave_secreta
```

**4. Rode o projeto:**
```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`

---

## 🔑 Como autenticar

**1. Crie um usuário:**
```bash
POST /auth/register
{
  "nome": "user",
  "email": "user@email.com",
  "senha": "123456"
}
```

**2. Faça login e copie o token:**
```bash
POST /auth/login
{
  "email": "user@email.com",
  "senha": "123456"
}
```

**3. Use o token nas requisições:**
```
Authorization: Bearer eyJhbGci...
```

---

## 🧪 Testes

Para rodar os 15 testes unitários:

```bash
./mvnw test
```

Cobertura de testes:
- `ProjetoServiceTest` — 4 testes
- `TarefaServiceTest` — 11 testes

---

## 📈 Evolução do projeto

| Versão | Branch | Descrição |
|---|---|---|
| 1.0 | `main` | JDBC puro + padrão DAO |
| 2.0 | `spring-boot` | API REST com Spring Boot + JWT |

---

## 👨‍💻 Autor

**Giovani Ramires Cardoso dos Santos**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-giovani--ramires-blue)](https://linkedin.com/in/giovani-ramires)
[![GitHub](https://img.shields.io/badge/GitHub-GiovaniRamires-black)](https://github.com/GiovaniRamires)