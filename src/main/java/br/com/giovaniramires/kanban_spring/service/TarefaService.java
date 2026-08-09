package br.com.giovaniramires.kanban_spring.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.giovaniramires.kanban_spring.model.Bug;
import br.com.giovaniramires.kanban_spring.model.Feature;
import br.com.giovaniramires.kanban_spring.model.Projeto;
import br.com.giovaniramires.kanban_spring.model.Tarefa;
import br.com.giovaniramires.kanban_spring.model.Tarefa.Status;
import br.com.giovaniramires.kanban_spring.model.Bug.Severidade;
import br.com.giovaniramires.kanban_spring.model.Feature.Complexidade;
import br.com.giovaniramires.kanban_spring.model.Feature.ValorNegocio;
import br.com.giovaniramires.kanban_spring.repository.ProjetoRepository;
import br.com.giovaniramires.kanban_spring.repository.TarefaRepository;

@Service
public class TarefaService {    
    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private ProjetoRepository projetoRepository;

    public Bug cadastrarBug(Long projetoId, String titulo, String descricao,
            String responsavel, Severidade severidade,
            boolean reproduzivel) {

        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
        Bug bug = new Bug(projeto, titulo, descricao, responsavel, severidade, reproduzivel);
        return (Bug) tarefaRepository.save(bug);
    }

    public Feature cadastrarFeature(Long projetoId, String titulo, String descricao,
            String responsavel, ValorNegocio valorNegocio,
            Complexidade complexidade) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
        Feature feature = new Feature(projeto, titulo, descricao, responsavel, valorNegocio, complexidade);
        return (Feature) tarefaRepository.save(feature);
    }

    public List<Tarefa> listarPorProjeto(Long projetoId) {
        return tarefaRepository.findByProjetoId(projetoId);
    }

    public Tarefa buscarPorId(Long tarefaId) {
        return tarefaRepository.findById(tarefaId).orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    }

    public void moverStatus(Long tarefaId, Status novoStatus) {
        if(novoStatus == null){
            throw new RuntimeException("Status inválido");
        } 
        var tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));   
        tarefa.setStatus(novoStatus);
        tarefaRepository.save(tarefa);
        
    }

    public void excluir(Long tarefaId) {
        var tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        tarefaRepository.delete(tarefa);
    }

    public List<Tarefa> relatorioResponsavel(String responsavel){
        return tarefaRepository.findByResponsavel(responsavel);
    }

    public List<Tarefa> relatorioBacklogPriorizado(){
        var tarefa = tarefaRepository.findByStatus(Status.FAZER);
        var tarefasOrdenadas = tarefa.stream().sorted((t1, t2) -> t1.calcularPrioridade() - t2.calcularPrioridade()).toList();
        return tarefasOrdenadas;
    }
}
