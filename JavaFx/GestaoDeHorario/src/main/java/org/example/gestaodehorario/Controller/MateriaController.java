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

public class MateriaController {

    // Componentes FXML
    @FXML private TextField txtNomeMateria;
    @FXML private ComboBox<Curso> cbCurso;
    @FXML private TextField txtCargaHoraria;
    @FXML private TableView<Materia> tableMateria;
    @FXML private TableColumn<Materia, String> colNome;
    @FXML private TableColumn<Materia, String> colCurso;
    @FXML private TableColumn<Materia, Integer> colCargaHoraria;

    @FXML private Button btnAdicionar;
    @FXML private Button btnEditar;

    // DAOs
    private final MateriaDAO materiaDAO = new MateriaDAO();
    private final CursoDAO cursoDAO = new CursoDAO();

    // Lista observável para a TableView
    private final ObservableList<Materia> materiasList = FXCollections.observableArrayList();

    private Materia materiaEmEdicao = null;

    @FXML
    public void initialize() {
        configurarTabela();
        configurarComboBox();
        carregarDados();
        atualizarInterfaceParaEdicao();
    }

    private void configurarTabela() {
        // Configuração das colunas
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colCurso.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCurso().getNome())
        );
        colCargaHoraria.setCellValueFactory(cellData -> cellData.getValue().cargaHorariaProperty().asObject());

        // Vincula a lista à tabela
        tableMateria.setItems(materiasList);

        // Listener para seleção na tabela
        tableMateria.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarMateria(newValue)
        );
        tableMateria.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, newValue) -> preencherCampos(newValue)
        );
    }

    private void atualizarInterfaceParaEdicao() {
        if (materiaEmEdicao != null) {
            btnAdicionar.setText("Salvar Edição");
            btnEditar.setDisable(true);
        } else {
            btnAdicionar.setText("Adicionar");
            btnEditar.setDisable(false);
        }
    }

    private void configurarComboBox() {
        // Configura como os cursos serão exibidos
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

        // Carrega os cursos do banco
        try {
            cbCurso.getItems().setAll(cursoDAO.getAll());
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar cursos: " + e.getMessage());
        }
    }

    private void carregarDados() {
        try {
            materiasList.setAll(materiaDAO.getAll());
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar matérias: " + e.getMessage());
        }
    }

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

    private void preencherCampos(Materia materia) {
        if (materia != null) {
            // Preenche os campos básicos
            txtNomeMateria.setText(materia.getNome());
            txtCargaHoraria.setText(String.valueOf(materia.getCargaHoraria()));
            txtCargaHoraria.setText(materia.getCargaHoraria() > 0
                    ? String.valueOf(materia.getCargaHoraria())
                    : "");

            // Configura o ComboBox de cursos
            selecionarCursoNoComboBox(materia.getCurso());
        } else {
            limparCampos();
        }
    }

    private void selecionarCursoNoComboBox(Curso curso) {
        if (curso != null) {
            // Procura o curso na lista do ComboBox
            cbCurso.getItems().stream()
                    .filter(c -> c.getIdCurso() == curso.getIdCurso())
                    .findFirst()
                    .ifPresent(c -> cbCurso.getSelectionModel().select(c));
        } else {
            cbCurso.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void btnAdicionarClick() {
        if (validarCampos()) {
            Materia materia = criarMateriaFromForm();

            try {
                if (materiaEmEdicao == null) {
                    // Modo de adição - verificar duplicata
                    if (materiaDAO.existeMateria(materia.getNome(), materia.getCurso().getIdCurso())) {
                        mostrarAlerta("Já existe uma matéria com este nome para o curso selecionado!");
                        return;
                    }
                    materiaDAO.insert(materia);
                } else {
                    // Modo de edição - atualizar existente
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

    private Materia criarMateriaFromForm() {
        Materia materia = new Materia();
        // Se estiver editando, manter o ID original
        if (materiaEmEdicao != null) {
            materia.setIdMateria(materiaEmEdicao.getIdMateria());
        }
        materia.setNome(txtNomeMateria.getText().trim());
        materia.setCargaHoraria(Integer.parseInt(txtCargaHoraria.getText()));
        materia.setCurso(cbCurso.getValue());
        return materia;
    }

    private boolean validarCampos() {
        // Validação básica dos campos
        if (txtNomeMateria.getText().isBlank() ||
                txtCargaHoraria.getText().isBlank() ||
                cbCurso.getValue() == null) {

            mostrarAlerta("Preencha todos os campos!");
            return false;
        }

        // Validação numérica
        try {
            Integer.parseInt(txtCargaHoraria.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Carga horária deve ser um número inteiro!");
            return false;
        }

        // Validação de duplicata apenas para novos registros
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

    private void selecionarMateria(Materia materia) {
        if (materia != null) {
            txtNomeMateria.setText(materia.getNome());
            txtCargaHoraria.setText(String.valueOf(materia.getCargaHoraria()));
            cbCurso.setValue(materia.getCurso());
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void limparCampos() {
        materiaEmEdicao = null; // Resetar estado de edição
        txtNomeMateria.clear();
        txtCargaHoraria.clear();
        cbCurso.getSelectionModel().clearSelection();
        tableMateria.getSelectionModel().clearSelection();
        atualizarInterfaceParaEdicao();
    }

    @FXML
    private void btnVoltarClick() {
        ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
    }
}