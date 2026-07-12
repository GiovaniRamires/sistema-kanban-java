package br.com.giovaniramires.kanban_spring.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("FEATURE")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor

public class Feature extends Tarefa {
    public enum ValorNegocio {
        BAIXO,
        MEDIO,
        ALTO
    }

    public enum Complexidade {
        SIMPLES,
        MEDIA,
        COMPLEXA
    }

    @Enumerated(EnumType.STRING)
    private ValorNegocio valorNegocio;

    @Enumerated(EnumType.STRING)
    private Complexidade complexidade;

    public Feature (Projeto projeto, String titulo, String descricao, String responsavel, ValorNegocio valorNegocio, Complexidade complexidade){
        super(projeto, titulo, descricao, responsavel);
        this.valorNegocio = valorNegocio;
        this.complexidade = complexidade;
    }

    @Override
    public int calcularPrioridade(){
        if(valorNegocio == ValorNegocio.ALTO){
            return 1;
        } else if (valorNegocio == ValorNegocio.MEDIO){
            return 2;
        } else {return 3;} // ValorNegocio.BAIXO
    }

    @Override
    public int estimarPrazo(){
        if(complexidade == Complexidade.COMPLEXA){
            return 5;
        } else if (complexidade == Complexidade.MEDIA){
            return 15;
        } else {return 30;} // Complexidade.SIMPLES
    }
}
