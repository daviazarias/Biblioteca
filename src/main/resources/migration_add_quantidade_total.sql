-- ============================================================================
-- SCRIPT DE MIGRAÇÃO - Adicionar coluna quantidade_total
-- Execute este script se o banco de dados já existir
-- ============================================================================

USE
biblioteca_db;

-- Adicionar coluna quantidade_total se não existir
ALTER TABLE livro
    ADD COLUMN IF NOT EXISTS quantidade_total INT NOT NULL DEFAULT 0;

-- Atualizar quantidade_total com os valores atuais de quantidade_disponivel
UPDATE livro
SET quantidade_total = quantidade_disponivel
WHERE quantidade_total = 0;

-- Adicionar check constraint
ALTER TABLE livro
    ADD CONSTRAINT chk_quantidade_total CHECK (quantidade_total >= 0);

-- Verificar resultado
SELECT id_livro, titulo, quantidade_disponivel, quantidade_total
FROM livro LIMIT 10;

