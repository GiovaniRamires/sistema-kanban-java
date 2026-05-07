
package br.edu.projetokanban.model;

//ABSTRACT usado quando uma classe não pode ser instanciada
public abstract class Tarefa {
    public enum Status{
            FAZER, //To DO
            FAZENDO, //Doing
            FEITO //Done
    }
    private int id;
    private int projetoId;
    private String titulo;
    private String descricao;
    private String responsavel;
    private Status status;
    
    
    public Tarefa(int projetoId, String titulo, String descricao, String responsavel){
        if(titulo == null || titulo.trim().isEmpty()){
            throw new IllegalArgumentException("Nome do projeto não pode ser vazio");
        }
        this.titulo = titulo.trim();
        this.projetoId = projetoId;
        this.descricao = descricao;
        this.responsavel = responsavel;
        this.status = Status.FAZER;
    }
    public abstract int calcularPrioridade();
    public abstract int estimarPrazo();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjetoId() {
        return projetoId;
    }

    public void setProjetoId(int projetoId) {
        this.projetoId = projetoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if(titulo == null || titulo.trim().isEmpty()){
            throw new IllegalArgumentException("Nome do projeto não pode ser vazio");
        }
        this.titulo = titulo.trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    @Override
    public String toString(){
        return "[" + id + "]" + titulo + " Status " + status + " Responsável " + responsavel + " Prioridade: " + calcularPrioridade()  
               + " Prazo " + estimarPrazo() + "dias";
                
    }
}
