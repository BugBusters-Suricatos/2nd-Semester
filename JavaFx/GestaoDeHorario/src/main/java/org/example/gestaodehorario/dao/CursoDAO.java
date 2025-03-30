package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Coordenador;
import org.example.gestaodehorario.model.Curso;

import java.sql.*;
import java.util.*;
import static org.example.gestaodehorario.connect.DatabaseManager.getConnection;

public class CursoDAO {
    public void insert(Curso curso) throws SQLException {
        String sql = "INSERT INTO Curso (nome, id_periodo, id_coordenador) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, curso.getNome());
            stmt.setInt(2, curso.getIdPeriodo());
            stmt.setInt(3, curso.getCoordenador().getIdCoordenador());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    curso.setIdCurso(rs.getInt(1));
                }
            }
        }
    }

    public void update(Curso curso) throws SQLException {
        String sql = "UPDATE Curso SET nome = ?, id_periodo = ?, id_coordenador = ? WHERE id_curso = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, curso.getNome());
            stmt.setInt(2, curso.getIdPeriodo());
            stmt.setInt(3, curso.getCoordenador().getIdCoordenador());
            stmt.setInt(4, curso.getIdCurso());
            stmt.executeUpdate();
        }
    }

    public void delete(int idCurso) throws SQLException {
        String sql = "DELETE FROM Curso WHERE id_curso = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCurso);
            stmt.executeUpdate();
        }
    }

    public Optional<Curso> getById(int idCurso) throws SQLException {
        String sql = "SELECT * FROM curso WHERE id_curso = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCurso);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CoordenadorDAO coordenadorDAO = new CoordenadorDAO();
                Coordenador coordenador = coordenadorDAO.getById(rs.getInt("id_coordenador")).orElse(null);

                return Optional.of(new Curso(
                        rs.getInt("id_curso"),
                        rs.getString("nome"),
                        rs.getInt("id_periodo"),
                        coordenador
                ));
            }
            return Optional.empty();
        }
    }

    public boolean existeCurso(String nome, int idPeriodo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Curso WHERE nome = ? AND id_periodo = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setInt(2, idPeriodo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public List<Curso> getAll() throws SQLException {
        String sql = "SELECT id_curso, nome, id_periodo, id_coordenador FROM Curso";
        List<Curso> cursos = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            CoordenadorDAO coordenadorDAO = new CoordenadorDAO();

            while (rs.next()) {
                Coordenador coordenador = coordenadorDAO.getById(rs.getInt("id_coordenador")).orElse(null);
                cursos.add(new Curso(
                        rs.getInt("id_curso"),
                        rs.getString("nome"),
                        rs.getInt("id_periodo"),
                        coordenador
                ));
            }
        }
        return cursos;
    }
}