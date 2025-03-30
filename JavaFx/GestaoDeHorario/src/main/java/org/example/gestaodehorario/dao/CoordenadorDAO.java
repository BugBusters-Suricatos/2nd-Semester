package org.example.gestaodehorario.dao;

import org.example.gestaodehorario.connect.DatabaseManager;
import org.example.gestaodehorario.model.Coordenador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoordenadorDAO {
    public void insert(Coordenador coordenador) throws SQLException {
        String sql = "INSERT INTO Coordenador (nome, email, senha) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, coordenador.getNome());
            stmt.setString(2, coordenador.getEmail());
            stmt.setString(3, coordenador.getSenha());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    coordenador.setIdCoordenador(rs.getInt(1));
                }
            }
        }
    }

    public void update(Coordenador coordenador) throws SQLException {
        String sql = "UPDATE Coordenador SET nome = ?, email = ?, senha = ? WHERE id_coordenador = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, coordenador.getNome());
            stmt.setString(2, coordenador.getEmail());
            stmt.setString(3, coordenador.getSenha());
            stmt.setInt(4, coordenador.getIdCoordenador());
            stmt.executeUpdate();
        }
    }

    public void delete(int idCoordenador) throws SQLException {
        String sql = "DELETE FROM Coordenador WHERE id_coordenador = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCoordenador);
            stmt.executeUpdate();
        }
    }

    public Optional<Coordenador> getById(int id) throws SQLException {
        String sql = "SELECT * FROM Coordenador WHERE id_coordenador = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Coordenador(
                            rs.getInt("id_coordenador"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha")
                    ));
                }
                return Optional.empty();
            }
        }
    }

    public boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Coordenador WHERE email = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public List<Coordenador> getAll() throws SQLException {
        List<Coordenador> coordenadores = new ArrayList<>();
        String sql = "SELECT * FROM Coordenador";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                coordenadores.add(new Coordenador(
                        rs.getInt("id_coordenador"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha")
                ));
            }
        }
        return coordenadores;
    }
}