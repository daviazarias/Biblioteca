package com.biblioteca.config;

import com.biblioteca.categoria.internal.entity.Categoria;
import com.biblioteca.categoria.internal.repository.CategoriaRepository;
import com.biblioteca.livro.internal.entity.Livro;
import com.biblioteca.livro.internal.repository.LivroRepository;
import com.biblioteca.usuario.internal.entity.Usuario;
import com.biblioteca.usuario.internal.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private final CategoriaRepository categoriaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Verificar se já existem dados
        if (usuarioRepository.count() > 0) {
            return;
        }

        // Criar usuários
        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.setEmail("admin@biblioteca.com");
        admin.setSenha(passwordEncoder.encode("admin123"));
        admin.setTipoUsuario(Usuario.TipoUsuario.ADMIN);
        admin.setDataCadastro(LocalDate.now());
        admin.setAtivo(true);
        usuarioRepository.save(admin);

        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("usuario@biblioteca.com");
        usuario.setSenha(passwordEncoder.encode("usuario123"));
        usuario.setTipoUsuario(Usuario.TipoUsuario.USUARIO);
        usuario.setDataCadastro(LocalDate.now());
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);

        // Criar categorias
        Categoria programacao = new Categoria();
        programacao.setNome("Programação");
        programacao.setDescricao("Livros sobre programação e desenvolvimento");
        categoriaRepository.save(programacao);

        Categoria design = new Categoria();
        design.setNome("Design");
        design.setDescricao("Padrões de design e arquitetura");
        categoriaRepository.save(design);

        Categoria metodologia = new Categoria();
        metodologia.setNome("Metodologia");
        metodologia.setDescricao("Práticas e metodologias de desenvolvimento");
        categoriaRepository.save(metodologia);

        // Criar livros
        Livro livro1 = new Livro();
        livro1.setTitulo("Clean Code");
        livro1.setAutor("Robert C. Martin");
        livro1.setIsbn("978-0132350884");
        livro1.setAnoPublicacao(2008);
        livro1.setDataAdicao(LocalDate.now());
        livro1.setQuantidadeDisponivel(5);
        livro1.adicionarCategoria(programacao);
        livroRepository.save(livro1);

        Livro livro2 = new Livro();
        livro2.setTitulo("Design Patterns");
        livro2.setAutor("Gang of Four");
        livro2.setIsbn("978-0201633610");
        livro2.setAnoPublicacao(1994);
        livro2.setDataAdicao(LocalDate.now());
        livro2.setQuantidadeDisponivel(3);
        livro2.adicionarCategoria(design);
        livroRepository.save(livro2);

        Livro livro3 = new Livro();
        livro3.setTitulo("The Pragmatic Programmer");
        livro3.setAutor("Andrew Hunt & David Thomas");
        livro3.setIsbn("978-0201616224");
        livro3.setAnoPublicacao(1999);
        livro3.setDataAdicao(LocalDate.now());
        livro3.setQuantidadeDisponivel(4);
        livro3.adicionarCategoria(metodologia);
        livroRepository.save(livro3);

        Livro livro4 = new Livro();
        livro4.setTitulo("Refactoring");
        livro4.setAutor("Martin Fowler");
        livro4.setIsbn("978-0134757599");
        livro4.setAnoPublicacao(2018);
        livro4.setDataAdicao(LocalDate.now());
        livro4.setQuantidadeDisponivel(2);
        livro4.adicionarCategoria(programacao);
        livroRepository.save(livro4);

        log.info("Dados iniciais carregados com sucesso!");
        log.info("Admin: admin@biblioteca.com / admin");
        log.info("Usuario: usuario@biblioteca.com / usuario123");
    }
}
