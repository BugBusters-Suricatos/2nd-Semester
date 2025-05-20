package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Indisponibilidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IndisponibilidadeDAO {

    // Salva uma indisponibilidade individual
    public void salvarIndisponibilidade(Indisponibilidade ind) throws SQLException {
        String sql = "INSERT INTO Indisponibilidade (id_professor, dia_semana, hora_inicio, hora_fim) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ind.getId_professor());
            stmt.setString(2, ind.getDia_semana());
            stmt.setString(3, ind.getHora_inicio());
            stmt.setString(4, ind.getHora_fim());
            stmt.executeUpdate();
        }
    }

    // Salva uma lista de indisponibilidades
    public void salvarIndisponibilidades(List<Indisponibilidade> lista) throws SQLException {
        for (Indisponibilidade ind : lista) {
            salvarIndisponibilidade(ind);
        }
    }

    // Retorna todas as indisponibilidades
    public List<Indisponibilidade> getAll() throws SQLException {
        List<Indisponibilidade> lista = new ArrayList<>();
        String sql = "SELECT * FROM indisponibilidade";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Indisponibilidade ind = new Indisponibilidade(
                        rs.getInt("id"),
                        rs.getInt("id_professor"),
                        rs.getString("dia_semana"),
                        rs.getString("hora_inicio"),
                        rs.getString("hora_fim"),
                        null, // id_curso (não está mais usando)
                        null  // id_semestre (não está mais usando)
                );
                lista.add(ind);
            }
        }
        return lista;
    }
}
