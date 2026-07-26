package br.com.giovaniramires.kanban_spring.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import br.com.giovaniramires.kanban_spring.repository.TarefaRepository;

public class TarefaServiceTest {
    @Mock
    private TarefaRepository tarefaRepository;
    
    @InjectMocks
    private TarefaService tarefaService;

}
