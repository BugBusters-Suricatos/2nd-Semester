package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Slot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SlotDAO {

    // Insere um novo slot (com id_periodo)
    public void insert(Slot slot) throws SQLException {
        String sql = "INSERT INTO Slot (dia_semana, hora_inicio, hora_fim, id_periodo, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, slot.getDia_semana());
            stmt.setString(2, slot.getHora_inicio());
            stmt.setString(3, slot.getHora_fim());
            stmt.setInt(4, slot.getId_periodo());
            stmt.setString(5, slot.getStatus());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    slot.setId_slot(rs.getInt(1));
                }
            }
        }
    }

    // Atualiza um slot existente
    public void update(Slot slot) throws SQLException {
        String sql = "UPDATE Slot SET dia_semana = ?, hora_inicio = ?, hora_fim = ?, id_periodo = ?, status = ? WHERE id_slot = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, slot.getDia_semana());
            stmt.setString(2, slot.getHora_inicio());
            stmt.setString(3, slot.getHora_fim());
            stmt.setInt(4, slot.getId_periodo());
            stmt.setString(5, slot.getStatus());
            stmt.setInt(6, slot.getId_slot());

            stmt.executeUpdate();
        }
    }

    // Remove um slot pelo ID
    public void delete(int idSlot) throws SQLException {
        String sql = "DELETE FROM Slot WHERE id_slot = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSlot);
            stmt.executeUpdate();
        }
    }

    // Busca um slot pelo ID
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

    // Lista todos os slots
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

    public Optional<Slot> getByHorarioDia(String horaInicio, String dia, int idPeriodo) throws SQLException {
        String sql = "SELECT * FROM Slot WHERE hora_inicio = ? AND dia_semana = ? AND id_periodo = ?";
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
                        rs.getString("hora_inicio"),
                        rs.getString("hora_fim"),
                        rs.getString("status"),
                        rs.getInt("id_periodo")
                ));
            }
            return Optional.empty(); // Retorna Optional vazio se não encontrar
        }
    }

    // Busca slots vinculados a um curso e semestre (com filtro de período)
    public List<Slot> getByCursoSemestre(int idCurso, int idSemestre) throws SQLException {
        String sql = """
            SELECT s.* 
            FROM Slot s
            JOIN Curso_Slot cs ON s.id_slot = cs.id_slot
            WHERE cs.id_curso = ? 
            AND cs.id_semestre = ?
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
            stmt.setInt(2, idSemestre);

            return processarResultado(stmt.executeQuery());
        }
    }

    // Busca a hora_fim com base na hora_inicio (considera id_periodo para evitar ambiguidade)
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

    // Atualiza o status de um slot específico (considera período para evitar ambiguidade)
    public void atualizarStatus(String dia, String horaInicio, int idPeriodo, String status) throws SQLException {
        String sql = "UPDATE Slot SET status = ? WHERE dia_semana = ? AND hora_inicio = ? AND id_periodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, dia);
            stmt.setString(3, horaInicio);
            stmt.setInt(4, idPeriodo);
            stmt.executeUpdate();
        }
    }

    // Processa resultados das queries (com id_periodo)
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

    public List<Slot> getByPeriodo(int idPeriodo) throws SQLException {
        String sql = "SELECT * FROM Slot WHERE id_periodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPeriodo);
            return processarResultado(stmt.executeQuery());
        }
    }
}