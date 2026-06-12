CREATE SCHEMA IF NOT EXISTS saldos;

CREATE TABLE IF NOT EXISTS saldos.saldo_diario (
    id UUID PRIMARY KEY,
    comerciante_id UUID NOT NULL,
    data_referencia DATE NOT NULL,
    total_creditos NUMERIC(19, 2) NOT NULL DEFAULT 0,
    total_debitos NUMERIC(19, 2) NOT NULL DEFAULT 0,
    saldo NUMERIC(19, 2) NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_saldo_diario_comerciante_data UNIQUE (comerciante_id, data_referencia)
);

CREATE INDEX IF NOT EXISTS idx_saldo_diario_comerciante_data
    ON saldos.saldo_diario (comerciante_id, data_referencia DESC);
