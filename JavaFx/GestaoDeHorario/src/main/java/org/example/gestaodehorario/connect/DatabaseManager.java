package org.example.gestaodehorario.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:database/SistemaHorario.db";

    public static Connection getConnection() throws SQLException {
        System.out.println("Banco de dados conectado com sucesso!");
        return DriverManager.getConnection(URL);
    }

}