package org.example.gestaodehorario.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.gestaodehorario.ScreenManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Label;

public class TelaHomeController {

    // Botões de navegação
    @FXML
    private void btnCursosClick(ActionEvent event) {
        ScreenManager.changeScreen("GerenciamentoCursos-view.fxml");
    }

    @FXML
    private void btnMateriaClick(ActionEvent event) { // Nome do método alterado
        ScreenManager.changeScreen("GerenciamentoMateria-view.fxml");
    }

    @FXML
    private void btnProfessoresClick(ActionEvent event) {
        ScreenManager.changeScreen("GerenciamentoProfessores-view.fxml");
    }

    @FXML
    private void btnHorariosClick(ActionEvent event) {
        ScreenManager.changeScreen("GerenciamentoHorario-view.fxml");
    }

    // Labels
    @FXML
    private Label lblTotalCursos;
    @FXML
    private Label lblTotalMateria;
    @FXML
    private Label lblTotalProfessores;

    // Carregamento inicial
    @FXML
    public void initialize() {
        carregarDadosDoBanco();
    }

    // Lógica de banco de dados
    private void carregarDadosDoBanco() {
//        try (Connection connection = H2DatabaseUtil.getConnection()) {
//
//            // Total de cursos
//            String sqlCursos = "SELECT COUNT(*) AS totalCursos FROM Curso";
//            try (PreparedStatement stmt = connection.prepareStatement(sqlCursos);
//                 ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    lblTotalCursos.setText("Total de Cursos: " + rs.getInt("totalCursos"));
//                }
//            }
//
//            // Total de matérias (refatorado)
//            String sqlMateria = "SELECT COUNT(*) AS totalMateria FROM Materia";
//            try (PreparedStatement stmt = connection.prepareStatement(sqlMateria);
//                 ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    lblTotalMateria.setText("Total de Matérias: " + rs.getInt("totalMateria"));
//                }
//            }
//
//            // Total de professores
//            String sqlProfessores = "SELECT COUNT(*) AS totalProfessores FROM Professor";
//            try (PreparedStatement stmt = connection.prepareStatement(sqlProfessores);
//                 ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    lblTotalProfessores.setText("Total de Professores: " + rs.getInt("totalProfessores"));
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}