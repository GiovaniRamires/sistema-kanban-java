
package br.edu.projetokanban.service;

import br.edu.projetokanban.dao.ProjetoDAO;
import br.edu.projetokanban.model.Projeto;
import java.sql.SQLException;
import java.util.List;

public class ProjetoService {

    private ProjetoDAO projetoDAO = new ProjetoDAO();

    public void cadastrar(String nome) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do projeto é obrigatório.");
        }
        Projeto projeto = new Projeto(nome);
        projetoDAO.inserir(projeto);
        System.out.println("Projeto cadastrado com sucesso! ID: " + projeto.getId());
    }

    public List<Projeto> listarTodos() throws SQLException {
        return projetoDAO.listarTodos();
    }

    public Projeto buscarPorId(int id) throws SQLException {
        Projeto p = projetoDAO.buscarPorId(id);
        if (p == null) {
            throw new IllegalArgumentException("Projeto não encontrado para o ID: " + id);
        }
        return p;
    }

    public void atualizar(int id, String novoNome) throws SQLException {
        Projeto p = buscarPorId(id); 
        p.setNome(novoNome);
        projetoDAO.atualizar(p);
        System.out.println("Projeto atualizado com sucesso!");
    }

    public void excluir(int id) throws SQLException {
        buscarPorId(id); 
        projetoDAO.excluir(id);
        System.out.println("Projeto excluído com sucesso!");
    }
}