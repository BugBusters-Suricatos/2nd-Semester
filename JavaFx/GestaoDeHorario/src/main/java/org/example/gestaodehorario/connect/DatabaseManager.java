package org.example.gestaodehorario.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados SQLite utilizado pelo sistema de horários.
 */
public class DatabaseManager {

    /** URL de conexão JDBC para o banco de dados SQLite. */
    private static final String URL = "jdbc:sqlite:database/SistemaHorario.db";

    /**
     * Obtém uma conexão com o banco de dados.
     * <p>
     * Ao ser chamado, exibe uma mensagem de sucesso no console e retorna o objeto Connection.
     * </p>
     *
     * @return uma instância de {@link Connection} apontando para o banco de dados SQLite
     * @throws SQLException se ocorrer um erro ao tentar estabelecer a conexão
     */
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            System.out.println("Conexão estabelecida com sucesso!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Falha na conexão: " + e.getMessage());
            throw new RuntimeException("Erro crítico no banco de dados", e);
        }
    }

}
