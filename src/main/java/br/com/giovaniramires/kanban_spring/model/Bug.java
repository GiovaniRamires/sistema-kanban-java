package br.com.giovaniramires.kanban_spring.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("BUG")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Bug extends Tarefa {
    public enum Severidade {
        BAIXA,
        MEDIA,
        ALTA,
        CRITICA
    }

    @Enumerated(EnumType.STRING)
    private Severidade severidade;

    private boolean reproduzivel;

    public Bug(Projeto projeto, String titulo, String descricao, String responsavel, Severidade severidade,
            boolean reproduzivel) {
                super(projeto, titulo, descricao, responsavel);
                this.severidade = severidade;
                this.reproduzivel = reproduzivel;
    }

    public int calcularPrioridade(){
        int prioridade;
        if(severidade == Severidade.CRITICA){
            prioridade = 1;
        } else if (severidade == Severidade.ALTA){
            prioridade = 2;
        } else if (severidade == Severidade.MEDIA){
            prioridade = 3;
        } else {
            prioridade = 4; // Severidade.BAIXA
        }
        
        if(reproduzivel && prioridade > 1) {
            prioridade--;
        }
        return prioridade;
    }

    public int estimarPrazo(){
        if(severidade == Severidade.CRITICA){
            return 1;
        } else if (severidade == Severidade.ALTA){
            return 3;
        } else if (severidade == Severidade.MEDIA){
            return 7;
        } else {
            return 14;
        }
    }

}