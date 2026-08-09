
package br.edu.projetokanban.model;

public class Bug extends Tarefa {
    public enum Severidade{
        BAIXA,
        MEDIA,
        ALTA,
        CRITICA
    }
    
    private Severidade severidade;
    private boolean reproduzivel;
    
    public Bug(int projetoId, String titulo, String descricao, String responsavel, Severidade severidade, boolean reproduzivel){
        super(projetoId, titulo, descricao, responsavel);
        this.severidade = severidade;
        this.reproduzivel = reproduzivel;
    }
    
    @Override
    public int calcularPrioridade(){
        int prioridade;
        if(severidade == Severidade.CRITICA){
            prioridade = 1;
        } else if (severidade == Severidade.ALTA){
            prioridade = 2;
        } else if (severidade == Severidade.MEDIA){
            prioridade = 3;
        } else {
            prioridade = 4; //Severidade.BAIXA
        }
        
        if (reproduzivel && prioridade > 1) {
            prioridade--;
        }
        return prioridade;
    }
    
    @Override
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

    public Severidade getSeveridade() {
        return severidade;
    }

    public void setSeveridade(Severidade severidade) {
        this.severidade = severidade;
    }

    public boolean isReproduzivel() {
        return reproduzivel;
    }

    public void setReproduzivel(boolean reproduzivel) {
        this.reproduzivel = reproduzivel;
    }
    
    @Override
    public String toString(){
        return "[BUG]" + super.toString() + " Severidade: " + severidade + " Reproduzivel: " + "(" + (reproduzivel ? "sim" : "não") + ")";
     }
    
}
