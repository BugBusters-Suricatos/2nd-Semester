package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Indisponibilidade;

import java.sql.*;
import java.util.*;

public class IndisponibilidadeDAO {

    public List<Integer> getSlotIdsPorProfessor(int idProfessor) {
        List<Integer> slots = new ArrayList<>();
        String sql = "SELECT id_slot FROM Indisponibilidade WHERE id_professor = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProfessor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    slots.add(rs.getInt("id_slot"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    public void salvarSlotsIndisponiveis(int idProfessor, List<Integer> slotsIndisponiveis) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String deleteSql = "DELETE FROM Indisponibilidade WHERE id_professor = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
                stmt.setInt(1, idProfessor);
                stmt.executeUpdate();
            }

            if (!slotsIndisponiveis.isEmpty()) {
                String insertSql = "INSERT INTO Indisponibilidade (id_professor, id_slot) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    for (int idSlot : slotsIndisponiveis) {
                        stmt.setInt(1, idProfessor);
                        stmt.setInt(2, idSlot);
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // ADICIONE ESTE MÉTODO NO FINAL DA CLASSE, NÃO APAGUE OS OUTROS!
    public List<Indisponibilidade> getAll() {
        List<Indisponibilidade> indisponibilidades = new ArrayList<>();
        String sql = "SELECT id, id_professor, id_slot FROM Indisponibilidade";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Usa o construtor com (id, id_professor, id_slot)
                Indisponibilidade ind = new Indisponibilidade(
                        rs.getInt("id"),
                        rs.getInt("id_professor"),
                        rs.getInt("id_slot")
                );
                indisponibilidades.add(ind);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return indisponibilidades;
    }
}
