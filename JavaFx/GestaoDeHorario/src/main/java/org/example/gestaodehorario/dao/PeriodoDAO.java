package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Periodo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) para operações com entidades Periodo no banco de dados.
 * Gerencia operações CRUD (Create, Read, Update, Delete) para períodos acadêmicos.
 * Os IDs dos períodos são gerados automaticamente pelo banco de dados.
 */
public class PeriodoDAO {

    /**
     * Recupera todos os períodos cadastrados no sistema
     * @return Lista de períodos (pode ser vazia se não houver registros)
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Busca um período pelo seu ID único
     * @param id ID do período a ser recuperado
     * @return Optional contendo o período encontrado ou vazio se não existir
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Insere um novo período no banco de dados (ID é gerado automaticamente)
     * @param periodo Objeto Periodo a ser persistido (o ID será atualizado com o valor gerado)
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Atualiza os dados de um período existente
     * @param periodo Objeto Periodo com os novos dados (deve conter o ID válido)
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
    public void update(Periodo periodo) throws SQLException {
        String sql = "UPDATE Periodo SET nome = ? WHERE id_periodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, periodo.getNome());
            stmt.setInt(2, periodo.getIdPeriodo());
            stmt.executeUpdate();
        }
    }

    /**
     * Remove permanentemente um período do sistema
     * @param idPeriodo ID do período a ser excluído
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
    public void delete(int idPeriodo) throws SQLException {
        String sql = "DELETE FROM Periodo WHERE id_periodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPeriodo);
            stmt.executeUpdate();
        }
    }
}