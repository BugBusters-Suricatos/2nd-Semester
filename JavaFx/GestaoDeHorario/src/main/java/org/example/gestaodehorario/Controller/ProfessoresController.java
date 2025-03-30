package org.example.gestaodehorario.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.dao.MateriaDAO;
import org.example.gestaodehorario.dao.ProfessorDAO;
import org.example.gestaodehorario.model.Materia;
import org.example.gestaodehorario.model.Professor;

import java.sql.SQLException;

public class ProfessoresController {

    // Componentes FXML
    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private ComboBox<Materia> materiasComboBox;
    @FXML private TableView<Professor> professoresTable;
    @FXML private TableColumn<Professor, String> nomeColumn;
    @FXML private TableColumn<Professor, String> emailColumn;
    @FXML private TableColumn<Professor, String> materiaColumn;

    @FXML private Button btnAdicionar;
    @FXML private Button btnEditar;
    @FXML private Button btnRemover;

    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private final MateriaDAO materiaDAO = new MateriaDAO();
    private final ObservableList<Professor> professoresList = FXCollections.observableArrayList();
    private Professor professorEmEdicao = null;

    @FXML
    public void initialize() {
        configurarTabela();
        configurarComboBox();
        carregarDados();
        atualizarInterface();
        limparCampos();
    }

    private void configurarTabela() {
        nomeColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        materiaColumn.setCellValueFactory(cellData ->
                cellData.getValue().getMateria().nomeProperty()
        );

        professoresTable.setItems(professoresList);
        professoresTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    selecionarProfessor(newVal);
                    atualizarInterface(); // Atualiza os botões quando a seleção muda
                }
        );
    }

    private void configurarComboBox() {
        materiasComboBox.setConverter(new StringConverter<Materia>() {
            @Override
            public String toString(Materia materia) {
                return materia != null ? materia.getNome() : "";
            }

            @Override
            public Materia fromString(String string) {
                return null;
            }
        });

        try {
            materiasComboBox.getItems().setAll(materiaDAO.getBasicInfo());
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar matérias: " + e.getMessage());
        }
    }

    private void carregarDados() {
        try {
            professoresList.setAll(professorDAO.getAllComMaterias());
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar professores: " + e.getMessage());
        }
    }

    @FXML
    private void btnAdicionarClick() {
        if (validarCampos()) {
            Professor novoProfessor = criarProfessorFromForm();

            try {
                if (professorEmEdicao == null) {
                    professorDAO.insert(novoProfessor);
                } else {
                    novoProfessor.setId(professorEmEdicao.getId());
                    professorDAO.update(novoProfessor);
                }
                carregarDados();
                limparCampos();
            } catch (SQLException e) {
                mostrarAlerta("Erro ao salvar professor: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnEditarClick() {
        Professor selecionado = professoresTable.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            professorEmEdicao = selecionado;
            preencherCampos(selecionado);
            atualizarInterface();
        } else {
            mostrarAlerta("Selecione um professor para editar!");
        }
    }

    @FXML
    private void btnRemoverClick() {
        Professor selecionado = professoresTable.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                professorDAO.delete(selecionado.getId());
                carregarDados();
                limparCampos();
            } catch (SQLException e) {
                mostrarAlerta("Erro ao remover professor: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Selecione um professor para remover!");
        }
    }

    private Professor criarProfessorFromForm() {
        return new Professor(
                nomeField.getText().trim(),
                emailField.getText().trim(),
                materiasComboBox.getValue()
        );
    }

    private boolean validarCampos() {
        if (nomeField.getText().isBlank() ||
                emailField.getText().isBlank() ||
                materiasComboBox.getValue() == null) {

            mostrarAlerta("Preencha todos os campos!");
            return false;
        }

        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            mostrarAlerta("Formato de e-mail inválido!");
            return false;
        }

        return true;
    }

    private void selecionarProfessor(Professor professor) {
        if (professor != null) {
            nomeField.setText(professor.getNome());
            emailField.setText(professor.getEmail());
            materiasComboBox.setValue(professor.getMateria());
        }
    }

    private void preencherCampos(Professor professor) {
        nomeField.setText(professor.getNome());
        emailField.setText(professor.getEmail());
        materiasComboBox.setValue(professor.getMateria());
    }

    private void limparCampos() {
        professorEmEdicao = null;
        nomeField.clear();
        emailField.clear();
        materiasComboBox.getSelectionModel().clearSelection();
        professoresTable.getSelectionModel().clearSelection();
        atualizarInterface();
    }

    private void atualizarInterface() {
        if (professorEmEdicao != null) {
            btnAdicionar.setText("Salvar Edição");
            btnEditar.setDisable(true); // Desabilita o botão Editar durante a edição
            btnRemover.setDisable(false);
        } else {
            btnAdicionar.setText("Adicionar");
            btnEditar.setDisable(false);
            btnRemover.setDisable(professoresTable.getSelectionModel().isEmpty());
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void btnVoltarClick() {
        ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
    }
}