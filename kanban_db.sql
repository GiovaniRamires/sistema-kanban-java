-- ============================================
-- SCRIPT SQL — Sistema Kanban (Grupo 3)
-- ============================================

CREATE DATABASE IF NOT EXISTS kanban_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE kanban_db;

-- --------------------------------------------
-- TABELA: projetos
-- --------------------------------------------
CREATE TABLE projetos (
    id    INT AUTO_INCREMENT PRIMARY KEY,
    nome  VARCHAR(100) NOT NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1
);

-- --------------------------------------------
-- TABELA: tarefas
-- --------------------------------------------
CREATE TABLE tarefas (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    projeto_id    INT NOT NULL,
    tipo          ENUM('BUG', 'FEATURE') NOT NULL,
    titulo        VARCHAR(150) NOT NULL,
    descricao     TEXT,
    responsavel   VARCHAR(100),
    status        ENUM('FAZER', 'FAZENDO', 'FEITO') NOT NULL DEFAULT 'FAZER',

    -- Campos exclusivos de BUG
    severidade    ENUM('BAIXA', 'MEDIA', 'ALTA', 'CRITICA') DEFAULT NULL,
    reproduzivel  TINYINT(1) DEFAULT NULL,

    -- Campos exclusivos de FEATURE
    valor_negocio ENUM('BAIXO', 'MEDIO', 'ALTO') DEFAULT NULL,
    complexidade  ENUM('SIMPLES', 'MEDIA', 'COMPLEXA') DEFAULT NULL,

    CONSTRAINT fk_tarefa_projeto
        FOREIGN KEY (projeto_id) REFERENCES projetos(id)
        ON DELETE CASCADE
);

-- --------------------------------------------
-- TABELA: eventos_tarefa
-- --------------------------------------------
CREATE TABLE eventos_tarefa (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    tarefa_id  INT NOT NULL,
    descricao  TEXT NOT NULL,
    datahora   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_evento_tarefa
        FOREIGN KEY (tarefa_id) REFERENCES tarefas(id)
        ON DELETE CASCADE
);

-- ============================================
-- INSERTS DE TESTE
-- ============================================

INSERT INTO projetos (nome, ativo) VALUES
('Sistema de Vendas', 1),
('App Mobile', 1),
('Portal RH', 0);

INSERT INTO tarefas (projeto_id, tipo, titulo, descricao, responsavel, status, severidade, reproduzivel, valor_negocio, complexidade) VALUES
(1, 'BUG',     'Erro ao finalizar pedido',  'Tela de checkout trava ao confirmar pagamento', 'Ana',    'FAZENDO', 'ALTA',    1,    NULL,    NULL),
(1, 'FEATURE', 'Filtro por categoria',       'Adicionar filtro na listagem de produtos',      'Carlos', 'FAZER',   NULL,      NULL, 'ALTO',  'MEDIA'),
(2, 'BUG',     'App fecha ao abrir câmera',  'Crash no Android 13 ao abrir câmera',          'Lucas',  'FAZER',   'CRITICA', 1,    NULL,    NULL),
(2, 'FEATURE', 'Login com Google',           'Implementar OAuth2 com Google',                 'Ana',    'FEITO',   NULL,      NULL, 'ALTO',  'COMPLEXA'),
(3, 'FEATURE', 'Relatório de ponto',         'Gerar PDF com horas trabalhadas por mês',       'Carlos', 'FAZER',   NULL,      NULL, 'MEDIO', 'MEDIA');

INSERT INTO eventos_tarefa (tarefa_id, descricao, datahora) VALUES
(1, 'Tarefa criada',                          NOW() - INTERVAL 3 DAY),
(1, 'Status alterado: FAZER → FAZENDO',       NOW() - INTERVAL 1 DAY),
(4, 'Tarefa criada',                          NOW() - INTERVAL 5 DAY),
(4, 'Status alterado: FAZER → FAZENDO',       NOW() - INTERVAL 4 DAY),
(4, 'Status alterado: FAZENDO → FEITO',       NOW() - INTERVAL 2 DAY);

-- ============================================
-- CONSULTAS DOS RELATÓRIOS (referência para o DAO)
-- ============================================

-- Relatório 1: Backlog priorizado (FAZER e FAZENDO, ordenado por tipo e severidade/valor)
SELECT t.id, p.nome AS projeto, t.tipo, t.titulo, t.responsavel, t.status,
       t.severidade, t.valor_negocio
FROM tarefas t
JOIN projetos p ON t.projeto_id = p.id
WHERE t.status <> 'FEITO'
ORDER BY
    CASE t.tipo
        WHEN 'BUG' THEN
            CASE t.severidade WHEN 'CRITICA' THEN 1 WHEN 'ALTA' THEN 2 WHEN 'MEDIA' THEN 3 ELSE 4 END
        ELSE
            CASE t.valor_negocio WHEN 'ALTO' THEN 1 WHEN 'MEDIO' THEN 2 ELSE 3 END
    END;

-- Relatório 2: Estimativa por responsável
SELECT t.responsavel,
       COUNT(*) AS total_tarefas,
       SUM(CASE WHEN t.status = 'FEITO'  THEN 1 ELSE 0 END) AS concluidas,
       SUM(CASE WHEN t.status <> 'FEITO' THEN 1 ELSE 0 END) AS pendentes
FROM tarefas t
GROUP BY t.responsavel
ORDER BY pendentes DESC;
