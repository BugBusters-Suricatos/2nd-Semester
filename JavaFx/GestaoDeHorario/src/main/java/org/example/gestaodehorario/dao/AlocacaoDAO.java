package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Alocacao;

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
     * Persiste em lote uma lista de alocações e atualiza o status dos slots correspondentes.
     * <p>
     * Executa todos os inserts em uma única transação, e em caso de falha realiza rollback.
     * </p>
     *
     * @param alocacoes lista de {@link Alocacao} a serem salvas
     * @throws SQLException se ocorrer erro de acesso ao banco de dados ou na transação
     */
    public void salvar(List<Alocacao> alocacoes) throws SQLException {
        String sql = "INSERT INTO Alocacao (id_materia_professor, id_slot, id_semestre) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // inicia transação

            try {
                for (Alocacao alocacao : alocacoes) {
                    stmt.setInt(1, alocacao.getMateriaProfessor().getIdMateriaProfessor());
                    stmt.setInt(2, alocacao.getSlot().getId_slot());
                    stmt.setInt(3, alocacao.getSemestre().getIdSemestre());
                    stmt.addBatch();

                    // Atualiza status do slot para 'Ocupado'
                    new SlotDAO().atualizarStatus(
                            alocacao.getSlot().getDia_semana(),
                            alocacao.getSlot().getHora_inicio(),
                            alocacao.getSlot().getId_periodo(),
                            "Ocupado"
                    );
                }

                stmt.executeBatch();
                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    /**
     * Insere uma única alocação no banco de dados e define seu ID gerado.
     *
     * @param alocacao instância de {@link Alocacao} a ser inserida
     * @throws SQLException se ocorrer erro ao acessar o banco de dados
     */
    public void insert(Alocacao alocacao) throws SQLException {
        String sql = "INSERT INTO Alocacao (id_materia_professor, id_slot, id_semestre) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, alocacao.getMateriaProfessor().getIdMateriaProfessor());
            stmt.setInt(2, alocacao.getSlot().getId_slot());
            stmt.setInt(3, alocacao.getSemestre().getIdSemestre());

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
     *
     * @param idAlocacao identificador da alocação a ser removida
     * @throws SQLException se ocorrer erro ao acessar o banco de dados
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
     * Busca uma alocação pelo seu identificador.
     *
     * @param id identificador da alocação
     * @return {@link Optional} contendo a alocação se encontrada, ou vazio caso contrário
     * @throws SQLException se ocorrer erro ao acessar o banco de dados
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
     *
     * @param rs {@link ResultSet} posicionado na linha a ser mapeada
     * @return nova instância de {@link Alocacao} com dados do ResultSet
     * @throws SQLException se ocorrer erro ao ler dados do ResultSet
     */
    private Alocacao mapearAlocacao(ResultSet rs) throws SQLException {
        MateriaProfessorDAO mpDAO = new MateriaProfessorDAO();
        SlotDAO slotDAO = new SlotDAO();
        SemestreDAO semestreDAO = new SemestreDAO();

        return new Alocacao(
                rs.getInt("id_alocacao"),
                mpDAO.getById(rs.getInt("id_materia_professor")).orElse(null),
                slotDAO.getById(rs.getInt("id_slot")).orElse(null),
                semestreDAO.getById(rs.getInt("id_semestre")).orElse(null)
        );
    }

    /**
     * Retorna todas as alocações existentes no banco de dados.
     *
     * @return lista de {@link Alocacao}
     * @throws SQLException se ocorrer erro ao acessar o banco de dados
     */
    public List<Alocacao> getAll() throws SQLException {
        String sql = "SELECT * FROM Alocacao";
        List<Alocacao> alocacoes = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Alocacao alocacao = mapearAlocacao(rs);
                if (alocacao != null) {
                    alocacoes.add(alocacao);
                }
            }
        }
        return alocacoes;
    }

    /**
     * Busca alocações vinculadas a um semestre específico.
     *
     * @param idSemestre identificador do semestre
     * @return lista de {@link Alocacao} associadas ao semestre
     * @throws SQLException se ocorrer erro ao acessar o banco de dados
     */
    public List<Alocacao> getBySemestre(int idSemestre) throws SQLException {
        String sql = """
            SELECT a.* 
            FROM Alocacao a
            JOIN Semestre s ON a.id_semestre = s.id_semestre
            WHERE s.id_semestre = ?
            """;

        List<Alocacao> alocacoes = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSemestre);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Alocacao alocacao = mapearAlocacao(rs);
                    if (alocacao != null) {
                        alocacoes.add(alocacao);
                    }
                }
            }
        }
        return alocacoes;
    }

    /**
     * Verifica se um dado slot já está ocupado em um semestre.
     *
     * @param idSlot      identificador do slot
     * @param idSemestre  identificador do semestre
     * @return true se o slot estiver ocupado, false caso contrário
     * @throws SQLException se ocorrer erro ao acessar o banco de dados
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
}
