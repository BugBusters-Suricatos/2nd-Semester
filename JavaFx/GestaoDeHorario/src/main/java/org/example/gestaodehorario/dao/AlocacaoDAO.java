package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Alocacao;
import org.example.gestaodehorario.model.MateriaProfessor;
import org.example.gestaodehorario.model.Semestre;
import org.example.gestaodehorario.model.Slot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlocacaoDAO {

    public void salvar(List<Alocacao> alocacoes) throws SQLException {
        String sql = "INSERT INTO Alocacao (id_materia_professor, id_slot, id_semestre) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Inicia transação

            try {
                for (Alocacao alocacao : alocacoes) {
                    stmt.setInt(1, alocacao.getMateriaProfessor().getIdMateriaProfessor());
                    stmt.setInt(2, alocacao.getSlot().getId_slot());
                    stmt.setInt(3, alocacao.getSemestre().getIdSemestre());
                    stmt.addBatch();

                    // Atualiza status do slot para "Ocupado"
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

    public void delete(int idAlocacao) throws SQLException {
        String sql = "DELETE FROM Alocacao WHERE id_alocacao = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAlocacao);
            stmt.executeUpdate();
        }
    }

    public Optional<Alocacao> getById(int id) throws SQLException {
        String sql = "SELECT * FROM Alocacao WHERE id_alocacao = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.ofNullable(mapearAlocacao(rs));
            }
            return Optional.empty();
        }
    }

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
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Alocacao alocacao = mapearAlocacao(rs);
                if (alocacao != null) {
                    alocacoes.add(alocacao);
                }
            }
        }
        return alocacoes;
    }

    public boolean slotEstaOcupado(int idSlot, int idSemestre) throws SQLException {
        String sql = "SELECT 1 FROM Alocacao WHERE id_slot = ? AND id_semestre = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSlot);
            stmt.setInt(2, idSemestre);

            return stmt.executeQuery().next();
        }
    }
}