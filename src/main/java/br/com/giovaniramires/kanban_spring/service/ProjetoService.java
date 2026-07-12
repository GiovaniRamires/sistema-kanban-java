package br.com.giovaniramires.kanban_spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.giovaniramires.kanban_spring.model.Projeto;
import br.com.giovaniramires.kanban_spring.repository.ProjetoRepository;

@Service
public class ProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;


    public Projeto criar(String nome){
        Projeto projeto = new Projeto(nome);
        System.out.println("Projeto de nome:" + nome + "criado com sucesso");
        return projetoRepository.save(projeto);
        
    }

    public List<Projeto> listarAtivos(){
        return projetoRepository.findByAtivoTrue();
    }

    public Projeto buscarPorId (Long id){
        return projetoRepository.findById(id).orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
    }

    public void atualizar(Long id, String novoNome){
        Projeto projeto = buscarPorId(id);
        projeto.setNome(novoNome);
        projetoRepository.save(projeto);
        System.out.println("Projeto de id:" + id + "atualizado com sucesso");
    }

    public void arquivar(Long id){
        Projeto projeto = buscarPorId(id);
        projeto.setAtivo(false);
        projetoRepository.save(projeto);
        System.out.println("Projeto de id:" + id + "arquivado com sucesso");
    }

    public void deletar (Long id){
        Projeto projeto = buscarPorId(id);
        projetoRepository.delete(projeto);
        System.out.println("Projeto de id:" + id + "deletado com sucesso");
    }

}
