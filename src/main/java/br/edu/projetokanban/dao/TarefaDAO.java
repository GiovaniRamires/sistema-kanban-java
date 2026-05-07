
package br.edu.projetokanban.dao;

import br.edu.projetokanban.model.*;
import br.edu.projetokanban.model.Bug.Severidade;
import br.edu.projetokanban.model.Feature.ValorNegocio;
import br.edu.projetokanban.model.Feature.Complexidade;
import br.edu.projetokanban.model.Tarefa.Status;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {


    public void inserir(Tarefa tarefa) throws SQLException {
        String sql = "INSERT INTO tarefas "
                   + "(projeto_id, tipo, titulo, descricao, responsavel, status, "
                   + " severidade, reproduzivel, valor_negocio, complexidade) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, tarefa.getProjetoId());
            ps.setString(5, tarefa.getResponsavel());
            ps.setString(6, tarefa.getStatus().name()); // converte enum para String

            // Verifica o tipo real do objeto para preencher os campos certos
            if (tarefa instanceof Bug) {
                Bug bug = (Bug) tarefa;
                ps.setString(2, "BUG");
                ps.setString(3, bug.getTitulo());
                ps.setString(4, bug.getDescricao());
                ps.setString(7, bug.getSeveridade().name());
                ps.setBoolean(8, bug.isReproduzivel());
                ps.setNull(9, Types.VARCHAR);  // valor_negocio não se aplica a Bug
                ps.setNull(10, Types.VARCHAR); // complexidade não se aplica a Bug

            } else if (tarefa instanceof Feature) {
                Feature feature = (Feature) tarefa;
                ps.setString(2, "FEATURE");
                ps.setString(3, feature.getTitulo());
                ps.setString(4, feature.getDescricao());
                ps.setNull(7, Types.VARCHAR);  // severidade não se aplica a Feature
                ps.setNull(8, Types.BOOLEAN);  // reproduzivel não se aplica a Feature
                ps.setString(9, feature.getValorNegocio().name());
                ps.setString(10, feature.getComplexidade().name());
            }

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                tarefa.setId(rs.getInt(1));
            }
        }
    }

    
    public List<Tarefa> listarPorProjeto(int projetoId) throws SQLException {
        String sql = "SELECT * FROM tarefas WHERE projeto_id = ?";
        List<Tarefa> lista = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, projetoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(montarTarefa(rs));
            }
        }
        return lista;
    }


    public Tarefa buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tarefas WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return montarTarefa(rs);
            }
        }
        return null;
    }


    public void atualizar(Tarefa tarefa) throws SQLException {
        String sql = "UPDATE tarefas SET titulo = ?, descricao = ?, responsavel = ?, "
                   + "status = ?, severidade = ?, reproduzivel = ?, "
                   + "valor_negocio = ?, complexidade = ? WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tarefa.getTitulo());
            ps.setString(2, tarefa.getDescricao());
            ps.setString(3, tarefa.getResponsavel());
            ps.setString(4, tarefa.getStatus().name());

            if (tarefa instanceof Bug) {
                Bug bug = (Bug) tarefa;
                ps.setString(5, bug.getSeveridade().name());
                ps.setBoolean(6, bug.isReproduzivel());
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);
            } else {
                Feature feature = (Feature) tarefa;
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.BOOLEAN);
                ps.setString(7, feature.getValorNegocio().name());
                ps.setString(8, feature.getComplexidade().name());
            }

            ps.setInt(9, tarefa.getId());
            ps.executeUpdate();
        }
    }


    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM tarefas WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    
    public List<Tarefa> listarBacklogPriorizado() throws SQLException {
        String sql = "SELECT * FROM tarefas WHERE status <> 'FEITO' "
                   + "ORDER BY "
                   + "CASE tipo "
                   + "  WHEN 'BUG' THEN "
                   + "    CASE severidade WHEN 'CRITICA' THEN 1 WHEN 'ALTA' THEN 2 "
                   + "                    WHEN 'MEDIA' THEN 3 ELSE 4 END "
                   + "  ELSE "
                   + "    CASE valor_negocio WHEN 'ALTO' THEN 1 WHEN 'MEDIO' THEN 2 ELSE 3 END "
                   + "END";

        List<Tarefa> lista = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(montarTarefa(rs));
            }
        }
        return lista;
    }

    
    public List<Tarefa> listarPorResponsavel(String responsavel) throws SQLException {
        String sql = "SELECT * FROM tarefas WHERE responsavel = ? ORDER BY status";

        List<Tarefa> lista = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, responsavel);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(montarTarefa(rs));
            }
        }
        return lista;
    }

    
    private Tarefa montarTarefa(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        Tarefa tarefa;

        if ("BUG".equals(tipo)) {
            Bug bug = new Bug(
                rs.getInt("projeto_id"),
                rs.getString("titulo"),
                rs.getString("descricao"),
                rs.getString("responsavel"),
                Severidade.valueOf(rs.getString("severidade")),
                rs.getBoolean("reproduzivel")
            );
            tarefa = bug;

        } else {
            Feature feature = new Feature(
                rs.getInt("projeto_id"),
                rs.getString("titulo"),
                rs.getString("descricao"),
                rs.getString("responsavel"),
                ValorNegocio.valueOf(rs.getString("valor_negocio")),
                Complexidade.valueOf(rs.getString("complexidade"))
            );
            tarefa = feature;
        }

        // Campos comuns a Bug e Feature
        tarefa.setId(rs.getInt("id"));
        tarefa.setStatus(Status.valueOf(rs.getString("status")));
        return tarefa;
    }
}