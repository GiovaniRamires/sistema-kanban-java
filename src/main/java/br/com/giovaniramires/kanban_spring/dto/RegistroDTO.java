package br.com.giovaniramires.kanban_spring.dto;

import lombok.Data;

@Data
public class RegistroDTO {
    private String nome;
    private String senha;
    private String email;
}
