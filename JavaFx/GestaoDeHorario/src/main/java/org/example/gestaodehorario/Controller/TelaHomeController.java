package org.example.gestaodehorario.Controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.dao.CursoDAO;
import org.example.gestaodehorario.dao.MateriaDAO;
import org.example.gestaodehorario.dao.MateriaProfessorDAO;
import org.example.gestaodehorario.dao.ProfessorDAO;
import org.example.gestaodehorario.util.AlertHelper;

/**
 * Controlador da tela inicial com dashboard de estatísticas
 */
public class TelaHomeController {

    // Componentes FXML
    @FXML private BorderPane rootPane;
    @FXML private Label lblTotalCursos;
    @FXML private Label lblTotalMateria;
    @FXML private Label lblTotalProfessores;
    @FXML private Label lblTotalAssociacoes;

    // DAOs
    private final CursoDAO cursoDAO = new CursoDAO();
    private final MateriaDAO materiaDAO = new MateriaDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private final MateriaProfessorDAO materiaProfessorDAO = new MateriaProfessorDAO();

    /**
     * Inicialização do controller com animações e carregamento de dados
     */
    @FXML
    public void initialize() {
        configurarAnimacoes();
        carregarDadosDoBanco();
    }

    /**
     * Configura animações de entrada
     */
    private void configurarAnimacoes() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), rootPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    /**
     * Carrega e atualiza as estatísticas em tempo real
     */
    private void carregarDadosDoBanco() {
        try {
            lblTotalCursos.setText(String.valueOf(cursoDAO.getTotalCursos()));
            lblTotalMateria.setText(String.valueOf(materiaDAO.getTotalMaterias()));
            lblTotalProfessores.setText(String.valueOf(professorDAO.getTotalProfessores()));
            lblTotalAssociacoes.setText(String.valueOf(materiaProfessorDAO.getTotalAssociacoes()));
        } catch (Exception e) {
            AlertHelper.showError("Erro ao atualizar dashboard: " + e.getMessage());
            definirValoresPadrao();
        }
    }

    /**
     * Define valores padrão para as estatísticas
     */
    private void definirValoresPadrao() {
        lblTotalCursos.setText("--");
        lblTotalMateria.setText("--");
        lblTotalProfessores.setText("--");
        lblTotalAssociacoes.setText("--");
    }

    // Métodos de navegação (mantidos)
    @FXML
    private void btnCursosClick(ActionEvent event) {
        ScreenManager.changeScreen("view/GerenciamentoCursos-view.fxml", "styles/customGerenciamentoCursos.css");
    }

    @FXML
    private void btnMateriaClick(ActionEvent event) {
        ScreenManager.changeScreen("view/GerenciamentoMateria-view.fxml", "styles/customGerenciamentoMateria.css");
    }

    @FXML
    private void btnProfessoresClick(ActionEvent event) {
        ScreenManager.changeScreen("view/GerenciamentoProfessores-view.fxml", "styles/customGerenciamentoProfessores.css");
    }

    @FXML
    private void btnHorariosClick(ActionEvent event) {
        ScreenManager.changeScreen("view/GerenciamentoIndisponibilidade-view.fxml", "styles/customGerenciamentoIndisponibilidade.css");
    }

    @FXML
    private void btnMontagemClick(ActionEvent event) {
        ScreenManager.changeScreen("view/GerenciamentoMontagem-view.fxml", "styles/customGerenciamentoMontagem.css");
    }
}