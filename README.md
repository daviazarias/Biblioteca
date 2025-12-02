# Sistema de Biblioteca - Fullstack

Sistema completo de gerenciamento de biblioteca desenvolvido com Java 17, Spring Boot, MySQL e Thymeleaf.

## Tecnologias Utilizadas

### Backend

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security
- MySQL Database
- Maven

### Frontend

- Thymeleaf
- HTML5
- CSS3
- Bootstrap 5
- JavaScript (Fetch API)

## Arquitetura

O projeto segue uma arquitetura em camadas bem definida:

```
src/main/java/com/biblioteca/
├── api/                          # Controllers REST
│   ├── categoria/controller/
│   ├── dashboard/controller/
│   ├── emprestimo/controller/
│   ├── livro/controller/
│   ├── relatorio/controller/
│   └── usuario/controller/
├── categoria/
│   ├── internal/
│   │   ├── entity/               # Entidades JPA
│   │   └── repository/           # Repositórios Spring Data
│   └── open/
│       └── service/              # Lógica de negócio
├── livro/
│   ├── internal/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── mapper/               # Conversão Entity <-> DTO
│   └── open/
│       ├── dto/                  # Records para transferência de dados
│       └── service/
├── usuario/
│   ├── internal/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── mapper/
│   └── open/
│       ├── dto/
│       └── service/
├── emprestimo/
│   ├── internal/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── mapper/
│   └── open/
│       ├── dto/
│       └── service/
├── config/                       # Configurações
│   ├── SecurityConfig.java
│   ├── CustomUserDetailsService.java
│   └── DataInitializer.java
├── web/controller/               # Controllers Web (Thymeleaf)
└── BibliotecaApplication.java    # Classe principal
```

## Funcionalidades

### 1. Autenticação e Autorização

- Login com Spring Security
- Senhas criptografadas com BCrypt
- Dois tipos de usuários: ADMIN e USUARIO

### 2. Gestão de Usuários

- Cadastro de novos usuários
- Listagem de usuários ativos
- Inativação de usuários
- Validação de dados

### 3. Gestão de Livros

- Cadastro de livros com múltiplas categorias
- Listagem de livros disponíveis
- Atualização de quantidade
- Exclusão de livros
- Validação de ISBN único

### 4. Gestão de Empréstimos

- Registro de novos empréstimos
- Devolução de livros
- Controle de quantidade disponível
- Verificação de empréstimos atrasados
- Bloqueio de usuários com pendências

### 5. Relatórios e Consultas Complexas

- Livros mais populares (TOP 10)
- Livros disponíveis por categoria
- Empréstimos atrasados com dias de atraso
- Usuários com mais empréstimos (período customizável)
- Total de empréstimos por categoria

## Banco de Dados

### Modelo Relacional

```sql
usuario
(id_usuario, nome, email, senha, tipo_usuario, data_cadastro, ativo)
categoria (id_categoria, nome, descricao)
livro (id_livro, titulo, autor, isbn, ano_publicacao, data_adicao, quantidade_disponivel)
livro_categoria (id_livro, id_categoria)
emprestimo (id_emprestimo, id_usuario, id_livro, data_emprestimo, data_devolucao_prevista, data_devolucao_real, status)
```

### Relacionamentos

- Usuario 1:N Emprestimo
- Livro 1:N Emprestimo
- Livro N:M Categoria

## Instalação e Execução

### Pré-requisitos

- JDK 17 ou superior
- Maven 3.6+
- MySQL 8.0+

### Passo 1: Configurar o Banco de Dados

Crie o banco de dados MySQL:

```sql
CREATE
DATABASE biblioteca_db;
```

### Passo 2: Configurar application.properties

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca_db?createDatabaseIfNotExist=true
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### Passo 3: Executar o Script SQL (Opcional)

O Hibernate criará as tabelas automaticamente, mas você pode executar o script:

```bash
mysql -u root -p biblioteca_db < src/main/resources/schema.sql
```

### Passo 4: Compilar e Executar

```bash
# Compilar o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

Ou execute diretamente pelo JAR:

```bash
java -jar target/sistema-biblioteca-1.0.0.jar
```

### Passo 5: Acessar a Aplicação

Abra o navegador em: http://localhost:8081

## Credenciais de Acesso

A aplicação já vem com usuários pré-cadastrados:

### Administrador

- **Email:** admin@biblioteca.com
- **Senha:** admin123

### Usuário Regular

- **Email:** usuario@biblioteca.com
- **Senha:** usuario123

## API REST Endpoints

### Usuários

- `GET /api/usuarios` - Listar todos
- `GET /api/usuarios/ativos` - Listar ativos
- `GET /api/usuarios/{id}` - Buscar por ID
- `POST /api/usuarios` - Criar novo
- `DELETE /api/usuarios/{id}` - Inativar

### Livros

- `GET /api/livros` - Listar todos
- `GET /api/livros/disponiveis` - Listar disponíveis
- `GET /api/livros/{id}` - Buscar por ID
- `POST /api/livros` - Criar novo
- `DELETE /api/livros/{id}` - Deletar
- `PATCH /api/livros/{id}/quantidade` - Atualizar quantidade

### Empréstimos

- `GET /api/emprestimos` - Listar todos
- `GET /api/emprestimos/ativos` - Listar ativos
- `GET /api/emprestimos/{id}` - Buscar por ID
- `POST /api/emprestimos` - Criar novo
- `PATCH /api/emprestimos/{id}/devolver` - Registrar devolução

### Categorias

- `GET /api/categorias` - Listar todas
- `GET /api/categorias/{id}` - Buscar por ID

### Dashboard

- `GET /api/dashboard/stats` - Estatísticas gerais

### Relatórios

- `GET /api/relatorios/livros-populares` - Top 10 livros
- `GET /api/relatorios/livros-disponiveis` - Livros disponíveis
- `GET /api/relatorios/emprestimos-atrasados` - Empréstimos atrasados
- `GET /api/relatorios/usuarios-mais-emprestimos` - Top 10 usuários
- `GET /api/relatorios/livros-por-categoria` - Livros por categoria

## Estrutura de Pastas Completa

```
sistema-biblioteca/
├── src/
│   ├── main/
│   │   ├── java/com/biblioteca/
│   │   │   ├── api/
│   │   │   ├── categoria/
│   │   │   ├── config/
│   │   │   ├── emprestimo/
│   │   │   ├── livro/
│   │   │   ├── usuario/
│   │   │   ├── web/
│   │   │   └── BibliotecaApplication.java
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── fragments/
│   │       │   │   └── navbar.html
│   │       │   ├── dashboard.html
│   │       │   ├── emprestimos.html
│   │       │   ├── livros.html
│   │       │   ├── login.html
│   │       │   ├── relatorios.html
│   │       │   └── usuarios.html
│   │       ├── application.properties
│   │       └── schema.sql
├── pom.xml
└── README.md
```

## Regras de Negócio Implementadas

1. **Usuários:**
    - Email deve ser único
    - Senha criptografada com BCrypt
    - Administradores não podem ser inativados
    - Apenas usuários ativos podem fazer empréstimos

2. **Livros:**
    - ISBN deve ser único
    - Quantidade não pode ser negativa
    - Livro pode ter múltiplas categorias

3. **Empréstimos:**
    - Verificar disponibilidade do livro
    - Decrementar quantidade ao emprestar
    - Incrementar quantidade ao devolver
    - Usuários com empréstimos atrasados não podem emprestar
    - Status atualizado automaticamente (ATIVO -> ATRASADO)

## Consultas SQL Complexas

O sistema implementa 5 consultas complexas que utilizam:

1. **JOINs múltiplos** - Relacionamento entre 3+ tabelas
2. **GROUP BY e agregações** - COUNT, SUM
3. **Subconsultas** - Queries aninhadas
4. **ORDER BY e LIMIT** - Ranking e top N
5. **Funções de data** - DATEDIFF, CURRENT_DATE
6. **Condições WHERE complexas** - Múltiplos filtros

## Validações

### Entity (Jakarta Validation)

- `@NotNull`, `@NotBlank` - Campos obrigatórios
- `@Email` - Validação de email
- `@Size` - Tamanho mínimo/máximo
- `@Min`, `@Max` - Valores numéricos
- `@Pattern` - Expressões regulares
- `@Future` - Datas futuras

### Service (Regras de Negócio)

- Verificação de duplicatas
- Controle de estoque
- Validação de status
- Regras de empréstimo

## Troubleshooting

### Erro de Conexão com MySQL

Verifique se o MySQL está rodando e as credenciais estão corretas.

### Erro de compilação

Certifique-se de estar usando JDK 17 ou superior:

```bash
java -version
```

### Tabelas não criadas

Execute o script SQL manualmente ou verifique `spring.jpa.hibernate.ddl-auto=update`.

## Autor

Sistema desenvolvido para trabalho prático de Banco de Dados.

| Nome | RA |
|------|------|
| João Antonio Lassister Melo | 2024.1.08.027 |
| Davi Azarias do Vale Cabral | 2024.1.08.006 |
| Renan Catini Amaral | 2024.1.08.042 |
| Luis Renato Goulart | 2023.1.08.049 |
| João Antonio Siqueira Pascuini | 2024.1.08.028 |

## Licença

Este projeto é de código aberto para fins educacionais.
