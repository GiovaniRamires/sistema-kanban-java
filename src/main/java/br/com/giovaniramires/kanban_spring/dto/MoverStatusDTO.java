package br.com.giovaniramires.kanban_spring.dto;

import br.com.giovaniramires.kanban_spring.model.Tarefa.Status;
import lombok.Data;

@Data
public class MoverStatusDTO {
    private Long tarefaId;
    private Status novoStatus;
}
