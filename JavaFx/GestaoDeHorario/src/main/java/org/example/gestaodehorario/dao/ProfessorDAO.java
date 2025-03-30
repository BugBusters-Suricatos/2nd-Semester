package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Materia;
import org.example.gestaodehorario.model.Professor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfessorDAO {
    private final MateriaDAO materiaDAO = new MateriaDAO();

    // Método para inserir um novo professor
    public void insert(Professor professor) throws SQLException {
        String sql = "INSERT INTO professor (nome, email, materia_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, professor.getNome());
            stmt.setString(2, professor.getEmail());
            stmt.setInt(3, professor.getMateria().getIdMateria());
            stmt.executeUpdate();
        }
    }

    // Método para buscar todos os professores com suas matérias
    public List<Professor> getAllComMaterias() throws SQLException {
        String sql = """
            SELECT 
                p.id, 
                p.nome AS nome_professor, 
                p.email,
                m.id_materia AS idMateria,
                m.nome AS nome_materia
            FROM professor p
            INNER JOIN materia m ON p.materia_id = m.id_materia
            """;

        List<Professor> professores = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Materia materia = new Materia(
                        rs.getInt("idMateria"),
                        rs.getString("nome_materia")
                );
                Professor professor = new Professor(
                        rs.getString("nome_professor"),
                        rs.getString("email"),
                        materia
                );
                professor.setId(rs.getInt("id"));
                professores.add(professor);
            }
        }
        return professores;
    }

    // Método para atualizar um professor
    public void update(Professor professor) throws SQLException {
        String sql = """
            UPDATE professor 
            SET nome = ?, email = ?, materia_id = ?
            WHERE id = ?
            """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, professor.getNome());
            stmt.setString(2, professor.getEmail());
            stmt.setInt(3, professor.getMateria().getIdMateria());
            stmt.setInt(4, professor.getId());
            stmt.executeUpdate();
        }
    }

    // Método para excluir um professor
    public void delete(int idProfessor) throws SQLException {
        String sql = "DELETE FROM professor WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProfessor);
            stmt.executeUpdate();
        }
    }

    // Métodos auxiliares
    private Materia criarMateria(ResultSet rs) throws SQLException {
        return new Materia(
                rs.getInt("idMateria"),
                rs.getString("nome_materia")
        );
    }

    private Professor criarProfessor(ResultSet rs, Materia materia) throws SQLException {
        Professor professor = new Professor(
                rs.getString("nome_professor"),
                rs.getString("email"),
                materia
        );
        professor.setId(rs.getInt("id"));
        return professor;
    }

    public Optional<Professor> getById(int id) throws SQLException {
        String sql = "SELECT * FROM professor WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Optional<Materia> materia = materiaDAO.getById(rs.getInt("materia_id"));
                return Optional.of(new Professor(
                        rs.getString("nome"),
                        rs.getString("email"),
                        materia.orElse(null)
                ));
            }
            return Optional.empty();
        }
    }
}