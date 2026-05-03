package br.edu.projetokanban.model;
public class Feature extends Tarefa {
    public enum ValorNegocio{
        BAIXO,
        MEDIO,
        ALTO
    }
    public enum Complexidade{
        SIMPLES,
        MEDIA,
        COMPLEXA
    }
    private ValorNegocio valorNegocio;
    private Complexidade complexidade;
    public Feature(int projetoId, String titulo, String descricao, String responsavel, ValorNegocio valornegocio, Complexidade complexidade){
        super(projetoId, titulo, descricao, responsavel);
        this.valorNegocio = valornegocio;
        this.complexidade = complexidade;
    }

    @Override
    public int calcularPrioridade(){
        if(valorNegocio == ValorNegocio.ALTO){
            return 1;
        } else if (valorNegocio == valorNegocio.MEDIO){
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public int estimarPrazo(){
        if(complexidade == Complexidade.SIMPLES){
            return 5;
        } else if(complexidade == complexidade.MEDIA){
            return 15;
        } else {
            return 30;
        }
    }
    public ValorNegocio getValorNegocio() {
        return valorNegocio;
    }
    public void setValorNegocio(ValorNegocio valorNegocio) {
        this.valorNegocio = valorNegocio;
    }
    public Complexidade getComplexidade() {
        return complexidade;
    }
    public void setComplexidade(Complexidade complexidade) {
        this.complexidade = complexidade;
    }

    @Override
    public String toString(){
       return  "[FEATURE] " +super.toString() + " Valor: " + valorNegocio + " Complexidade: " + complexidade;
    }
}