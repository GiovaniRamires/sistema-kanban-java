package br.com.giovaniramires.kanban_spring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.giovaniramires.kanban_spring.model.Bug;
import br.com.giovaniramires.kanban_spring.model.Feature;
import br.com.giovaniramires.kanban_spring.model.Projeto;
import br.com.giovaniramires.kanban_spring.model.Tarefa;
import br.com.giovaniramires.kanban_spring.model.Bug.Severidade;
import br.com.giovaniramires.kanban_spring.model.Feature.Complexidade;
import br.com.giovaniramires.kanban_spring.model.Feature.ValorNegocio;
import br.com.giovaniramires.kanban_spring.model.Tarefa.Status;
import br.com.giovaniramires.kanban_spring.repository.ProjetoRepository;
import br.com.giovaniramires.kanban_spring.repository.TarefaRepository;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {
    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private ProjetoRepository projetoRepository;

    @InjectMocks
    private TarefaService tarefaService;

    @Test
    public void testeCadastrarBugSucesso(){
        Projeto projeto = new Projeto("Meu Projeto");
        Bug bug = new Bug(projeto, "titulo", "desc", "resp", Severidade.ALTA, true);

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(tarefaRepository.save(any())).thenReturn(bug);

        Bug resultado = tarefaService.cadastrarBug(1L, "titulo", "desc", "resp", Severidade.ALTA, true);
        
        assertEquals(Tarefa.Status.FAZER, resultado.getStatus());
        verify(tarefaRepository).save(any());
    }

    @Test
    public void testeCadastrarBugErro(){
        when(projetoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            tarefaService.cadastrarBug(99L, "", "", "", Severidade.BAIXA, false);
        });
    }

    @Test
    public void testeCadastrarFeatureSucesso(){
        Projeto projeto = new Projeto("Meu projeto");
        Feature feature = new Feature(projeto, "Nova Feature", "desc Nova Feature", 
        "Giovani", ValorNegocio.ALTO, Complexidade.COMPLEXA);
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(tarefaRepository.save(any())).thenReturn(feature);

        Feature resultado = tarefaService.cadastrarFeature(1L, "Nova Feature", "desc Nova Feature", 
        "Giovani", ValorNegocio.ALTO, Complexidade.COMPLEXA);

        assertEquals(Tarefa.Status.FAZER, resultado.getStatus());
        verify(tarefaRepository).save(any());
    }
    @Test
    public void testeCadastrarFeatureErro(){
        when(projetoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            tarefaService.cadastrarFeature(99L, "", "", "", ValorNegocio.BAIXO, Complexidade.MEDIA);
        });
    }

    @Test
    public void moverStatusSucesso(){
        Projeto projeto = new Projeto("Meu projeto");
        Bug bug = new Bug(projeto, "titulo", "desc", "resp", Severidade.ALTA, true);

        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(bug));

        tarefaService.moverStatus(1L, Status.FAZENDO);

        verify(tarefaRepository).save(any());
        assertEquals(Tarefa.Status.FAZENDO, bug.getStatus());

    }

    @Test
    public void moverStatusNulo(){
        assertThrows(RuntimeException.class, () -> {
            tarefaService.moverStatus(1L, null);
        });
    }

    @Test
    public void moverStatusTarefaNotFound(){
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            tarefaService.moverStatus(99L, Status.FAZENDO);
        });
    }

    @Test
    public void buscarPorIdTarefaNotFound(){
        when(tarefaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            tarefaService.buscarPorId(99L);
        });
    }

    @Test
    public void deletarSucesso(){
        Projeto projeto = new Projeto("Meu projeto");
        Bug bug = new Bug(projeto, "titulo", "desc", "resp", Severidade.ALTA, true);
        
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(bug));

        tarefaService.excluir(1L);

        verify(tarefaRepository).delete(any());
    }

    @Test
    public void relatorioPrioziadoSucesso(){
        Projeto projeto = new Projeto("Meu projeto");
        Bug bugCritico = new Bug(projeto, "Bug Critico", "teste de Bug Critico", "Giovani", Severidade.CRITICA, true);
        Bug bugBaixo = new Bug(projeto, "Bug Baixa", "teste de Bug Baixa", "Giovani", Severidade.BAIXA, true);

        when(tarefaRepository.findByStatus(Status.FAZER)).thenReturn(List.of(bugBaixo, bugCritico));

        var resultado = tarefaService.relatorioBacklogPriorizado();

        assertEquals(bugCritico, resultado.get(0));
    }
}
