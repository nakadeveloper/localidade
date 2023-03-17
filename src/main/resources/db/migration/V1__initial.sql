CREATE TABLE paises (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em DATETIME(6) NOT NULL,
    atualizado_em DATETIME(6) NOT NULL,
    excluido_em DATETIME(6)
);