# Estrutura de Arquivos Estáticos

Este documento descreve a organização dos arquivos CSS e JavaScript do sistema.

## Diretórios

```
src/main/resources/static/
├── css/           # Folhas de estilo
│   ├── main.css   # Estilos globais do sistema
│   └── login.css  # Estilos específicos da página de login
└── js/            # Scripts JavaScript
    ├── utils.js   # Funções utilitárias compartilhadas
    ├── dashboard.js
    ├── usuarios.js
    ├── livros.js (TODO)
    ├── emprestimos.js (TODO)
    ├── reservas.js (TODO)
    ├── multas.js
    └── relatorios.js (TODO)
```

## CSS

### main.css

Contém estilos globais aplicados em todo o sistema:

- Gradientes de fundo
- Cards modernos
- Botões com gradiente
- Estilos de accordion
- Badges e tabelas

### login.css

Estilos específicos para a página de login.

## JavaScript

### utils.js

Biblioteca de utilitários com funções compartilhadas:

- `BibliotecaUtils.formatarData()` - Formata datas para pt-BR
- `BibliotecaUtils.formatarDataHora()` - Formata data e hora
- `BibliotecaUtils.formatarMoeda()` - Formata valores monetários
- `BibliotecaUtils.mostrarErro()` - Exibe mensagens de erro
- `BibliotecaUtils.mostrarSucesso()` - Exibe mensagens de sucesso
- `BibliotecaUtils.confirmar()` - Caixa de confirmação

### Módulos de Páginas

Cada página possui seu próprio módulo JavaScript encapsulado em um objeto:

- **DashboardApp** - Carrega estatísticas do dashboard
- **UsuariosApp** - Gerenciamento de usuários (CRUD)
- **MultasApp** - Gerenciamento de multas e pagamentos

## Uso nos Templates

Os templates HTML referenciam os arquivos estáticos desta forma:

```html

<head>
    <!-- Bootstrap e Bootstrap Icons via WebJars -->
    <link href="/webjars/bootstrap/5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webjars/bootstrap-icons/1.11.1/font/bootstrap-icons.css" rel="stylesheet">

    <!-- CSS customizado -->
    <link href="/css/main.css" rel="stylesheet">
</head>
<body>
<!-- Conteúdo da página -->

<!-- Scripts -->
<script src="/webjars/bootstrap/5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="/js/utils.js"></script>
<script src="/js/[modulo].js"></script>
</body>
```

## Benefícios da Centralização

1. **Manutenibilidade**: Código centralizado facilita correções e melhorias
2. **Cache**: Navegadores podem cachear arquivos estáticos separadamente
3. **Reutilização**: Funções comuns (utils.js) disponíveis em todas as páginas
4. **Organização**: Separação clara entre apresentação (HTML), estilo (CSS) e comportamento (JS)
5. **Performance**: Arquivos podem ser minificados e comprimidos em produção

## TODO

- [ ] Extrair JavaScript de livros.html para livros.js
- [ ] Extrair JavaScript de emprestimos.html para emprestimos.js
- [ ] Extrair JavaScript de reservas.html para reservas.js
- [ ] Extrair JavaScript de relatorios.html para relatorios.js
- [ ] Adicionar minificação de CSS/JS para produção
- [ ] Implementar versionamento de assets (cache busting)

