package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Materia;
import org.example.gestaodehorario.model.Professor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) para operações com entidades Professor no banco de dados.
 * Gerencia o CRUD de professores e suas relações com matérias.
 * Utiliza {@link MateriaDAO} para operações relacionadas a matérias.
 */
public class ProfessorDAO {
    private final MateriaDAO materiaDAO = new MateriaDAO();

    /**
     * Insere um novo professor no banco de dados
     * @param professor Objeto Professor a ser persistido (deve conter uma matéria válida)
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Recupera todos os professores com informações completas de suas matérias
     * @return Lista de professores com dados da matéria associada (usando INNER JOIN)
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Atualiza os dados de um professor existente
     * @param professor Objeto Professor com os novos dados (deve conter ID válido)
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Remove permanentemente um professor do sistema
     * @param idProfessor ID do professor a ser excluído
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
    public void delete(int idProfessor) throws SQLException {
        String sql = "DELETE FROM professor WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProfessor);
            stmt.executeUpdate();
        }
    }

    /**
     * Cria um objeto Materia a partir de um ResultSet
     * @param rs ResultSet contendo os dados da matéria
     * @return Objeto Materia construído
     * @throws SQLException Se ocorrer um erro ao acessar os dados
     */
    private Materia criarMateria(ResultSet rs) throws SQLException {
        return new Materia(
                rs.getInt("idMateria"),
                rs.getString("nome_materia")
        );
    }

    /**
     * Cria um objeto Professor a partir de um ResultSet e Matéria
     * @param rs ResultSet contendo os dados do professor
     * @param materia Matéria associada ao professor
     * @return Objeto Professor construído
     * @throws SQLException Se ocorrer um erro ao acessar os dados
     */
    private Professor criarProfessor(ResultSet rs, Materia materia) throws SQLException {
        Professor professor = new Professor(
                rs.getString("nome_professor"),
                rs.getString("email"),
                materia
        );
        professor.setId(rs.getInt("id"));
        return professor;
    }

    /**
     * Busca um professor pelo ID com sua matéria associada
     * @param id ID do professor a ser recuperado
     * @return Optional contendo o professor encontrado (a matéria pode ser null se não existir)
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Retorna o número total de professores cadastrados no sistema
     * @return Quantidade total de professores
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
    public int getTotalProfessores() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Professor";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }
}