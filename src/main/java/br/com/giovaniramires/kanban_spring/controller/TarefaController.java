package br.com.giovaniramires.kanban_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.giovaniramires.kanban_spring.service.TarefaService;

import br.com.giovaniramires.kanban_spring.dto.CadastrarBugDTO;
import br.com.giovaniramires.kanban_spring.dto.CadastrarFeatureDTO;
import br.com.giovaniramires.kanban_spring.dto.MoverStatusDTO;
import br.com.giovaniramires.kanban_spring.model.Tarefa;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {
    @Autowired
    private TarefaService tarefaService;

    @PostMapping("/bug")
    public ResponseEntity<Tarefa> cadastrarBug(@RequestBody CadastrarBugDTO dto) {
        var tarefaCadastrada = tarefaService.cadastrarBug(dto.getProjetoId(), dto.getTitulo(), dto.getDescricao(),
                dto.getResponsavel(), dto.getSeveridade(), dto.isReproduzivel());
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaCadastrada);
    }

    @PostMapping("/feature")
    public ResponseEntity<Tarefa> cadastrarFeature(@RequestBody CadastrarFeatureDTO dto) {
        var tarefaCadastrada = tarefaService.cadastrarFeature(dto.getProjetoId(), dto.getTitulo(), dto.getDescricao(),
                dto.getResponsavel(), dto.getValorNegocio(), dto.getComplexidade());
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaCadastrada);
    }

    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<?> listarPorProjeto(@PathVariable Long projetoId) {
        var tarefas = tarefaService.listarPorProjeto(projetoId);
        return ResponseEntity.status(HttpStatus.OK).body(tarefas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarPorId(@PathVariable Long id) {
        var tarefa = tarefaService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(tarefa);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody MoverStatusDTO dto) {
        tarefaService.moverStatus(id, dto.getNovoStatus());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        tarefaService.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @GetMapping("/responsavel/{responsavel}")
    public ResponseEntity<?> listarPorResponsavel(@PathVariable String responsavel) {
        var tarefas = tarefaService.relatorioResponsavel(responsavel);
        return ResponseEntity.status(HttpStatus.OK).body(tarefas);
    }
    
    @GetMapping("/backlog")
    public ResponseEntity<?> listarBacklogPriorizado() {
        var tarefas = tarefaService.relatorioBacklogPriorizado();
        return ResponseEntity.status(HttpStatus.OK).body(tarefas);
    }
}