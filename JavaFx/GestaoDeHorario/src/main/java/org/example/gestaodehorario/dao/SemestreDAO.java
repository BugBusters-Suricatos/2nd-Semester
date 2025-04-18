package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Semestre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) para operações com entidades Semestre no banco de dados.
 * Gerencia operações CRUD para semestres acadêmicos, incluindo inserção, atualização,
 * exclusão e recuperação de registros.
 */
public class SemestreDAO {

    /**
     * Insere um novo semestre no banco de dados
     * @param semestre Objeto Semestre a ser persistido (o ID será gerado automaticamente)
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
    public void insert(Semestre semestre) throws SQLException {
        String sql = "INSERT INTO Semestre (nome, ano) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, semestre.getNome());
            stmt.setInt(2, semestre.getAno());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    semestre.setIdSemestre(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Atualiza os dados de um semestre existente
     * @param semestre Objeto Semestre com os novos dados (deve conter ID válido)
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
    public void update(Semestre semestre) throws SQLException {
        String sql = "UPDATE Semestre SET nome = ?, ano = ? WHERE id_semestre = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, semestre.getNome());
            stmt.setInt(2, semestre.getAno());
            stmt.setInt(3, semestre.getIdSemestre());

            stmt.executeUpdate();
        }
    }

    /**
     * Remove permanentemente um semestre do sistema
     * @param idSemestre ID do semestre a ser excluído
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
    public void delete(int idSemestre) throws SQLException {
        String sql = "DELETE FROM Semestre WHERE id_semestre = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSemestre);
            stmt.executeUpdate();
        }
    }

    /**
     * Busca um semestre pelo seu ID único
     * @param id ID do semestre a ser recuperado
     * @return Optional contendo o semestre encontrado ou vazio se não existir
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
    public Optional<Semestre> getById(int id) throws SQLException {
        String sql = "SELECT * FROM Semestre WHERE id_semestre = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Semestre(
                            rs.getInt("id_semestre"),
                            rs.getString("nome"),
                            rs.getInt("ano")
                    ));
                }
                return Optional.empty();
            }
        }
    }

    /**
     * Recupera todos os semestres cadastrados no sistema
     * @return Lista de todos os semestres (pode ser vazia se não houver registros)
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
    public List<Semestre> getAll() throws SQLException {
        List<Semestre> semestres = new ArrayList<>();
        String sql = "SELECT * FROM Semestre";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                semestres.add(new Semestre(
                        rs.getInt("id_semestre"),
                        rs.getString("nome"),
                        rs.getInt("ano")
                ));
            }
        }
        return semestres;
    }
}