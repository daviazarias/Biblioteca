-- ============================================================================
-- SCRIPT DE INSERÇÃO DE DADOS - SISTEMA DE BIBLIOTECA
-- 20 registros por tabela
-- Usa INSERT IGNORE para evitar duplicação em execuções múltiplas
-- ============================================================================

USE
biblioteca_db;

-- ============================================================================
-- TABELA: CATEGORIA (20 registros)
-- ============================================================================

INSERT
IGNORE INTO categoria (id_categoria, nome, descricao)
VALUES (1, 'Programação', 'Livros sobre programação e desenvolvimento de software'),
       (2, 'Banco de Dados', 'Livros sobre modelagem, SQL e sistemas de banco de dados'),
       (3, 'Engenharia de Software', 'Livros sobre processos, metodologias e práticas de engenharia'),
       (4, 'Redes', 'Livros sobre redes de computadores e comunicação'),
       (5, 'Segurança', 'Livros sobre segurança da informação e cibersegurança'),
       (6, 'Inteligência Artificial', 'Livros sobre IA, machine learning e deep learning'),
       (7, 'Algoritmos', 'Livros sobre estruturas de dados e algoritmos'),
       (8, 'Sistemas Operacionais', 'Livros sobre SO, processos e gerenciamento de recursos'),
       (9, 'Arquitetura', 'Livros sobre arquitetura de software e sistemas'),
       (10, 'DevOps', 'Livros sobre práticas DevOps, CI/CD e automação'),
       (11, 'Web Development', 'Livros sobre desenvolvimento web frontend e backend'),
       (12, 'Mobile', 'Livros sobre desenvolvimento mobile Android e iOS'),
       (13, 'Cloud Computing', 'Livros sobre computação em nuvem e serviços cloud'),
       (14, 'Data Science', 'Livros sobre ciência de dados e análise'),
       (15, 'Matemática', 'Livros sobre matemática aplicada à computação'),
       (16, 'Design Patterns', 'Livros sobre padrões de projeto e boas práticas'),
       (17, 'Testing', 'Livros sobre testes de software e qualidade'),
       (18, 'Microservices', 'Livros sobre arquitetura de microsserviços'),
       (19, 'Containers', 'Livros sobre Docker, Kubernetes e containers'),
       (20, 'Carreira', 'Livros sobre carreira e desenvolvimento profissional');

-- ============================================================================
-- TABELA: USUARIO (20 registros)
-- Senhas criptografadas com BCrypt para 'senha123' e 'admin123'
-- ============================================================================

INSERT
IGNORE INTO usuario (id_usuario, nome, email, senha, tipo_usuario, data_cadastro, ativo)
VALUES (1, 'Admin Sistema', 'admin@biblioteca.com', '$2a$10$mEzFdBIwSvQnIv7nKSC/Z.OAiPHjEEcqxM5yhVThtRcA/YX8j5PNu',
        'ADMIN', '2024-01-15', TRUE),
       (2, 'Maria Silva Santos', 'maria.silva@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-02-20', TRUE),
       (3, 'João Pedro Oliveira', 'joao.oliveira@email.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USUARIO', '2024-02-22', TRUE),
       (4, 'Ana Paula Costa', 'ana.costa@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-03-01', TRUE),
       (5, 'Carlos Eduardo Lima', 'carlos.lima@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-03-05', TRUE),
       (6, 'Fernanda Alves', 'fernanda.alves@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-03-10', TRUE),
       (7, 'Rafael Santos', 'rafael.santos@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-03-15', TRUE),
       (8, 'Juliana Rodrigues', 'juliana.rodrigues@email.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USUARIO', '2024-03-20', TRUE),
       (9, 'Bruno Henrique', 'bruno.henrique@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-04-01', TRUE),
       (10, 'Camila Souza', 'camila.souza@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-04-05', TRUE),
       (11, 'Diego Martins', 'diego.martins@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-04-10', TRUE),
       (12, 'Eliane Ferreira', 'eliane.ferreira@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-04-15', TRUE),
       (13, 'Felipe Cardoso', 'felipe.cardoso@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-05-01', TRUE),
       (14, 'Gabriela Mendes', 'gabriela.mendes@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-05-05', TRUE),
       (15, 'Henrique Barros', 'henrique.barros@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-05-10', TRUE),
       (16, 'Isabela Rocha', 'isabela.rocha@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-05-15', TRUE),
       (17, 'Lucas Fernandes', 'lucas.fernandes@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-06-01', TRUE),
       (18, 'Mariana Gomes', 'mariana.gomes@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-06-05', TRUE),
       (19, 'Pedro Henrique', 'pedro.henrique@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-06-10', TRUE),
       (20, 'Tatiana Araújo', 'tatiana.araujo@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'USUARIO', '2024-06-15', TRUE);

-- ============================================================================
-- TABELA: LIVRO (20 registros)
-- ============================================================================

INSERT
IGNORE INTO livro (id_livro, titulo, autor, isbn, ano_publicacao, data_adicao, quantidade_disponivel, quantidade_total)
VALUES (1, 'Clean Code: A Handbook of Agile Software Craftsmanship', 'Robert C. Martin', '978-0132350884', 2008,
        '2024-01-10', 5, 5),
       (2, 'Design Patterns: Elements of Reusable Object-Oriented Software',
        'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', '978-0201633610', 1994, '2024-01-10', 3, 3),
       (3, 'The Pragmatic Programmer', 'Andrew Hunt, David Thomas', '978-0135957059', 2019, '2024-01-12', 4, 4),
       (4, 'Introduction to Algorithms', 'Thomas H. Cormen, Charles E. Leiserson', '978-0262033848', 2009, '2024-01-15',
        2, 2),
       (5, 'Database System Concepts', 'Abraham Silberschatz, Henry F. Korth', '978-0078022159', 2019, '2024-01-18', 6, 6),
       (6, 'Computer Networks', 'Andrew S. Tanenbaum, David J. Wetherall', '978-0132126953', 2010, '2024-01-20', 3, 3),
       (7, 'Operating System Concepts', 'Abraham Silberschatz, Peter B. Galvin', '978-1118063330', 2012, '2024-01-22', 4, 4),
       (8, 'Artificial Intelligence: A Modern Approach', 'Stuart Russell, Peter Norvig', '978-0136042594', 2020,
        '2024-01-25', 5, 5),
       (9, 'Python Crash Course', 'Eric Matthes', '978-1593279288', 2019, '2024-02-01', 7, 7),
       (10, 'JavaScript: The Good Parts', 'Douglas Crockford', '978-0596517748', 2008, '2024-02-05', 4, 4),
       (11, 'Refactoring: Improving the Design of Existing Code', 'Martin Fowler', '978-0134757599', 2018, '2024-02-08', 3, 3),
       (12, 'The Mythical Man-Month', 'Frederick P. Brooks Jr.', '978-0201835953', 1995, '2024-02-10', 2, 2),
       (13, 'Code Complete', 'Steve McConnell', '978-0735619678', 2004, '2024-02-12', 4, 4),
       (14, 'Cracking the Coding Interview', 'Gayle Laakmann McDowell', '978-0984782857', 2015, '2024-02-15', 8, 8),
       (15, 'Head First Design Patterns', 'Eric Freeman, Elisabeth Robson', '978-0596007126', 2004, '2024-02-18', 5, 5),
       (16, 'The Clean Coder', 'Robert C. Martin', '978-0137081073', 2011, '2024-02-20', 3, 3),
       (17, 'Domain-Driven Design', 'Eric Evans', '978-0321125215', 2003, '2024-02-22', 2, 2),
       (18, 'Building Microservices', 'Sam Newman', '978-1491950357', 2015, '2024-02-25', 4, 4),
       (19, 'Kubernetes in Action', 'Marko Luksa', '978-1617293726', 2017, '2024-02-28', 3, 3),
       (20, 'Site Reliability Engineering', 'Betsy Beyer, Chris Jones', '978-1491929124', 2016, '2024-03-01', 4, 4);

-- ============================================================================
-- TABELA: LIVRO_CATEGORIA (Relacionamento N:M - mais de 20 registros)
-- ============================================================================

INSERT
IGNORE INTO livro_categoria (id_livro, id_categoria)
VALUES
-- Clean Code
(1, 1),
(1, 3),
(1, 16),
-- Design Patterns
(2, 1),
(2, 9),
(2, 16),
-- The Pragmatic Programmer
(3, 1),
(3, 3),
(3, 20),
-- Introduction to Algorithms
(4, 7),
(4, 15),
-- Database System Concepts
(5, 2),
(5, 15),
-- Computer Networks
(6, 4),
-- Operating System Concepts
(7, 8),
-- Artificial Intelligence
(8, 6),
(8, 14),
(8, 15),
-- Python Crash Course
(9, 1),
(9, 11),
-- JavaScript: The Good Parts
(10, 1),
(10, 11),
-- Refactoring
(11, 1),
(11, 3),
(11, 16),
-- The Mythical Man-Month
(12, 3),
(12, 20),
-- Code Complete
(13, 1),
(13, 3),
(13, 17),
-- Cracking the Coding Interview
(14, 1),
(14, 7),
(14, 20),
-- Head First Design Patterns
(15, 1),
(15, 16),
-- The Clean Coder
(16, 3),
(16, 20),
-- Domain-Driven Design
(17, 9),
(17, 16),
-- Building Microservices
(18, 9),
(18, 18),
-- Kubernetes in Action
(19, 10),
(19, 13),
(19, 19),
-- Site Reliability Engineering
(20, 10),
(20, 13);

-- ============================================================================
-- TABELA: EMPRESTIMO (20 registros)
-- Mistura de empréstimos: ativos, devolvidos e atrasados
-- ============================================================================

INSERT
IGNORE INTO emprestimo (id_emprestimo, id_usuario, id_livro, data_emprestimo, data_devolucao_prevista, data_devolucao_real, status)
VALUES
-- Empréstimos DEVOLVIDOS (no prazo)
(1, 2, 1, '2024-09-01', '2024-09-15', '2024-09-14', 'DEVOLVIDO'),
(2, 3, 2, '2024-09-05', '2024-09-19', '2024-09-18', 'DEVOLVIDO'),
(3, 4, 3, '2024-09-10', '2024-09-24', '2024-09-22', 'DEVOLVIDO'),
(4, 5, 4, '2024-09-15', '2024-09-29', '2024-09-28', 'DEVOLVIDO'),
(5, 6, 5, '2024-09-20', '2024-10-04', '2024-10-03', 'DEVOLVIDO'),

-- Empréstimos DEVOLVIDOS (com atraso)
(6, 7, 6, '2024-10-01', '2024-10-15', '2024-10-20', 'DEVOLVIDO'),
(7, 8, 7, '2024-10-05', '2024-10-19', '2024-10-25', 'DEVOLVIDO'),

-- Empréstimos ATIVOS (dentro do prazo)
(8, 9, 8, '2024-11-01', '2024-11-29', NULL, 'ATIVO'),
(9, 10, 9, '2024-11-05', '2024-11-30', NULL, 'ATIVO'),
(10, 11, 10, '2024-11-08', '2024-12-05', NULL, 'ATIVO'),
(11, 12, 11, '2024-11-10', '2024-12-08', NULL, 'ATIVO'),
(12, 13, 12, '2024-11-12', '2024-12-10', NULL, 'ATIVO'),

-- Empréstimos ATRASADOS
(13, 14, 13, '2024-10-20', '2024-11-10', NULL, 'ATRASADO'),
(14, 15, 14, '2024-10-22', '2024-11-12', NULL, 'ATRASADO'),
(15, 16, 15, '2024-10-25', '2024-11-15', NULL, 'ATRASADO'),

-- Mais empréstimos ATIVOS recentes
(16, 17, 16, '2024-11-15', '2024-12-13', NULL, 'ATIVO'),
(17, 18, 17, '2024-11-16', '2024-12-14', NULL, 'ATIVO'),
(18, 19, 18, '2024-11-17', '2024-12-15', NULL, 'ATIVO'),

-- Empréstimos de livros populares (múltiplos empréstimos do mesmo livro)
(19, 2, 9, '2024-10-10', '2024-10-24', '2024-10-23', 'DEVOLVIDO'),
(20, 3, 9, '2024-10-15', '2024-10-29', '2024-10-28', 'DEVOLVIDO');

-- ============================================================================
-- VERIFICAÇÃO DOS DADOS INSERIDOS
-- ============================================================================

SELECT 'Categorias inseridas:' AS Info, COUNT(*) AS Total
FROM categoria
UNION ALL
SELECT 'Usuários inseridos:', COUNT(*)
FROM usuario
UNION ALL
SELECT 'Livros inseridos:', COUNT(*)
FROM livro
UNION ALL
SELECT 'Relacionamentos Livro-Categoria:', COUNT(*)
FROM livro_categoria
UNION ALL
SELECT 'Empréstimos inseridos:', COUNT(*)
FROM emprestimo;

-- Estatísticas dos empréstimos por status
SELECT 'Empréstimos por Status' AS Relatorio,
       status,
       COUNT(*)                 AS Total
FROM emprestimo
GROUP BY status;

-- Top 5 livros mais emprestados
SELECT 'Top 5 Livros Mais Emprestados' AS Relatorio,
       l.titulo,
       COUNT(e.id_emprestimo)          AS Total_Emprestimos
FROM livro l
         LEFT JOIN emprestimo e ON l.id_livro = e.id_livro
GROUP BY l.id_livro, l.titulo
ORDER BY Total_Emprestimos DESC LIMIT 5;

-- DADOS DE EXEMPLO - MULTAS (20 registros)
INSERT
IGNORE INTO multa (id_multa, id_usuario, id_emprestimo, valor, data_geracao, data_pagamento, status, observacao)
VALUES (1, 2, 1, 5.00, '2024-09-20', '2024-09-25', 'PAGA', 'Atraso de 5 dias'),
       (2, 3, 2, 3.00, '2024-09-22', '2024-09-28', 'PAGA', 'Atraso de 3 dias'),
       (3, 5, 4, 7.00, '2024-10-05', '2024-10-10', 'PAGA', 'Atraso de 7 dias'),
       (4, 7, 6, 10.00, '2024-10-20', '2024-10-25', 'PAGA', 'Devolução com atraso'),
       (5, 8, 7, 12.00, '2024-10-25', '2024-10-30', 'PAGA', 'Atraso significativo'),
       (6, 14, 13, 15.00, '2024-11-10', NULL, 'PENDENTE', 'Empréstimo atrasado há 8 dias'),
       (7, 15, 14, 18.00, '2024-11-12', NULL, 'PENDENTE', 'Empréstimo atrasado há 6 dias'),
       (8, 16, 15, 21.00, '2024-11-15', NULL, 'PENDENTE', 'Empréstimo atrasado há 3 dias'),
       (9, 2, NULL, 5.00, '2024-11-01', NULL, 'PENDENTE', 'Multa por dano ao livro'),
       (10, 4, NULL, 10.00, '2024-11-05', NULL, 'PENDENTE', 'Taxa de reposição parcial'),
       (11, 6, NULL, 8.00, '2024-10-15', NULL, 'CANCELADA', 'Cancelada por acordo'),
       (12, 10, NULL, 12.00, '2024-10-20', NULL, 'CANCELADA', 'Erro no cálculo'),
       (13, 11, NULL, 4.00, '2024-11-08', NULL, 'PENDENTE', 'Multa administrativa'),
       (14, 12, NULL, 6.00, '2024-11-10', '2024-11-12', 'PAGA', 'Taxa de serviço'),
       (15, 13, NULL, 3.50, '2024-11-12', NULL, 'PENDENTE', 'Atraso menor'),
       (16, 17, NULL, 25.00, '2024-11-14', NULL, 'PENDENTE', 'Perda de livro'),
       (17, 18, NULL, 8.00, '2024-11-15', '2024-11-16', 'PAGA', 'Multa quitada'),
       (18, 19, NULL, 5.50, '2024-11-16', NULL, 'PENDENTE', 'Atraso na devolução'),
       (19, 3, NULL, 7.00, '2024-11-17', NULL, 'PENDENTE', 'Danos menores'),
       (20, 9, NULL, 15.00, '2024-11-17', NULL, 'PENDENTE', 'Reposição necessária');

-- DADOS DE EXEMPLO - RESERVAS (20 registros)
INSERT
IGNORE INTO reserva (id_reserva, id_usuario, id_livro, data_reserva, data_validade, data_retirada, status)
VALUES (1, 2, 5, '2024-10-01 09:00:00', '2024-10-04', '2024-10-02 14:30:00', 'CONCLUIDA'),
       (2, 3, 8, '2024-10-05 10:15:00', '2024-10-08', '2024-10-07 11:20:00', 'CONCLUIDA'),
       (3, 4, 12, '2024-10-10 08:45:00', '2024-10-13', '2024-10-11 16:00:00', 'CONCLUIDA'),
       (4, 5, 15, '2024-10-15 14:20:00', '2024-10-18', '2024-10-17 10:30:00', 'CONCLUIDA'),
       (5, 6, 18, '2024-10-20 11:00:00', '2024-10-23', '2024-10-22 09:45:00', 'CONCLUIDA'),
       (6, 7, 3, '2024-11-15 09:30:00', '2024-11-20', NULL, 'ATIVA'),
       (7, 8, 6, '2024-11-16 10:00:00', '2024-11-21', NULL, 'ATIVA'),
       (8, 9, 11, '2024-11-16 14:15:00', '2024-11-21', NULL, 'ATIVA'),
       (9, 10, 14, '2024-11-17 08:00:00', '2024-11-22', NULL, 'ATIVA'),
       (10, 11, 17, '2024-11-17 11:30:00', '2024-11-22', NULL, 'ATIVA'),
       (11, 12, 19, '2024-11-18 09:45:00', '2024-11-23', NULL, 'ATIVA'),
       (12, 13, 4, '2024-11-10 10:00:00', '2024-11-13', NULL, 'CANCELADA'),
       (13, 14, 7, '2024-11-11 15:30:00', '2024-11-14', NULL, 'CANCELADA'),
       (14, 15, 10, '2024-11-12 09:00:00', '2024-11-15', NULL, 'CANCELADA'),
       (15, 16, 2, '2024-11-05 10:00:00', '2024-11-08', NULL, 'EXPIRADA'),
       (16, 17, 9, '2024-11-06 14:00:00', '2024-11-09', NULL, 'EXPIRADA'),
       (17, 18, 13, '2024-11-07 11:00:00', '2024-11-10', NULL, 'EXPIRADA'),
       (18, 19, 16, '2024-11-18 10:15:00', '2024-11-23', NULL, 'ATIVA'),
       (19, 20, 20, '2024-11-18 11:00:00', '2024-11-23', NULL, 'ATIVA'),
       (20, 2, 1, '2024-11-18 13:30:00', '2024-11-23', NULL, 'ATIVA');

-- ============================================================================
-- OBSERVAÇÕES IMPORTANTES
-- ============================================================================

/*
1. Todas as senhas dos usuários são: senha123
   Hash BCrypt: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

2. Para login no sistema, use qualquer email da tabela usuario com senha: senha123
   Exemplo: maria.silva@email.com / senha123

3. O admin principal é: admin@biblioteca.com / senha123

4. Os dados incluem:
   - 20 categorias diferentes
   - 20 usuários (1 admin + 19 usuários regulares)
   - 20 livros variados
   - Mais de 40 relacionamentos livro-categoria
   - 20 empréstimos com diferentes status

5. Status dos empréstimos:
   - DEVOLVIDO: 7 empréstimos
   - ATIVO: 10 empréstimos
   - ATRASADO: 3 empréstimos

6. Alguns livros têm múltiplas categorias (relacionamento N:M)

7. As datas estão distribuídas ao longo de 2024 para simular histórico real
*/

-- ============================================================================
-- FIM DO SCRIPT
-- ============================================================================
