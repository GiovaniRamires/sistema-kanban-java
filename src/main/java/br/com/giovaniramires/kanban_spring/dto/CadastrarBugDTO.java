package br.com.giovaniramires.kanban_spring.dto;

import br.com.giovaniramires.kanban_spring.model.Bug.Severidade;
import lombok.Data;

@Data
public class CadastrarBugDTO {
    private Long projetoId;
    private String titulo;
    private String descricao;
    private String responsavel;
    private Severidade severidade;
    private boolean reproduzivel;
}
