package org.example.gestaodehorario.Controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.dao.*;
import org.example.gestaodehorario.model.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador JavaFX responsável pelas interações da tela de gerenciamento de cursos.
 * <p>
 * Realiza operações de CRUD (criar, ler, atualizar e excluir) em cursos,
 * além de carregar dados de períodos e coordenadores para seleção.
 * </p>
 */
public class CursosController {

    /** Tabela que exibe a lista de cursos. */
    @FXML private TableView<Curso> cursosTable;

    /** Coluna de identificação do curso na tabela. */
    @FXML private TableColumn<Curso, Integer> idColumn;

    /** Coluna de nome do curso na tabela. */
    @FXML private TableColumn<Curso, String> nomeColumn;

    /** Coluna de período associado ao curso na tabela. */
    @FXML private TableColumn<Curso, String> periodoColumn;

    /** Coluna de coordenador associado ao curso na tabela. */
    @FXML private TableColumn<Curso, String> coordenadorColumn;

    /** Campo de texto para entrada do nome do curso. */
    @FXML private TextField txtNomeCurso;

    /** ComboBox para seleção de período ao cadastrar ou editar curso. */
    @FXML private ComboBox<Periodo> comboPeriodos;

    /** ComboBox para seleção de coordenador ao cadastrar ou editar curso. */
    @FXML private ComboBox<Coordenador> comboCoordenadores;

    /** DAO para acesso a dados de períodos. */
    private final PeriodoDAO periodoDAO = new PeriodoDAO();

    /** DAO para acesso a dados de coordenadores. */
    private final CoordenadorDAO coordenadorDAO = new CoordenadorDAO();

    /** DAO para acesso a dados de cursos. */
    private final CursoDAO cursoDAO = new CursoDAO();

    /** Lista observável que alimenta a tabela de cursos. */
    private final ObservableList<Curso> cursosList = FXCollections.observableArrayList();

    /** Mapa auxiliar para busca rápida de períodos por ID. */
    private Map<Integer, Periodo> periodosMap = new HashMap<>();

    /** Mapa auxiliar para busca rápida de coordenadores por ID. */
    private Map<Integer, Coordenador> coordenadoresMap = new HashMap<>();

    /**
     * Inicializa o controlador após a injeção dos componentes FXML.
     * <p>
     * Realiza verificações de injeção, carrega períodos, coordenadores,
     * configura a tabela e carrega os dados iniciais.
     * </p>
     */
    @FXML
    public void initialize() {
        if (comboPeriodos == null) {
            throw new IllegalStateException("comboPeriodos não foi injetado corretamente pelo FXML.");
        }
        if (comboCoordenadores == null) {
            throw new IllegalStateException("comboCoordenadores não foi injetado corretamente pelo FXML.");
        }
        if (cursosTable == null) {
            throw new IllegalStateException("cursosTable não foi injetado corretamente pelo FXML.");
        }

        carregarPeriodos();
        carregarCoordenadores();
        configurarTabela();
        carregarDados();
    }

    /**
     * Configura as colunas da tabela, associa valores e define listener de seleção.
     */
    private void configurarTabela() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idCurso"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        periodoColumn.setCellValueFactory(cellData -> {
            Periodo periodo = periodosMap.get(cellData.getValue().getIdPeriodo());
            return new SimpleStringProperty(periodo != null ? periodo.getNome() : "N/D");
        });

        coordenadorColumn.setCellValueFactory(cellData -> {
            Coordenador coord = cellData.getValue().getCoordenador();
            return new SimpleStringProperty(coord != null ? coord.getNome() : "N/D");
        });

        cursosTable.setItems(cursosList);
        cursosTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarCurso(newValue)
        );
    }

    /**
     * Carrega lista de períodos do banco e preenche o ComboBox correspondente.
     */
    private void carregarPeriodos() {
        try {
            List<Periodo> periodos = periodoDAO.getAll();
            periodosMap = periodos.stream().collect(Collectors.toMap(Periodo::getIdPeriodo, p -> p));
            comboPeriodos.getItems().setAll(periodos);
            configurarComboPeriodos();
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar períodos: " + e.getMessage());
        }
    }

    /**
     * Carrega lista de coordenadores do banco e preenche o ComboBox correspondente.
     */
    private void carregarCoordenadores() {
        try {
            List<Coordenador> coordenadores = coordenadorDAO.getAll();
            coordenadoresMap = coordenadores.stream().collect(Collectors.toMap(Coordenador::getIdCoordenador, c -> c));
            comboCoordenadores.getItems().setAll(coordenadores);
            configurarComboCoordenadores();
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar coordenadores: " + e.getMessage());
        }
    }

    /**
     * Configura o conversor de valores exibidos no ComboBox de períodos.
     */
    private void configurarComboPeriodos() {
        comboPeriodos.setConverter(new StringConverter<Periodo>() {
            @Override
            public String toString(Periodo periodo) {
                return periodo != null ? periodo.getNome() : "";
            }

            @Override
            public Periodo fromString(String s) {
                return null;
            }
        });
    }

    /**
     * Configura o conversor de valores exibidos no ComboBox de coordenadores.
     */
    private void configurarComboCoordenadores() {
        comboCoordenadores.setConverter(new StringConverter<Coordenador>() {
            @Override
            public String toString(Coordenador coordenador) {
                return coordenador != null ? coordenador.getNome() : "";
            }

            @Override
            public Coordenador fromString(String s) {
                return null;
            }
        });
    }

    /**
     * Adiciona um novo curso ao sistema se os campos estiverem válidos.
     * <p>
     * Verifica existência prévia e insere no banco de dados.
     * </p>
     */
    @FXML
    private void adicionarCurso() {
        if (validarCampos()) {
            String nome = txtNomeCurso.getText().trim();
            Periodo periodo = comboPeriodos.getValue();

            try {
                if (cursoDAO.existeCurso(nome, periodo.getIdPeriodo())) {
                    mostrarAlerta("Já existe um curso com este nome e período!");
                    return;
                }

                Curso novoCurso = new Curso();
                novoCurso.setNome(nome);
                novoCurso.setIdPeriodo(periodo.getIdPeriodo());
                novoCurso.setCoordenador(comboCoordenadores.getValue());

                cursoDAO.insert(novoCurso);
                carregarDados();
                limparCampos();
            } catch (SQLException e) {
                mostrarAlerta("Erro ao adicionar curso: " + e.getMessage());
            }
        }
    }

    /**
     * Edita o curso selecionado na tabela com os novos dados preenchidos.
     */
    @FXML
    private void editarCurso() {
        Curso cursoSelecionado = cursosTable.getSelectionModel().getSelectedItem();
        if (cursoSelecionado == null || comboPeriodos.getValue() == null || comboCoordenadores.getValue() == null) {
            mostrarAlerta("Selecione um curso e preencha todos os campos!");
            return;
        }

        if (validarCampos()) {
            cursoSelecionado.setNome(txtNomeCurso.getText());
            cursoSelecionado.setIdPeriodo(comboPeriodos.getValue().getIdPeriodo());
            cursoSelecionado.setCoordenador(comboCoordenadores.getValue());

            try {
                cursoDAO.update(cursoSelecionado);
                carregarDados();
                limparCampos();
            } catch (SQLException e) {
                mostrarAlerta("Erro ao atualizar curso: " + e.getMessage());
            }
        }
    }

    /**
     * Remove o curso selecionado no sistema.
     */
    @FXML
    private void removerCurso() {
        Curso cursoSelecionado = cursosTable.getSelectionModel().getSelectedItem();
        if (cursoSelecionado != null) {
            try {
                cursoDAO.delete(cursoSelecionado.getIdCurso());
                carregarDados();
                limparCampos();
            } catch (SQLException e) {
                mostrarAlerta("Erro ao remover curso: " + e.getMessage());
            }
        }
    }

    /**
     * Preenche campos do formulário com dados do curso selecionado na tabela.
     *
     * @param curso curso selecionado na tabela
     */
    private void selecionarCurso(Curso curso) {
        if (curso != null) {
            txtNomeCurso.setText(curso.getNome());
            comboPeriodos.setValue(periodosMap.get(curso.getIdPeriodo()));
            comboCoordenadores.setValue(curso.getCoordenador());
        }
    }

    public class ValidatorUtil {
        public static boolean validarNome(String nome, int min, int max) {
            return nome != null
                    && nome.trim().length() >= min
                    && nome.trim().length() <= max;
        }
    }

    /**
     * Valida os campos de entrada antes de operações de adicionar ou editar.
     *
     * @return true se todos os campos estiverem preenchidos corretamente, false caso contrário
     */
    private boolean validarCampos() {
        if (!ValidatorUtil.validarNome(txtNomeCurso.getText(), 4, 100)) {
            mostrarAlerta("Nome inválido!");
            return false;
        }

        if (txtNomeCurso.getText().isBlank() || comboPeriodos.getValue() == null || comboCoordenadores.getValue() == null) {
            mostrarAlerta("Preencha todos os campos e selecione um período/coordenador!");
            return false;
        }

        if (txtNomeCurso.getText().length() < 5 || txtNomeCurso.getText().length() > 100) {
            mostrarAlerta("Nome deve ter entre 4 e 100 caracteres!");
            return false;
        }

        return true;
    }

    /**
     * Limpa todos os campos do formulário e desmarca seleção na tabela.
     */
    private void limparCampos() {
        txtNomeCurso.clear();
        comboPeriodos.setValue(null);
        comboCoordenadores.setValue(null);
        cursosTable.getSelectionModel().clearSelection();
    }

    /**
     * Carrega a lista de cursos do banco de dados e atualiza a tabela.
     */
    private void carregarDados() {
        Service<Void> service = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws SQLException {
                        List<Curso> cursos = cursoDAO.getAll();
                        Platform.runLater(() -> cursosList.setAll(cursos));
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    /**
     * Exibe um alerta de erro com mensagem informada.
     *
     * @param mensagem texto a ser exibido no alerta
     */
    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Ação executada ao clicar no botão de voltar, retornando à tela principal.
     *
     * @param event evento de ação do botão
     */
    @FXML
    private void btnVoltarClick(ActionEvent event) {
        ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
    }
}