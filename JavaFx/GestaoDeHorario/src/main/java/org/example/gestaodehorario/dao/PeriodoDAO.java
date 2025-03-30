package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Periodo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PeriodoDAO {

    // Busca todos os períodos cadastrados
    public List<Periodo> getAll() throws SQLException {
        String sql = "SELECT * FROM Periodo";
        List<Periodo> periodos = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                periodos.add(new Periodo(
                        rs.getInt("id_periodo"),
                        rs.getString("nome")
                ));
            }
        }
        return periodos;
    }

    // Busca um período pelo ID
    public Optional<Periodo> getById(int id) throws SQLException {
        String sql = "SELECT * FROM Periodo WHERE id_periodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Periodo(
                        rs.getInt("id_periodo"),
                        rs.getString("nome")
                ));
            }
            return Optional.empty();
        }
    }

    // Insere um novo período (ID é autoincrement)
    public void insert(Periodo periodo) throws SQLException {
        String sql = "INSERT INTO Periodo (nome) VALUES (?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, periodo.getNome());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    periodo.setIdPeriodo(rs.getInt(1)); // Atualiza o ID gerado
                }
            }
        }
    }

    // Atualiza o nome de um período existente
    public void update(Periodo periodo) throws SQLException {
        String sql = "UPDATE Periodo SET nome = ? WHERE id_periodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, periodo.getNome());
            stmt.setInt(2, periodo.getIdPeriodo());
            stmt.executeUpdate();
        }
    }

    // Remove um período pelo ID
    public void delete(int idPeriodo) throws SQLException {
        String sql = "DELETE FROM Periodo WHERE id_periodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPeriodo);
            stmt.executeUpdate();
        }
    }
}
