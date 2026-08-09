
package br.edu.projetokanban.model;

import java.time.LocalDateTime;

public class EventoTarefa {

    private int id;
    private int tarefaId;
    private String descricao;
    private LocalDateTime dataHora;

    // Construtor para CRIAR um evento novo
    public EventoTarefa(int tarefaId, String descricao) {
        this.tarefaId = tarefaId;
        this.descricao = descricao;
        this.dataHora = LocalDateTime.now();
    }

    // Construtor para CARREGAR um evento do banco de dados
    public EventoTarefa(int id, int tarefaId, String descricao, LocalDateTime dataHora) {
        this.id = id;
        this.tarefaId = tarefaId;
        this.descricao = descricao;
        this.dataHora = dataHora;
    }

    public int getId(){return id;}
    public int getTarefaId(){return tarefaId;}
    public String getDescricao(){return descricao;}
    public LocalDateTime getDataHora(){return dataHora;}

    public void setId(int id){this.id = id;}

    @Override
    public String toString() {
        return "[" + dataHora + "] " + descricao;
    }
}