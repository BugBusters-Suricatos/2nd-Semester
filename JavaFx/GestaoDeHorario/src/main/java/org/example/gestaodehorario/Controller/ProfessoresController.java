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

/**
 * Controlador JavaFX responsável pelo gerenciamento de professores.
 * <p>
 * Permite criar, editar, remover e listar professores, associando-os a matérias,
 * além de interagir com a interface gráfica por meio de componentes FXML.
 * </p>
 */
public class ProfessoresController {

    /** Campo de texto para entrada do nome do professor. */
    @FXML private TextField nomeField;

    /** Campo de texto para entrada do e-mail do professor. */
    @FXML private TextField emailField;

    /** ComboBox para seleção da matéria associada ao professor. */
    @FXML private ComboBox<Materia> materiasComboBox;

    /** TableView que exibe a lista de professores. */
    @FXML private TableView<Professor> professoresTable;

    /** Coluna que exibe o nome na tabela de professores. */
    @FXML private TableColumn<Professor, String> nomeColumn;

    /** Coluna que exibe o e-mail na tabela de professores. */
    @FXML private TableColumn<Professor, String> emailColumn;

    /** Coluna que exibe a matéria associada na tabela de professores. */
    @FXML private TableColumn<Professor, String> materiaColumn;

    /** Botão para adicionar ou salvar edição de professor. */
    @FXML private Button btnAdicionar;

    /** Botão para iniciar edição do professor selecionado. */
    @FXML private Button btnEditar;

    /** Botão para remover o professor selecionado. */
    @FXML private Button btnRemover;

    /** DAO para operações de banco de dados relacionadas a professores. */
    private final ProfessorDAO professorDAO = new ProfessorDAO();

    /** DAO para operações de banco de dados relacionadas a matérias. */
    private final MateriaDAO materiaDAO = new MateriaDAO();

    /** Lista observável que alimenta a TableView de professores. */
    private final ObservableList<Professor> professoresList = FXCollections.observableArrayList();

    /** Referência ao professor atualmente em edição (null se for novo). */
    private Professor professorEmEdicao = null;

    /**
     * Inicializa o controlador após a injeção dos componentes FXML.
     * <p>
     * Configura colunas da tabela, inicializa ComboBox, carrega dados, ajusta interface
     * e limpa campos do formulário.
     * </p>
     */
    @FXML
    public void initialize() {
        configurarTabela();
        configurarComboBox();
        carregarDados();
        atualizarInterface();
        limparCampos();
    }

    /**
     * Configura as colunas da TableView e listener de seleção.
     */
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
                    atualizarInterface();
                }
        );
    }

    /**
     * Configura o ComboBox de matérias e carrega informações básicas do banco.
     */
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

    /**
     * Carrega todos os professores com suas matérias e atualiza a lista da TableView.
     */
    private void carregarDados() {
        try {
            professoresList.setAll(professorDAO.getAllComMaterias());
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar professores: " + e.getMessage());
        }
    }

    /**
     * Handler para clique no botão Adicionar/Salvar.
     * <p>
     * Valida campos, insere novo professor ou atualiza o existente, e recarrega dados.
     * </p>
     */
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

    /**
     * Handler para clique no botão Editar.
     * <p>
     * Prepara o formulário para edição do professor selecionado.
     * </p>
     */
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

    /**
     * Handler para clique no botão Remover.
     * <p>
     * Remove o professor selecionado do banco e atualiza a lista.
     * </p>
     */
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

    /**
     * Cria uma instância de {@link Professor} a partir dos dados do formulário.
     *
     * @return nova instância de Professor com dados preenchidos
     */
    private Professor criarProfessorFromForm() {
        return new Professor(
                nomeField.getText().trim(),
                emailField.getText().trim(),
                materiasComboBox.getValue()
        );
    }

    /**
     * Valida os campos do formulário antes das operações de inserção/edição.
     *
     * @return true se todos os campos estiverem válidos; false caso contrário
     */
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

    /**
     * Preenche o formulário com os dados do professor selecionado.
     *
     * @param professor instância de Professor selecionada na tabela
     */
    private void preencherCampos(Professor professor) {
        nomeField.setText(professor.getNome());
        emailField.setText(professor.getEmail());
        materiasComboBox.setValue(professor.getMateria());
    }

    /**
     * Seleciona o professor na lista e atualiza o formulário.
     *
     * @param professor instância de Professor selecionada
     */
    private void selecionarProfessor(Professor professor) {
        if (professor != null) {
            preencherCampos(professor);
        }
    }

    /**
     * Limpa o formulário e reseta o estado de edição.
     */
    @FXML
    private void limparCampos() {
        nomeField.clear();
        emailField.clear();
        materiasComboBox.getSelectionModel().clearSelection();
        professoresTable.getSelectionModel().clearSelection();
        professorEmEdicao = null;
        atualizarInterface();
    }

    /**
     * Atualiza textos e habilitação dos botões conforme o estado (edição ou adição).
     */
    private void atualizarInterface() {
        if (professorEmEdicao != null) {
            btnAdicionar.setText("Salvar Edição");
            btnEditar.setDisable(true);
            btnRemover.setDisable(false);
        } else {
            btnAdicionar.setText("Adicionar");
            btnEditar.setDisable(false);
            btnRemover.setDisable(professoresTable.getSelectionModel().isEmpty());
        }
    }

    /**
     * Exibe um alerta de erro com a mensagem especificada.
     *
     * @param mensagem texto de erro a ser exibido no diálogo
     */
    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Handler para o botão Voltar, retorna à tela principal.
     */
    @FXML
    private void btnVoltarClick() {
        ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
    }
}