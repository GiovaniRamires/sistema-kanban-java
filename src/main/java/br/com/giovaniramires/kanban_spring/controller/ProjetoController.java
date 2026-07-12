package br.com.giovaniramires.kanban_spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.giovaniramires.kanban_spring.model.Projeto;
import br.com.giovaniramires.kanban_spring.repository.ProjetoRepository;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/projeto")
public class ProjetoController {
    
    @Autowired
    private ProjetoRepository projetoRepository;
    
@PostMapping("/")
public ResponseEntity<Projeto> criar(@RequestBody Projeto projetoModel, HttpServletRequest request){
    System.out.println("Chegou no ProjetoService" + request.getAttribute("idProjeto"));
    var idProjeto = request.getAttribute("idProjeto");
    projetoModel.setId((Long)idProjeto);
    var projeto = this.projetoRepository.save(projetoModel);
    return ResponseEntity.status(HttpStatus.OK).body(projeto);
}

@GetMapping("/list")
public List<Projeto> listarTodos(HttpServletRequest request) {
    var projetos = this.projetoRepository.findAll();
    System.out.println(projetos);
    return projetos;
}

@GetMapping("/list/{id}")
public List<Projeto> listarPorId(@PathVariable Long id) {
    var projeto = this.projetoRepository.findById(id).orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
    return List.of(projeto);
}

@PutMapping("/update/{id}")
public ResponseEntity<Projeto> atualizar(@PathVariable Long id, @RequestBody Projeto projetoModel) {
    var projeto = this.projetoRepository.findById(id).orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
    projeto.setNome(projetoModel.getNome());
    projeto.setAtivo(projetoModel.isAtivo());
    var projetoAtualizado = this.projetoRepository.save(projeto);
    return ResponseEntity.status(HttpStatus.OK).body(projetoAtualizado);
}

@DeleteMapping("/delete/{id}")
public ResponseEntity<Void> deletar(@PathVariable Long id) {
    var projeto = this.projetoRepository.findById(id).orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
    this.projetoRepository.delete(projeto);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
}

public ResponseEntity<Void> arquivar(@PathVariable Long id) {
    var projeto = this.projetoRepository.findById(id).orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
    projeto.setAtivo(false);
    this.projetoRepository.save(projeto);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
}
}