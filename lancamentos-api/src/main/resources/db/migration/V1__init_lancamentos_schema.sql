CREATE SCHEMA IF NOT EXISTS lancamentos;

CREATE TABLE IF NOT EXISTS lancamentos.comerciantes (
    id UUID PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS lancamentos.lancamentos (
    id UUID PRIMARY KEY,
    comerciante_id UUID NOT NULL REFERENCES lancamentos.comerciantes(id),
    tipo VARCHAR(20) NOT NULL,
    valor NUMERIC(19, 2) NOT NULL CHECK (valor > 0),
    data_referencia DATE NOT NULL,
    descricao VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_lancamentos_comerciante_data
    ON lancamentos.lancamentos (comerciante_id, data_referencia DESC);
