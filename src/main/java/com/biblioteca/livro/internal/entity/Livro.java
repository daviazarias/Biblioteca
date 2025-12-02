package com.biblioteca.livro.internal.entity;

import com.biblioteca.categoria.internal.entity.Categoria;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "livro")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "categorias")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livro")
    private Long idLivro;

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    @Column(nullable = false, length = 200)
    private String titulo;

    @NotBlank(message = "Autor é obrigatório")
    @Size(max = 100, message = "Autor deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String autor;

    @NotBlank(message = "ISBN é obrigatório")
    @Pattern(regexp = "^[0-9-]+$", message = "ISBN deve conter apenas números e hífens")
    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @NotNull(message = "Ano de publicação é obrigatório")
    @Min(value = 1000, message = "Ano de publicação inválido")
    @Max(value = 2100, message = "Ano de publicação inválido")
    @Column(name = "ano_publicacao", nullable = false)
    private Integer anoPublicacao;

    @NotNull(message = "Data de adição é obrigatória")
    @Column(name = "data_adicao", nullable = false)
    private LocalDate dataAdicao;

    @NotNull(message = "Quantidade disponível é obrigatória")
    @Min(value = 0, message = "Quantidade não pode ser negativa")
    @Column(name = "quantidade_disponivel", nullable = false)
    private Integer quantidadeDisponivel = 0;

    @NotNull(message = "Quantidade total é obrigatória")
    @Min(value = 0, message = "Quantidade total não pode ser negativa")
    @Column(name = "quantidade_total", nullable = false)
    private Integer quantidadeTotal = 0;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "livro_categoria",
            joinColumns = @JoinColumn(name = "id_livro"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private Set<Categoria> categorias = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (dataAdicao == null) {
            dataAdicao = LocalDate.now();
        }
    }

    public void adicionarCategoria(Categoria categoria) {
        this.categorias.add(categoria);
        categoria.getLivros().add(this);
    }

    public void removerCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
        categoria.getLivros().remove(this);
    }
}
