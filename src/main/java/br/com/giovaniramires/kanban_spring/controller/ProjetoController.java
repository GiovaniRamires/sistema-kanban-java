package br.com.giovaniramires.kanban_spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.giovaniramires.kanban_spring.dto.CriarProjetoDTO;
import br.com.giovaniramires.kanban_spring.model.Projeto;
import br.com.giovaniramires.kanban_spring.service.ProjetoService;


@RestController
@RequestMapping("/projeto")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @PostMapping
    public ResponseEntity<Projeto> criarProjeto(@RequestBody CriarProjetoDTO dto){
        var projetoCriado = projetoService.criar(dto.getNome());
        return ResponseEntity.status(HttpStatus.CREATED).body(projetoCriado);

    }

    @GetMapping
    public ResponseEntity<List<Projeto>> listarTodos() {
        return ResponseEntity.ok(projetoService.listarTodos());
    }

    @GetMapping ("/ativos")
    public ResponseEntity<List<Projeto>> listarAtivos() {
        return ResponseEntity.ok(projetoService.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projeto> listarPorId(@PathVariable Long id) {
        var buscaDeId = projetoService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(buscaDeId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody CriarProjetoDTO dto) {
        var novoNome = dto.getNome();
        projetoService.atualizar(id, novoNome);
        return ResponseEntity.status(HttpStatus.OK).body(novoNome);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        projetoService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}/arquivar")
    public ResponseEntity<Void> arquivar(@PathVariable Long id) {
        projetoService.arquivar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();   
     }
}