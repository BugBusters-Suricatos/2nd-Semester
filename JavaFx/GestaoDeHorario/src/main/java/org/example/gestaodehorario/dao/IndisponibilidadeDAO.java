package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Indisponibilidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelo acesso aos dados da entidade {@link Indisponibilidade} no banco de dados.
 */
public class IndisponibilidadeDAO {

    /**
     * Obtém a lista de indisponibilidades de um professor específico.
     *
     * @param idProfessor O ID do professor.
     * @return Lista de {@link Indisponibilidade} do professor.
     * @throws SQLException Caso ocorra um erro de acesso ao banco de dados.
     */
    public List<Indisponibilidade> getByProfessor(int idProfessor) throws SQLException {
        List<Indisponibilidade> indisponibilidades = new ArrayList<>();
        String sql = """
            SELECT dia_semana, hora_inicio, hora_fim 
            FROM indisponibilidade 
            WHERE id_professor = ?
            ORDER BY 
                CASE dia_semana
                    WHEN 'Segunda' THEN 1
                    WHEN 'Terça' THEN 2
                    WHEN 'Quarta' THEN 3
                    WHEN 'Quinta' THEN 4
                    WHEN 'Sexta' THEN 5
                    WHEN 'Sábado' THEN 6
                    ELSE 7
                END,
                hora_inicio
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProfessor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Indisponibilidade indisp = new Indisponibilidade();
                    indisp.setDia_semana(rs.getString("dia_semana"));
                    indisp.setHora_inicio(rs.getString("hora_inicio"));
                    indisp.setHora_fim(rs.getString("hora_fim"));
                    indisponibilidades.add(indisp);
                }
            }
        }
        return indisponibilidades;
    }

    /**
     * Remove todas as disponibilidades associadas a um professor específico.
     *
     * @param professorId O ID do professor.
     * @throws SQLException Caso ocorra um erro de acesso ao banco de dados.
     */
    public void removerPorProfessor(int professorId) throws SQLException {
        String sql = "DELETE FROM disponibilidades WHERE professor_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, professorId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Salva a lista de indisponibilidades de um professor, substituindo as antigas.
     *
     * @param idProfessor O ID do professor.
     * @param indisponibilidades A lista de indisponibilidades a serem salvas.
     * @throws SQLException Caso ocorra um erro de acesso ao banco de dados.
     */
    public void salvarIndisponibilidades(int idProfessor, List<Indisponibilidade> indisponibilidades) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            // Remove existentes
            String deleteSql = "DELETE FROM indisponibilidade WHERE id_professor = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, idProfessor);
                deleteStmt.executeUpdate();
            }

            // Insere novas
            String insertSql = """
                INSERT INTO indisponibilidade 
                (id_professor, dia_semana, hora_inicio, hora_fim)
                VALUES (?, ?, ?, ?)
                """;

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                for (Indisponibilidade indip : indisponibilidades) {
                    insertStmt.setInt(1, idProfessor);
                    insertStmt.setString(2, indip.getDia_semana());
                    insertStmt.setString(3, indip.getHora_inicio());
                    insertStmt.setString(4, indip.getHora_fim());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Salva uma lista de indisponibilidades diretamente no banco de dados.
     *
     * @param indisponibilidades A lista de indisponibilidades a serem salvas.
     * @throws SQLException Caso ocorra um erro de acesso ao banco de dados.
     */
    public void salvar(List<Indisponibilidade> indisponibilidades) throws SQLException {
        String sql = "INSERT INTO Disponibilidade (id_professor, dia_semana, hora_inicio, hora_fim, id_curso, id_semestre) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Indisponibilidade indisp : indisponibilidades) {
                stmt.setInt(1, indisp.getId_professor());
                stmt.setString(2, indisp.getDia_semana());
                stmt.setString(3, indisp.getHora_inicio());
                stmt.setString(4, indisp.getHora_fim());
                stmt.setInt(5, indisp.getId_curso());
                stmt.setInt(6, indisp.getId_semestre());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}
