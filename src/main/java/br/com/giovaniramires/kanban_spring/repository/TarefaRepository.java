package br.com.giovaniramires.kanban_spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.giovaniramires.kanban_spring.model.Tarefa;

@Repository
public interface TarefaRepository extends JpaRepository <Tarefa, Long> {
    List<Tarefa> findByStatus(Tarefa.Status status);
    List<Tarefa> findByProjetoId(Long projetoId);
} 