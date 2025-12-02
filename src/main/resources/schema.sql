-- Criação do banco de dados
CREATE
DATABASE IF NOT EXISTS biblioteca_db;
USE
biblioteca_db;

-- Tabela de Usuários
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR (100) NOT NULL,
    email VARCHAR (100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario ENUM('ADMIN','USUARIO') NOT NULL DEFAULT 'USUARIO',
    data_cadastro DATE NOT NULL,
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de Categorias
CREATE TABLE IF NOT EXISTS categoria (
    id_categoria BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao TEXT
);

-- Tabela de Livros
CREATE TABLE IF NOT EXISTS livro (
    id_livro BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR (200) NOT NULL,
    autor VARCHAR (100) NOT NULL,
    isbn VARCHAR (20) NOT NULL UNIQUE,
    ano_publicacao INT NOT NULL,
    data_adicao DATE NOT NULL,
    quantidade_disponivel INT NOT NULL DEFAULT 0,
    quantidade_total INT NOT NULL DEFAULT 0,
    CHECK (quantidade_disponivel >= 0),
    CHECK (quantidade_total >= 0)
);

-- Tabela de Relacionamento Livro-Categoria
CREATE TABLE IF NOT EXISTS livro_categoria (
    id_livro BIGINT NOT NULL,
    id_categoria BIGINT NOT NULL,
    PRIMARY KEY (id_livro, id_categoria),
    FOREIGN KEY (id_livro) REFERENCES livro (id_livro) ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES categoria (id_categoria) ON DELETE CASCADE
);

-- Tabela de Empréstimos
CREATE TABLE IF NOT EXISTS emprestimo (
    id_emprestimo BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_livro BIGINT NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_devolucao_prevista DATE NOT NULL,
    data_devolucao_real DATE,
    status ENUM ('ATIVO', 'DEVOLVIDO', 'ATRASADO') NOT NULL DEFAULT 'ATIVO',
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_livro) REFERENCES livro (id_livro) ON DELETE CASCADE
);

-- TABELA MULTA
CREATE TABLE IF NOT EXISTS multa (
    id_multa BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_emprestimo BIGINT,
    valor DECIMAL(10,2) NOT NULL,
    data_geracao DATE NOT NULL,
    data_pagamento DATE,
    status ENUM ('PENDENTE', 'PAGA', 'CANCELADA') NOT NULL DEFAULT 'PENDENTE',
    observacao TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_emprestimo) REFERENCES emprestimo (id_emprestimo) ON DELETE SET NULL
);

-- TABELA RESERVA
CREATE TABLE IF NOT EXISTS reserva (
    id_reserva BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_livro BIGINT NOT NULL,
    data_reserva DATETIME NOT NULL,
    data_validade DATE NOT NULL,
    data_retirada DATETIME,
    status ENUM ('ATIVA', 'CONCLUIDA', 'CANCELADA', 'EXPIRADA') NOT NULL DEFAULT 'ATIVA',
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_livro) REFERENCES livro (id_livro) ON DELETE CASCADE
);

-- Índices para otimização
CREATE INDEX idx_usuario_email ON usuario (email);
CREATE INDEX idx_livro_isbn ON livro (isbn);
CREATE INDEX idx_emprestimo_status ON emprestimo (status);
CREATE INDEX idx_emprestimo_datas ON emprestimo (data_emprestimo, data_devolucao_prevista);
