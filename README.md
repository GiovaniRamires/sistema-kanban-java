# Sistema Kanban — Java + MySQL

Sistema de gerenciamento de tarefas no estilo Kanban com persistência em banco de dados, arquitetura em camadas (Model · DAO · Service · View) e suporte a dois tipos de tarefa: **Bug** e **Feature**.

> Projeto desenvolvido para a disciplina de Programação Orientada a Objetos — 2º semestre de ADS.

---

## Arquitetura

O projeto segue o padrão de separação de responsabilidades em 4 camadas:

```
br.edu.projetokanban/
├── model/
│   ├── Tarefa.java              # Classe abstrata base
│   ├── Bug.java                 # Estende Tarefa (severidade, reproduzível)
│   ├── Feature.java             # Estende Tarefa (valor de negócio, complexidade)
│   ├── Projeto.java             # Entidade de projeto
│   └── EventoTarefa.java        # Registro de histórico de mudanças
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

---

## Conceitos aplicados

- **Herança e polimorfismo** — `Bug` e `Feature` estendem `Tarefa` e implementam `calcularPrioridade()` e `estimarPrazo()` de formas diferentes
- **Classe abstrata** — `Tarefa` não pode ser instanciada diretamente
- **Enum** — `Status` (FAZER / FAZENDO / FEITO), `Severidade`, `ValorNegocio`, `Complexidade`
- **JDBC com PreparedStatement** — SQL parametrizado em todas as operações, sem risco de SQL injection
- **Try-with-resources** — conexões fechadas automaticamente após cada operação
- **Padrão DAO** — toda SQL isolada na camada DAO, jamais em Service ou View
- **Registro de eventos** — toda alteração de status gera um evento com timestamp automático

---

## Configuração do banco de dados

**Pré-requisitos:** MySQL 8+ e Java 11+

O script completo de criação do banco está no arquivo [`kanban_db.sql`](kanban_db.sql). Ele inclui a criação das tabelas e dados de teste prontos para usar.

Execute no MySQL:

```bash
mysql -u root -p < kanban_db.sql
```

Ou abra o arquivo no **MySQL Workbench** e execute diretamente.

Depois atualize as credenciais em `ConnectionFactory.java`:

```java
private static final String URL     = "jdbc:mysql://localhost:3306/kanban_db";
private static final String USUARIO = "seu_usuario";
private static final String SENHA   = "sua_senha";
```

---

## Como executar

1. Configure o banco conforme acima
2. Adicione o driver MySQL JDBC como dependência no `pom.xml` ou baixe o `.jar` em [dev.mysql.com/downloads/connector/j](https://dev.mysql.com/downloads/connector/j/)
3. Importe o projeto no **NetBeans** ou **IntelliJ IDEA** e execute `ProjetoKanban.java`

---

## Funcionalidades

### Projetos
- Cadastrar, listar, atualizar e excluir projetos

### Tarefas
- Cadastrar **Bug** informando severidade (`BAIXA` / `MEDIA` / `ALTA` / `CRITICA`) e se é reproduzível
- Cadastrar **Feature** informando valor de negócio (`BAIXO` / `MEDIO` / `ALTO`) e complexidade (`SIMPLES` / `MEDIA` / `COMPLEXA`)
- Mover tarefa entre status: `FAZER → FAZENDO → FEITO`
- Ver histórico completo de eventos de uma tarefa com data e hora
- Excluir tarefa

### Relatórios

**Backlog priorizado** — lista todas as tarefas com status `FAZER` ou `FAZENDO`, ordenadas por prioridade:
- Bugs são ordenados por severidade: `CRITICA → ALTA → MEDIA → BAIXA`
- Features são ordenadas por valor de negócio: `ALTO → MEDIO → BAIXO`

**Por responsável** — lista todas as tarefas de uma pessoa com o prazo total estimado em dias

---

## Integrantes

| Nome | RGM |
|---|---|
| Giovani Ramires Cardoso dos Santos | 46104828 |
| Arthur Oliveira Souza | 42941661 |
| Arthur Ramires Quirino | 46092323 |
