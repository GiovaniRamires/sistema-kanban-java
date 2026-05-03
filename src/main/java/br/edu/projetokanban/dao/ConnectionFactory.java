
package br.edu.projetokanban.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://localhost:3306/kanban_db";
    private static final String USUARIO = "SEU_USUÁRIO";
    private static final String SENHA = "SUA_SENHA";
    
    private ConnectionFactory (){}
    
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
    
    
    
}
