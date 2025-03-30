package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Curso;
import org.example.gestaodehorario.model.Materia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MateriaDAO {
    private final CursoDAO cursoDAO = new CursoDAO();

    public void insert(Materia materia) throws SQLException {
        String sql = "INSERT INTO Materia (nome, carga_horaria, id_curso) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, materia.getNome());
            stmt.setInt(2, materia.getCargaHoraria());
            stmt.setInt(3, materia.getCurso().getIdCurso());

            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) materia.setIdMateria(rs.getInt(1));
            }
        }
    }

    public boolean update(Materia materia) throws SQLException {
        String sql = "UPDATE materia SET nome = ?, carga_horaria = ?, id_curso = ? WHERE id_materia = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, materia.getNome());
            stmt.setInt(2, materia.getCargaHoraria());
            stmt.setInt(3, materia.getCurso().getIdCurso());
            stmt.setInt(4, materia.getIdMateria());

            return stmt.executeUpdate() > 0;
        }
    }

    public void delete(int idMateria) throws SQLException {
        String sql = "DELETE FROM Materia WHERE id_materia = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMateria);
            stmt.executeUpdate();
        }
    }

    public List<Materia> getByCurso(int idCurso) throws SQLException {
        String sql = "SELECT * FROM Materia WHERE id_curso = ?";
        List<Materia> materias = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCurso);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                materias.add(new Materia(
                        rs.getInt("id_materia"),
                        rs.getString("nome"),
                        rs.getInt("carga_horaria"),
                        rs.getInt("id_curso")
                ));
            }
        }
        return materias;
    }

    public boolean existeMateria(String nome, int idCurso) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Materia WHERE nome = ? AND id_curso = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setInt(2, idCurso);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public Optional<Materia> getById(int idMateria) throws SQLException {
        String sql = """
            SELECT 
                m.id_materia, 
                m.nome AS nome_materia, 
                m.carga_horaria, 
                c.id_curso, 
                c.nome AS nome_curso 
            FROM materia m
            INNER JOIN curso c ON m.curso_id = c.id_curso
            WHERE m.id_materia = ?
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMateria);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Curso curso = new Curso();
                curso.setIdCurso(rs.getInt("id_curso"));
                curso.setNome(rs.getString("nome_curso"));

                Materia materia = new Materia(
                        rs.getInt("id_materia"),
                        rs.getString("nome_materia"),
                        rs.getInt("carga_horaria"),
                        curso
                );
                return Optional.of(materia);
            }

            return Optional.empty();
        }
    }

    public List<Materia> getBasicInfo() throws SQLException {
        String sql = "SELECT id_materia, nome FROM materia";
        List<Materia> materias = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                materias.add(new Materia(
                        rs.getInt("id_materia"),
                        rs.getString("nome")
                ));
            }
        }
        return materias;
    }

    public List<Materia> getAll() throws SQLException {
        List<Materia> materias = new ArrayList<>();
        String sql = "SELECT m.*, c.nome as nome_curso FROM Materia m " +
                "LEFT JOIN Curso c ON m.id_curso = c.id_curso";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Materia materia = new Materia();
                materia.setIdMateria(rs.getInt("id_materia"));
                materia.setNome(rs.getString("nome"));
                materia.setCargaHoraria(rs.getInt("carga_horaria"));

                Curso curso = new Curso();
                curso.setIdCurso(rs.getInt("id_curso"));
                curso.setNome(rs.getString("nome_curso"));
                materia.setCurso(curso);

                materias.add(materia);
            }
        }
        return materias;
    }
}