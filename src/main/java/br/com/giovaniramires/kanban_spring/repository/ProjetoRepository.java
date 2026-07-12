package br.com.giovaniramires.kanban_spring.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.giovaniramires.kanban_spring.model.Projeto;


@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long>{
    List<Projeto> findByAtivoTrue();
}