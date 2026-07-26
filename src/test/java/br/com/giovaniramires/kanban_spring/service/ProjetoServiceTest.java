package br.com.giovaniramires.kanban_spring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.giovaniramires.kanban_spring.model.Projeto;
import br.com.giovaniramires.kanban_spring.repository.ProjetoRepository;

@ExtendWith(MockitoExtension.class)
public class ProjetoServiceTest {
    @Mock
    private ProjetoRepository projetoRepository;

    @InjectMocks
    private ProjetoService projetoService;

    @Test
    public void criarProjetoTeste() {
        Projeto projetoTeste = new Projeto("Meu Projeto");
        when(projetoRepository.save(any())).thenReturn(projetoTeste);

        Projeto resultado = projetoService.criar("Meu Projeto");

        assertEquals(projetoTeste, resultado);
        verify(projetoRepository).save(any());
    }

    @Test
    public void criarProjetoTesteFalha() {
        assertThrows(IllegalArgumentException.class, () -> {
            projetoService.criar("");
        });
    }

    @Test
    public void buscarIdInexistente() {
        when(projetoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            projetoService.buscarPorId(99L);
        });
    }

    @Test
    public void arquivarTeste() {
        Projeto projeto = new Projeto("Meu Projeto");
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        projetoService.arquivar(1L);

        verify(projetoRepository).save(projeto);
        assertFalse(projeto.isAtivo());
    }
}
