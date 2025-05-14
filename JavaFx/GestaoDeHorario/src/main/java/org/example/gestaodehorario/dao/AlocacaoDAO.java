package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Alocacao;
import org.example.gestaodehorario.model.RelatorioAlocacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO responsável pelas operações de persistência e consulta de objetos {@link Alocacao}.
 * <p>
 * Oferece métodos para salvar, inserir, remover e buscar alocações, além de verificar
 * a ocupação de slots em um dado semestre.
 * </p>
 */
public class AlocacaoDAO {

    /**
     * Persiste em lote uma lista de alocações.
     * Executa todos os inserts em uma única transação, e em caso de falha realiza rollback.
     */
    public void salvar(List<Alocacao> alocacoes) throws SQLException {
        String sql = "INSERT INTO Alocacao (id_professor, id_materia, id_slot, id_semestre) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Alocacao alocacao : alocacoes) {
                stmt.setInt(1, alocacao.getProfessor().getId());
                stmt.setInt(2, alocacao.getMateria().getIdMateria());
                stmt.setInt(3, alocacao.getSlot().getId_slot());
                stmt.setInt(4, alocacao.getSemestre().getIdSemestre());
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
        }
    }

    /**
     * Insere uma única alocação no banco de dados e define seu ID gerado.
     */
    public void insert(Alocacao alocacao) throws SQLException {
        String sql = "INSERT INTO Alocacao (id_professor, id_materia, id_slot, id_semestre) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, alocacao.getProfessor().getId());
            stmt.setInt(2, alocacao.getMateria().getIdMateria());
            stmt.setInt(3, alocacao.getSlot().getId_slot());
            stmt.setInt(4, alocacao.getSemestre().getIdSemestre());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    alocacao.setIdAlocacao(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Remove a alocação com o ID especificado.
     */
    public void delete(int idAlocacao) throws SQLException {
        String sql = "DELETE FROM Alocacao WHERE id_alocacao = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAlocacao);
            stmt.executeUpdate();
        }
    }

    /**
     * Remove todas as alocações de um semestre específico.
     */
    public void deleteAllBySemestre(int idSemestre) throws SQLException {
        String sql = "DELETE FROM Alocacao WHERE id_semestre = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSemestre);
            stmt.executeUpdate();
        }
    }

    /**
     * Busca uma alocação pelo seu identificador.
     */
    public Optional<Alocacao> getById(int id) throws SQLException {
        String sql = "SELECT * FROM Alocacao WHERE id_alocacao = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(mapearAlocacao(rs));
                }
                return Optional.empty();
            }
        }
    }

    /**
     * Mapeia um {@link ResultSet} para uma instância de {@link Alocacao}.
     */
    private Alocacao mapearAlocacao(ResultSet rs) throws SQLException {
        ProfessorDAO professorDAO = new ProfessorDAO();
        MateriaDAO    materiaDAO    = new MateriaDAO();
        SlotDAO       slotDAO       = new SlotDAO();
        SemestreDAO   semestreDAO   = new SemestreDAO();

        return new Alocacao(
                rs.getInt("id_alocacao"),
                professorDAO.getById(rs.getInt("id_professor")).orElse(null),
                materiaDAO.getById(rs.getInt("id_materia")).orElse(null),
                slotDAO.getById(rs.getInt("id_slot")).orElse(null),
                semestreDAO.getById(rs.getInt("id_semestre")).orElse(null)
        );
    }

    /**
     * Retorna todas as alocações existentes no banco de dados.
     */
    public List<Alocacao> getAll() throws SQLException {
        String sql = "SELECT * FROM Alocacao";
        List<Alocacao> alocacoes = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Alocacao a = mapearAlocacao(rs);
                if (a != null) alocacoes.add(a);
            }
        }
        return alocacoes;
    }

    /**
     * Retorna o número total de alocações cadastradas no sistema.
     */
    public int getTotalAlocacao() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Alocacao";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    /**
     * Busca alocações vinculadas a um semestre específico.
     */
    public List<Alocacao> getBySemestre(int idSemestre) throws SQLException {
        String sql = """
            SELECT a.* 
              FROM Alocacao a
             WHERE a.id_semestre = ?
            """;
        List<Alocacao> alocacoes = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSemestre);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Alocacao a = mapearAlocacao(rs);
                    if (a != null) alocacoes.add(a);
                }
            }
        }
        return alocacoes;
    }

    /**
     * Verifica se um dado slot já está ocupado em um semestre.
     */
    public boolean slotEstaOcupado(int idSlot, int idSemestre) throws SQLException {
        String sql = "SELECT 1 FROM Alocacao WHERE id_slot = ? AND id_semestre = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSlot);
            stmt.setInt(2, idSemestre);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Gera relatório completo de alocações.
     */
    public List<RelatorioAlocacao> getRelatorioAll() throws SQLException {
        String sql = """
            SELECT 
              c.nome AS curso, 
              s.nome AS semestre, 
              p.nome AS periodo, 
              slot.dia_semana AS dia, 
              slot.hora_inicio || ' - ' || slot.hora_fim AS horario,
              m.nome AS materia,
              j.nome AS professor
            FROM Alocacao a
            JOIN Materia   m     ON a.id_materia = m.id_materia
            JOIN Professor j     ON a.id_professor = j.id
            JOIN Curso     c     ON m.id_curso = c.id_curso
            JOIN Semestre  s     ON a.id_semestre = s.id_semestre
            JOIN Slot      slot  ON a.id_slot = slot.id_slot
            JOIN Periodo   p     ON slot.id_periodo = p.id_periodo
            ORDER BY curso, semestre, periodo, dia, slot.hora_inicio
            """;

        List<RelatorioAlocacao> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new RelatorioAlocacao(
                        rs.getString("curso"),
                        rs.getString("semestre"),
                        rs.getString("periodo"),
                        rs.getString("dia"),
                        rs.getString("horario"),
                        rs.getString("materia"),
                        rs.getString("professor")
                ));
            }
        }
        return list;
    }
}
