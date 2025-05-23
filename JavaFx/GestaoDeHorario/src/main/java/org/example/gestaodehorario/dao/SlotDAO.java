package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Slot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) para operações com entidades Slot no banco de dados.
 * Gerencia horários específicos vinculados a períodos acadêmicos, incluindo
 * disponibilidade e status de ocupação.
 */
public class SlotDAO {

    public void insert(Slot slot) throws SQLException {
        String sql = "INSERT INTO Slot (dia_semana, hora_inicio, hora_fim, id_periodo, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, slot.getDia_semana());
            stmt.setString(2, slot.getHora_inicio());
            stmt.setString(3, slot.getHora_fim());
            stmt.setInt(4, slot.getIdPeriodo());
            stmt.setString(5, slot.getStatus());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    slot.setId_slot(rs.getInt(1));
                }
            }
        }
    }

    public void update(Slot slot) throws SQLException {
        String sql = "UPDATE Slot SET dia_semana = ?, hora_inicio = ?, hora_fim = ?, id_periodo = ?, status = ? WHERE id_slot = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, slot.getDia_semana());
            stmt.setString(2, slot.getHora_inicio());
            stmt.setString(3, slot.getHora_fim());
            stmt.setInt(4, slot.getIdPeriodo());
            stmt.setString(5, slot.getStatus());
            stmt.setInt(6, slot.getId_slot());
            stmt.executeUpdate();
        }
    }

    public void delete(int idSlot) throws SQLException {
        String sql = "DELETE FROM Slot WHERE id_slot = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSlot);
            stmt.executeUpdate();
        }
    }

    public Optional<Slot> getById(int id) throws SQLException {
        String sql = "SELECT * FROM Slot WHERE id_slot = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Slot(
                        rs.getInt("id_slot"),
                        rs.getString("dia_semana"),
                        rs.getString("hora_inicio"),
                        rs.getString("hora_fim"),
                        rs.getString("status"),
                        rs.getInt("id_periodo")
                ));
            }
            return Optional.empty();
        }
    }

    public List<Slot> getAll() throws SQLException {
        List<Slot> slots = new ArrayList<>();
        String sql = "SELECT * FROM Slot";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                slots.add(new Slot(
                        rs.getInt("id_slot"),
                        rs.getString("dia_semana"),
                        rs.getString("hora_inicio"),
                        rs.getString("hora_fim"),
                        rs.getString("status"),
                        rs.getInt("id_periodo")
                ));
            }
        }
        return slots;
    }

    /**
     * Recupera slots vinculados a um curso (sem diferenciar semestres).
     * Filtragem por período deve ser feita no controller.
     * @param idCurso ID do curso
     * @return Lista de slots ordenados por dia da semana e hora de início
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
    public List<Slot> getByCurso(int idCurso) throws SQLException {
        String sql = """
            SELECT s.*
            FROM Slot s
            JOIN Curso_Slot cs ON s.id_slot = cs.id_slot
            WHERE cs.id_curso = ?
            ORDER BY 
                CASE dia_semana
                    WHEN 'Segunda' THEN 1
                    WHEN 'Terça' THEN 2
                    WHEN 'Quarta' THEN 3
                    WHEN 'Quinta' THEN 4
                    WHEN 'Sexta' THEN 5
                    WHEN 'Sábado' THEN 6
                END,
                hora_inicio
            """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCurso);
            return processarResultado(stmt.executeQuery());
        }
    }

    private List<Slot> processarResultado(ResultSet rs) throws SQLException {
        List<Slot> slots = new ArrayList<>();
        while (rs.next()) {
            slots.add(new Slot(
                    rs.getInt("id_slot"),
                    rs.getString("dia_semana"),
                    rs.getString("hora_inicio"),
                    rs.getString("hora_fim"),
                    rs.getString("status"),
                    rs.getInt("id_periodo")
            ));
        }
        return slots;
    }

    public Optional<Slot> getByHorarioDia(String horaInicio, String dia, int idPeriodo) throws SQLException {
        String sql = "SELECT hora_fim, dia_semana, status, id_slot FROM Slot WHERE hora_inicio = ? AND dia_semana = ? AND id_periodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, horaInicio);
            stmt.setString(2, dia);
            stmt.setInt(3, idPeriodo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Slot(
                        rs.getInt("id_slot"),
                        rs.getString("dia_semana"),
                        horaInicio,
                        rs.getString("hora_fim"),
                        rs.getString("status"),
                        idPeriodo
                ));
            }
            return Optional.empty();
        }
    }

    public String findHoraFimByHoraInicio(String horaInicio, int idPeriodo) throws SQLException {
        String sql = "SELECT hora_fim FROM Slot WHERE hora_inicio = ? AND id_periodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, horaInicio);
            stmt.setInt(2, idPeriodo);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("hora_fim") : null;
        }
    }

    public List<Slot> getByPeriodo(int idPeriodo) throws SQLException {
        String sql = "SELECT * FROM Slot WHERE id_periodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPeriodo);
            return processarResultado(stmt.executeQuery());
        }
    }
}
