package org.example.gestaodehorario.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.dao.CursoDAO;
import org.example.gestaodehorario.dao.MateriaDAO;
import org.example.gestaodehorario.model.Curso;
import org.example.gestaodehorario.model.Materia;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Controlador JavaFX responsável pelo gerenciamento de matérias.
 * <p>
 * Provê funcionalidades para criar, editar, remover e listar matérias associadas a cursos,
 * além de interagir com a interface gráfica por meio de componentes FXML.
 * </p>
 */
public class MateriaController {

    /** Campo de texto para entrada do nome da matéria. */
    @FXML private TextField txtNomeMateria;

    /** ComboBox para seleção do curso ao qual a matéria pertence. */
    @FXML private ComboBox<Curso> cbCurso;

    /** Campo de texto para entrada da carga horária da matéria. */
    @FXML private TextField txtCargaHoraria;

    /** TableView que exibe a lista de matérias. */
    @FXML private TableView<Materia> tableMateria;

    /** Coluna que exibe o nome da matéria na tabela. */
    @FXML private TableColumn<Materia, String> colNome;

    /** Coluna que exibe o nome do curso associado à matéria. */
    @FXML private TableColumn<Materia, String> colCurso;

    /** Coluna que exibe a carga horária da matéria. */
    @FXML private TableColumn<Materia, Integer> colCargaHoraria;

    /** Botão para adicionar ou salvar edição de matéria. */
    @FXML private Button btnAdicionar;

    /** Botão para iniciar edição da matéria selecionada. */
    @FXML private Button btnEditar;

    /** DAO para operações de banco de dados relacionadas a matérias. */
    private final MateriaDAO materiaDAO = new MateriaDAO();

    /** DAO para operações de banco de dados relacionadas a cursos. */
    private final CursoDAO cursoDAO = new CursoDAO();

    /** Lista observável que alimenta a TableView de matérias. */
    private final ObservableList<Materia> materiasList = FXCollections.observableArrayList();

    /** Referência à matéria atualmente em modo de edição (null se for novo registro). */
    private Materia materiaEmEdicao = null;

    /**
     * Método inicial chamado após injeção dos componentes FXML.
     * <p>
     * Configura colunas da tabela, inicializa ComboBox, carrega dados e ajusta a interface.
     * </p>
     */
    @FXML
    public void initialize() {
        configurarTabela();
        configurarComboBox();
        carregarDados();
        atualizarInterfaceParaEdicao();
    }

    /**
     * Configura as colunas da TableView e listeners de seleção.
     */
    private void configurarTabela() {
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colCurso.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCurso().getNome())
        );
        colCargaHoraria.setCellValueFactory(cellData -> cellData.getValue().cargaHorariaProperty().asObject());

        tableMateria.setItems(materiasList);
        tableMateria.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> selecionarMateria(newVal)
        );
        tableMateria.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> preencherCampos(newVal)
        );
    }

    /**
     * Atualiza textos e habilitação de botões conforme modo de edição ou adição.
     */
    private void atualizarInterfaceParaEdicao() {
        if (materiaEmEdicao != null) {
            btnAdicionar.setText("Salvar Edição");
            btnEditar.setDisable(true);
        } else {
            btnAdicionar.setText("Adicionar");
            btnEditar.setDisable(false);
        }
    }

    /**
     * Configura o ComboBox de cursos e carrega os cursos do banco.
     */
    private void configurarComboBox() {
        cbCurso.setConverter(new StringConverter<Curso>() {
            @Override
            public String toString(Curso curso) {
                return curso != null ? curso.getNome() : "";
            }

            @Override
            public Curso fromString(String string) {
                return null;
            }
        });

        try {
            cbCurso.getItems().setAll(cursoDAO.getAll());
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar cursos: " + e.getMessage());
        }
    }

    /**
     * Carrega todas as matérias do banco e atualiza a lista da TableView.
     */
    private void carregarDados() {
        try {
            materiasList.setAll(materiaDAO.getAll());
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar matérias: " + e.getMessage());
        }
    }

    /**
     * Inicia o modo de edição para a matéria com o ID especificado.
     *
     * @param idMateria identificador da matéria a ser editada
     */
    public void editarMateria(int idMateria) {
        try {
            Optional<Materia> materia = materiaDAO.getById(idMateria);
            materia.ifPresentOrElse(
                    m -> preencherCampos(m),
                    () -> mostrarAlerta("Matéria não encontrada!")
            );
        } catch (SQLException e) {
            mostrarAlerta("Erro de banco de dados: " + e.getMessage());
        }
    }

    /**
     * Preenche os campos do formulário com os dados da matéria informada.
     *
     * @param materia instância de {@link Materia} cujo conteúdo será exibido
     */
    private void preencherCampos(Materia materia) {
        if (materia != null) {
            txtNomeMateria.setText(materia.getNome());
            txtCargaHoraria.setText(String.valueOf(materia.getCargaHoraria()));
            selecionarCursoNoComboBox(materia.getCurso());
        } else {
            limparCampos();
        }
    }

    /**
     * Seleciona o curso correspondente no ComboBox.
     *
     * @param curso instância de {@link Curso} a ser selecionada
     */
    private void selecionarCursoNoComboBox(Curso curso) {
        if (curso != null) {
            cbCurso.getItems().stream()
                    .filter(c -> c.getIdCurso() == curso.getIdCurso())
                    .findFirst()
                    .ifPresent(c -> cbCurso.getSelectionModel().select(c));
        } else {
            cbCurso.getSelectionModel().clearSelection();
        }
    }

    /**
     * Handler para o clique do botão Adicionar/Salvar Edição.
     * <p>
     * Verifica campos, insere ou atualiza matéria no banco.
     * </p>
     */
    @FXML
    private void btnAdicionarClick() {
        if (validarCampos()) {
            Materia materia = criarMateriaFromForm();

            try {
                if (materiaEmEdicao == null) {
                    if (materiaDAO.existeMateria(materia.getNome(), materia.getCurso().getIdCurso())) {
                        mostrarAlerta("Já existe uma matéria com este nome para o curso selecionado!");
                        return;
                    }
                    materiaDAO.insert(materia);
                } else {
                    materia.setIdMateria(materiaEmEdicao.getIdMateria());
                    materiaDAO.update(materia);
                }

                carregarDados();
                limparCampos();

            } catch (SQLException e) {
                mostrarAlerta("Erro ao salvar matéria: " + e.getMessage());
            }
        }
    }

    /**
     * Handler para o clique do botão Editar.
     * <p>
     * Prepara a interface para edição da matéria selecionada.
     * </p>
     */
    @FXML
    private void btnEditarClick() {
        Materia selecionada = tableMateria.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            materiaEmEdicao = selecionada;
            preencherCampos(selecionada);
            atualizarInterfaceParaEdicao();
        } else {
            mostrarAlerta("Selecione uma matéria para editar!");
        }
    }

    /**
     * Handler para o clique do botão Remover.
     * <p>
     * Exclui a matéria selecionada do banco de dados.
     * </p>
     */
    @FXML
    private void btnRemoverClick() {
        Materia materiaSelecionada = tableMateria.getSelectionModel().getSelectedItem();

        if (materiaSelecionada != null) {
            try {
                materiaDAO.delete(materiaSelecionada.getIdMateria());
                carregarDados();
                limparCampos();
            } catch (SQLException e) {
                mostrarAlerta("Erro ao remover matéria: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Selecione uma matéria para remover!");
        }
    }

    /**
     * Cria uma instância de {@link Materia} a partir dos valores do formulário.
     *
     * @return nova instância de Materia com dados preenchidos
     */
    private Materia criarMateriaFromForm() {
        Materia materia = new Materia();
        if (materiaEmEdicao != null) {
            materia.setIdMateria(materiaEmEdicao.getIdMateria());
        }
        materia.setNome(txtNomeMateria.getText().trim());
        materia.setCargaHoraria(Integer.parseInt(txtCargaHoraria.getText()));
        materia.setCurso(cbCurso.getValue());
        return materia;
    }

    /**
     * Valida os campos do formulário antes de operações de inserção ou edição.
     *
     * @return true se estiver tudo válido, false caso contrário
     */
    private boolean validarCampos() {
        if (txtNomeMateria.getText().isBlank() ||
                txtCargaHoraria.getText().isBlank() ||
                cbCurso.getValue() == null) {
            mostrarAlerta("Preencha todos os campos!");
            return false;
        }
        try {
            Integer.parseInt(txtCargaHoraria.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Carga horária deve ser um número inteiro!");
            return false;
        }
        if (materiaEmEdicao == null) {
            try {
                if (materiaDAO.existeMateria(
                        txtNomeMateria.getText().trim(),
                        cbCurso.getValue().getIdCurso())) {
                    mostrarAlerta("Matéria já existe para este curso!");
                    return false;
                }
            } catch (SQLException e) {
                mostrarAlerta("Erro ao validar matéria: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * Atualiza campos do formulário para a matéria selecionada.
     *
     * @param materia matéria selecionada na tabela
     */
    private void selecionarMateria(Materia materia) {
        if (materia != null) {
            txtNomeMateria.setText(materia.getNome());
            txtCargaHoraria.setText(String.valueOf(materia.getCargaHoraria()));
            cbCurso.setValue(materia.getCurso());
        }
    }

    /**
     * Exibe alerta de erro com a mensagem informada.
     *
     * @param mensagem texto a ser exibido
     */
    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Limpa todos os campos do formulário e reseta estado de edição.
     */
    private void limparCampos() {
        materiaEmEdicao = null;
        txtNomeMateria.clear();
        txtCargaHoraria.clear();
        cbCurso.getSelectionModel().clearSelection();
        tableMateria.getSelectionModel().clearSelection();
        atualizarInterfaceParaEdicao();
    }

    /**
     * Ação para o botão Voltar, retorna à tela principal.
     */
    @FXML
    private void btnVoltarClick() {
        ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
    }
}
