package br.com.giovaniramires.kanban_spring.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tarefas")
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class Tarefa {
    public enum Status {
        FAZER,
        FAZENDO,
        FEITO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @Column(nullable = false)
    private String titulo;

    private String descricao;
    private String responsavel;

    @Enumerated(EnumType.STRING)
    private Status status = Status.FAZER;

    private LocalDateTime criadoEm;

    public Tarefa(Projeto projeto, String titulo, String descricao, String responsavel) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do projeto não pode ser vazio");
        }
        this.projeto = projeto;
        this.titulo = titulo;
        this.descricao = descricao;
        this.responsavel = responsavel;
        this.criadoEm = LocalDateTime.now();
    }

    public abstract int calcularPrioridade();
    public abstract int estimarPrazo();
}
