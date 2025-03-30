package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Materia;
import org.example.gestaodehorario.model.MateriaProfessor;
import org.example.gestaodehorario.model.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MateriaProfessorDAO {
    public void insert(MateriaProfessor materiaProfessor) throws SQLException {
        String sql = "INSERT INTO Materia_Professor (id_materia, id_professor) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, materiaProfessor.getMateria().getIdMateria());
            stmt.setInt(2, materiaProfessor.getProfessor().getId());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    materiaProfessor.setIdMateriaProfessor(rs.getInt(1));
                }
            }
        }
    }

    public void delete(int idMateriaProfessor) throws SQLException {
        String sql = "DELETE FROM Materia_Professor WHERE id_materia_professor = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMateriaProfessor);
            stmt.executeUpdate();
        }
    }

    public Optional<MateriaProfessor> getByMateriaProfessor(int idMateria, int idProfessor) throws SQLException {
        String sql = "SELECT * FROM Materia_Professor WHERE id_materia = ? AND id_professor = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMateria);
            stmt.setInt(2, idProfessor);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Materia materia = new MateriaDAO().getById(rs.getInt("id_materia")).orElse(null);
                Professor professor = new ProfessorDAO().getById(rs.getInt("id_professor")).orElse(null);

                return Optional.of(new MateriaProfessor(
                        rs.getInt("id_materia_professor"),
                        materia,
                        professor
                ));
            }
            return Optional.empty();
        }
    }

    public Optional<MateriaProfessor> getById(int id) throws SQLException {
        String sql = "SELECT * FROM Materia_Professor WHERE id_materia_professor = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Materia materia = new MateriaDAO().getById(rs.getInt("id_materia")).orElse(null);
                Professor professor = new ProfessorDAO().getById(rs.getInt("id_professor")).orElse(null);

                return Optional.of(new MateriaProfessor(
                        rs.getInt("id_materia_professor"),
                        materia,
                        professor
                ));
            }
            return Optional.empty();
        }
    }


    public List<MateriaProfessor> getAll() throws SQLException {
        List<MateriaProfessor> materiasProfessores = new ArrayList<>();
        String sql = "SELECT * FROM Materia_Professor";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Materia materia = new MateriaDAO().getById(rs.getInt("id_materia"))
                        .orElseThrow(() -> new RuntimeException("Matéria não encontrada"));
                Professor professor = new ProfessorDAO().getById(rs.getInt("id_professor"))
                        .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
                materiasProfessores.add(new MateriaProfessor(
                        rs.getInt("id_materia_professor"),
                        materia,
                        professor
                ));
            }
        }
        return materiasProfessores;
    }

    public List<MateriaProfessor> getByMateria(int idMateria) throws SQLException {
        List<MateriaProfessor> list = new ArrayList<>();
        String sql = "SELECT * FROM Materia_Professor WHERE id_materia = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMateria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Materia materia = new MateriaDAO().getById(idMateria)
                            .orElseThrow(() -> new RuntimeException("Matéria não encontrada"));
                    Professor professor = new ProfessorDAO().getById(rs.getInt("id_professor"))
                            .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
                    list.add(new MateriaProfessor(
                            rs.getInt("id_materia_professor"),
                            materia,
                            professor
                    ));
                }
            }
        }
        return list;
    }

    public List<MateriaProfessor> getByProfessor(int idProfessor) throws SQLException {
        List<MateriaProfessor> list = new ArrayList<>();
        String sql = "SELECT * FROM Materia_Professor WHERE id_professor = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProfessor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Materia materia = new MateriaDAO().getById(rs.getInt("id_materia"))
                            .orElseThrow(() -> new RuntimeException("Matéria não encontrada"));
                    Professor professor = new ProfessorDAO().getById(idProfessor)
                            .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
                    list.add(new MateriaProfessor(
                            rs.getInt("id_materia_professor"),
                            materia,
                            professor
                    ));
                }
            }
        }
        return list;
    }
}