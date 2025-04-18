package org.example.gestaodehorario.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.gestaodehorario.ScreenManager;
import javafx.scene.control.Label;

/**
 * Controlador JavaFX da tela inicial (home) do sistema de gestão de horários.
 * <p>
 * Fornece navegação para as diferentes funcionalidades (cursos, matérias,
 * professores, horários de indisponibilidade e montagem de grade) e exibe
 * estatísticas resumidas do sistema.
 * </p>
 */
public class TelaHomeController {

    /** Label que exibe o total de cursos cadastrados. */
    @FXML
    private Label lblTotalCursos;

    /** Label que exibe o total de matérias cadastradas. */
    @FXML
    private Label lblTotalMateria;

    /** Label que exibe o total de professores cadastrados. */
    @FXML
    private Label lblTotalProfessores;

    /**
     * Método de inicialização do controlador, chamado após a injeção dos componentes FXML.
     * <p>
     * Chama o método para carregar dados do banco e atualizar as labels.
     * </p>
     */
    @FXML
    public void initialize() {
        carregarDadosDoBanco();
    }

    /**
     * Carrega as contagens de cursos, matérias e professores do banco de dados
     * e atualiza as respectivas labels na interface.
     */
    private void carregarDadosDoBanco() {
        // Implementar lógica de acesso ao banco e atualização dos labels
    }

    /**
     * Navega para a tela de gerenciamento de cursos.
     *
     * @param event evento de ação disparado pelo clique no botão de cursos
     */
    @FXML
    private void btnCursosClick(ActionEvent event) {
        ScreenManager.changeScreen("view/GerenciamentoCursos-view.fxml", "styles/customGerenciamentoCursos.css");
    }

    /**
     * Navega para a tela de gerenciamento de matérias.
     *
     * @param event evento de ação disparado pelo clique no botão de matérias
     */
    @FXML
    private void btnMateriaClick(ActionEvent event) {
        ScreenManager.changeScreen("view/GerenciamentoMateria-view.fxml", "styles/customGerenciamentoMateria.css");
    }

    /**
     * Navega para a tela de gerenciamento de professores.
     *
     * @param event evento de ação disparado pelo clique no botão de professores
     */
    @FXML
    private void btnProfessoresClick(ActionEvent event) {
        ScreenManager.changeScreen("view/GerenciamentoProfessores-view.fxml", "styles/customGerenciamentoProfessores.css");
    }

    /**
     * Navega para a tela de gerenciamento de indisponibilidades de horário.
     *
     * @param event evento de ação disparado pelo clique no botão de horários
     */
    @FXML
    private void btnHorariosClick(ActionEvent event) {
        ScreenManager.changeScreen("view/GerenciamentoIndisponibilidade-view.fxml", "styles/customGerenciamentoIndisponibilidade.css");
    }

    /**
     * Navega para a tela de montagem de grade de alocação.
     *
     * @param event evento de ação disparado pelo clique no botão de montagem de grade
     */
    @FXML
    private void btnMontagemClick(ActionEvent event) {
        ScreenManager.changeScreen("view/GerenciamentoMontagem-view.fxml", "styles/customGerenciamentoMontagem.css");
    }
}
