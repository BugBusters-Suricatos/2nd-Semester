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

    /**
     * Insere um novo slot no banco de dados
     * @param slot Objeto Slot a ser persistido (o ID será gerado automaticamente)
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Atualiza os dados de um slot existente
     * @param slot Objeto Slot com os novos dados (deve conter ID válido)
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Remove permanentemente um slot do sistema
     * @param idSlot ID do slot a ser excluído
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
    public void delete(int idSlot) throws SQLException {
        String sql = "DELETE FROM Slot WHERE id_slot = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSlot);
            stmt.executeUpdate();
        }
    }

    /**
     * Busca um slot pelo seu ID único
     * @param id ID do slot a ser recuperado
     * @return Optional contendo o slot encontrado ou vazio se não existir
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Lista todos os slots cadastrados no sistema
     * @return Lista completa de slots (pode ser vazia se não houver registros)
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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
     * Busca um slot específico por horário, dia e período
     * @param horaInicio Hora de início no formato HH:MM
     * @param dia Dia da semana (ex: 'Segunda', 'Terça')
     * @param idPeriodo ID do período acadêmico
     * @return Optional contendo o slot encontrado ou vazio se não existir
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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
            return Optional.empty();
        }
    }

    /**
     * Recupera slots vinculados a um curso e semestre específicos
     * @param idCurso ID do curso
     * @param idSemestre ID do semestre
     * @return Lista de slots ordenados por dia da semana e hora de início
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Busca a hora de término correspondente a uma hora de início e período
     * @param horaInicio Hora de início no formato HH:MM
     * @param idPeriodo ID do período acadêmico
     * @return Hora de término ou null se não encontrado
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Atualiza o status de disponibilidade de um slot
     * @param dia Dia da semana (ex: 'Segunda', 'Terça')
     * @param horaInicio Hora de início no formato HH:MM
     * @param idPeriodo ID do período acadêmico
     * @param status Novo status (ex: 'disponível', 'ocupado')
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
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

    /**
     * Processa um ResultSet convertendo os resultados em objetos Slot
     * @param rs ResultSet contendo dados do banco
     * @return Lista de slots convertidos
     * @throws SQLException Se ocorrer um erro ao acessar os dados
     */
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

    /**
     * Lista todos os slots de um período específico
     * @param idPeriodo ID do período acadêmico
     * @return Lista de slots associados ao período
     * @throws SQLException Se ocorrer um erro de acesso ao banco de dados
     */
    public List<Slot> getByPeriodo(int idPeriodo) throws SQLException {
        String sql = "SELECT * FROM Slot WHERE id_periodo = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPeriodo);
            return processarResultado(stmt.executeQuery());
        }
    }
}