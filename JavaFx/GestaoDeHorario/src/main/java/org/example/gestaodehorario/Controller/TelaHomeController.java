package org.example.gestaodehorario.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.gestaodehorario.ScreenManager;
import javafx.scene.control.Label;

public class TelaHomeController {

    // Botões de navegação
    @FXML
    private void btnCursosClick(ActionEvent event) {
        ScreenManager.changeScreen("view/GerenciamentoCursos-view.fxml", "styles/customGerenciamentoCursos.css");
    }

    @FXML
    private void btnMateriaClick(ActionEvent event) { // Nome do método alterado
        ScreenManager.changeScreen("view/GerenciamentoMateria-view.fxml", "styles/customGerenciamentoMateria.css");
    }

    @FXML
    private void btnProfessoresClick(ActionEvent event) {
        ScreenManager.changeScreen("view/GerenciamentoProfessores-view.fxml", "styles/customGerenciamentoProfessores.css");
    }

    @FXML
    private void btnHorariosClick(ActionEvent event) {
        ScreenManager.changeScreen("view/GerenciamentoHorario-view.fxml", "styles/customGerenciamentoHorario.css");
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