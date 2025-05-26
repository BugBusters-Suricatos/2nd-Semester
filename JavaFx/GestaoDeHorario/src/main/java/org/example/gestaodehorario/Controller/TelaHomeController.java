package org.example.gestaodehorario.Controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.example.gestaodehorario.dao.AlocacaoDAO;
import org.example.gestaodehorario.dao.CursoDAO;
import org.example.gestaodehorario.dao.MateriaDAO;
import org.example.gestaodehorario.dao.ProfessorDAO;
import org.example.gestaodehorario.util.AlertHelper;

import java.io.IOException;

public class TelaHomeController {

    @FXML private StackPane contentArea;

    private final CursoDAO cursoDAO = new CursoDAO();
    private final MateriaDAO materiaDAO = new MateriaDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private final AlocacaoDAO alocacaoDAO = new AlocacaoDAO();

    /**
     * Inicialização do controller
     */
    @FXML
    public void initialize() {
        loadView("dashboard.fxml");
    }

    // ==== Ações de botões ====

    @FXML
    private void btnDashboardClick() {
        loadView("dashboard.fxml");
    }

    @FXML
    private void btnCursosClick() {
        loadView("GerenciamentoCursos-view.fxml");
    }

    @FXML
    private void btnMateriaClick() {
        loadView("GerenciamentoMateria-view.fxml");
    }

    @FXML
    private void btnProfessoresClick() {
        loadView("GerenciamentoProfessores-view.fxml");
    }

    @FXML
    private void btnHorariosClick() {
        loadView("GerenciamentoIndisponibilidade-view.fxml");
    }

    @FXML
    private void btnMontagemClick() {
        loadView("GerenciamentoMontagem-view.fxml");
    }

    @FXML
    private void btnRelatorioClick() {
        loadView("RelatorioGrade-view.fxml");
    }

    // ==== Carregamento dinâmico de views no StackPane ====

    private void loadView(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/view/" + fxmlFile));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            AlertHelper.showError("Erro ao carregar a tela: " + e.getMessage());
        }
    }
}