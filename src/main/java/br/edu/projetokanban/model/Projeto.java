
package br.edu.projetokanban.model;

public class Projeto {
    private int id;
    private String nome;
    private boolean ativo;
    
    //Construtor usado pelo usuário
    public Projeto(String nome){
        if(nome == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("Nome do projeto não pode ser vazio");
        }
        this.nome = nome.trim();
        this.ativo = true;
    }
    //Construtor usado pelo banco de dados
    public Projeto(int id, String nome, boolean ativo){
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if(nome == null || nome.trim().isEmpty()){
        throw new IllegalArgumentException("Nome do projeto não pode ser vazio");
        }
        this.nome = nome.trim();
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    @Override
    public String toString(){
        return "[" + id + "] " + nome + " " + (ativo ? "ativo" : "inativo"); 
    }
}
