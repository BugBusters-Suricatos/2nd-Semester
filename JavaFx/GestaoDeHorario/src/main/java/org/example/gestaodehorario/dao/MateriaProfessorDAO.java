package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Materia;
import org.example.gestaodehorario.model.MateriaProfessor;
import org.example.gestaodehorario.model.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) para operações com a tabela de relacionamento Materia_Professor.
 * Gerencia associações entre matérias e professores no banco de dados.
 * Utiliza {@link MateriaDAO} e {@link ProfessorDAO} para operações relacionadas.
 */
public class MateriaProfessorDAO {

    /**
     * Insere uma nova associação entre matéria e professor no banco de dados.
     * @param materiaProfessor Objeto contendo a relação matéria-professor a ser persistida
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Remove uma associação matéria-professor pelo ID.
     * @param idMateriaProfessor ID único da relação a ser excluída
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
    public void delete(int idMateriaProfessor) throws SQLException {
        String sql = "DELETE FROM Materia_Professor WHERE id_materia_professor = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMateriaProfessor);
            stmt.executeUpdate();
        }
    }

    /**
     * Recupera uma associação específica pelo ID da matéria e do professor.
     * @param idMateria ID da matéria
     * @param idProfessor ID do professor
     * @return Optional contendo a associação encontrada ou vazio se não existir
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Busca uma associação matéria-professor pelo ID único da relação.
     * @param id ID único da relação matéria-professor
     * @return Optional contendo a associação completa ou vazio se não encontrada
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Lista todas as associações matéria-professor cadastradas no sistema.
     * @return Lista completa de relações matéria-professor
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Recupera todas as associações relacionadas a uma matéria específica.
     * @param idMateria ID da matéria para filtragem
     * @return Lista de relações contendo a matéria especificada e seus professores associados
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Recupera todas as associações relacionadas a um professor específico.
     * @param idProfessor ID do professor para filtragem
     * @return Lista de relações contendo o professor especificado e suas matérias associadas
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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