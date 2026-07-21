package br.com.giovaniramires.kanban_spring.dto;


import br.com.giovaniramires.kanban_spring.model.Feature.Complexidade;
import br.com.giovaniramires.kanban_spring.model.Feature.ValorNegocio;
import lombok.Data;

@Data
public class CadastrarFeatureDTO {
    private Long projetoId;
    private String titulo;
    private String descricao;
    private String responsavel;
    private ValorNegocio valorNegocio;
    private Complexidade complexidade;
}
