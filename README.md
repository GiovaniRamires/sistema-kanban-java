# 🗂️ Sistema Kanban — Java + MySQL

Sistema de gerenciamento de tarefas no estilo Kanban com persistência em banco de dados, arquitetura em camadas (Model · DAO · Service · View) e suporte a dois tipos de tarefa: **Bug** e **Feature**.

> Projeto desenvolvido para a disciplina de Programação Orientada a Objetos — 2º semestre de ADS.

---

## 🏗️ Arquitetura

O projeto segue o padrão de **separação de responsabilidades** em 4 camadas:

```
br.edu.projetokanban/
├── model/
│   ├── Tarefa.java          # Classe abstrata base
│   ├── Bug.java             # Estende Tarefa (severidade, reproduzível)
│   ├── Feature.java         # Estende Tarefa (valor de negócio, complexidade)
│   ├── Projeto.java         # Entidade de projeto
│   └── EventoTarefa.java    # Registro de histórico de mudanças
├── dao/
│   ├── ConnectionFactory.java   # Gerencia conexão com o banco
│   ├── ProjetoDAO.java          # CRUD de projetos
│   ├── TarefaDAO.java           # CRUD de Bug e Feature (tabela unificada)
│   └── EventoTarefaDAO.java     # Insere e lista eventos
├── service/
│   ├── ProjetoService.java      # Validações e regras de negócio de projetos
│   └── TarefaService.java       # Validações, movimentação de status e relatórios
├── view/
│   └── MenuPrincipal.java       # Menus interativos via terminal
├── controller/
│   └── KanbanController.java    # Orquestra a inicialização
└── ProjetoKanban.java           # main()
```

## 💡 Conceitos aplicados

- **Herança e polimorfismo:** `Bug` e `Feature` estendem `Tarefa` e implementam `calcularPrioridade()` e `estimarPrazo()` de forma diferente
- **Classe abstrata:** `Tarefa` não pode ser instanciada diretamente
- **Enum:** `Status` (FAZER / FAZENDO / FEITO), `Severidade`, `ValorNegocio`, `Complexidade`
- **JDBC com PreparedStatement:** SQL parametrizado em todas as operações — sem SQL injection
- **Try-with-resources:** conexões fechadas automaticamente após cada operação
- **Padrão DAO:** toda SQL isolada na camada DAO, jamais em Service ou View
- **Registro de eventos:** toda alteração de status gera um evento com timestamp

## ⚙️ Configuração do banco de dados

**Pré-requisitos:** MySQL 8+ e Java 11+

```sql
CREATE DATABASE kanban_db;
USE kanban_db;

CREATE TABLE projetos (
    id    INT AUTO_INCREMENT PRIMARY KEY,
    nome  VARCHAR(120) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE tarefas (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    projeto_id     INT NOT NULL,
    tipo           ENUM('BUG','FEATURE') NOT NULL,
    titulo         VARCHAR(200) NOT NULL,
    descricao      TEXT,
    responsavel    VARCHAR(100),
    status         ENUM('FAZER','FAZENDO','FEITO') DEFAULT 'FAZER',
    severidade     ENUM('BAIXA','MEDIA','ALTA','CRITICA'),
    reproduzivel   BOOLEAN,
    valor_negocio  ENUM('BAIXO','MEDIO','ALTO'),
    complexidade   ENUM('SIMPLES','MEDIA','COMPLEXA'),
    FOREIGN KEY (projeto_id) REFERENCES projetos(id)
);

CREATE TABLE eventos_tarefa (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    tarefa_id  INT NOT NULL,
    descricao  VARCHAR(300),
    datahora   DATETIME,
    FOREIGN KEY (tarefa_id) REFERENCES tarefas(id)
);
```

Atualize as credenciais em `ConnectionFactory.java`:

```java
private static final String URL    = "jdbc:mysql://localhost:3306/kanban_db";
private static final String USUARIO = "seu_usuario";
private static final String SENHA   = "sua_senha";
```

## ▶️ Como executar

1. Configure o banco conforme acima
2. Adicione o driver MySQL JDBC ao classpath (ex: `mysql-connector-j-8.x.jar`)
3. Compile e execute:

```bash
javac -cp .:mysql-connector-j-8.x.jar -d out src/**/*.java
java  -cp out:mysql-connector-j-8.x.jar ProjetoKanban
```

Ou importe no **NetBeans / IntelliJ IDEA** e adicione o conector como biblioteca.

## 🖥️ Funcionalidades

### Projetos
- Cadastrar, listar, atualizar e excluir projetos

### Tarefas
- Cadastrar **Bug** com severidade e flag de reprodutibilidade
- Cadastrar **Feature** com valor de negócio e complexidade
- Mover tarefa entre status: `FAZER → FAZENDO → FEITO`
- Ver histórico completo de eventos de uma tarefa
- Excluir tarefa

### Relatórios
- **Backlog priorizado:** tarefas pendentes ordenadas por severidade (Bug) ou valor de negócio (Feature)
- **Por responsável:** todas as tarefas de uma pessoa com prazo total estimado
