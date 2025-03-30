package org.example.gestaodehorario.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class CursosController {
    @FXML private TableView<Curso> cursosTable;
    @FXML private TableColumn<Curso, Integer> idColumn;
    @FXML private TableColumn<Curso, String> nomeColumn;
    @FXML private TableColumn<Curso, String> periodoColumn;
    @FXML private TableColumn<Curso, String> coordenadorColumn;
    @FXML private TextField txtNomeCurso;
    @FXML private ComboBox<Periodo> comboPeriodos;
    @FXML private ComboBox<Coordenador> comboCoordenadores;

    private final PeriodoDAO periodoDAO = new PeriodoDAO();
    private final CoordenadorDAO coordenadorDAO = new CoordenadorDAO();
    private final CursoDAO cursoDAO = new CursoDAO();
    private final ObservableList<Curso> cursosList = FXCollections.observableArrayList();
    private Map<Integer, Periodo> periodosMap = new HashMap<>();
    private Map<Integer, Coordenador> coordenadoresMap = new HashMap<>();

    @FXML
    public void initialize() {
        // Verificação para garantir que os componentes FXML foram injetados
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

    private void selecionarCurso(Curso curso) {
        if (curso != null) {
            txtNomeCurso.setText(curso.getNome());
            comboPeriodos.setValue(periodosMap.get(curso.getIdPeriodo()));
            comboCoordenadores.setValue(curso.getCoordenador());
        }
    }

    private boolean validarCampos() {
        if (txtNomeCurso.getText().isBlank() || comboPeriodos.getValue() == null || comboCoordenadores.getValue() == null) {
            mostrarAlerta("Preencha todos os campos e selecione um período/coordenador!");
            return false;
        }

        if (txtNomeCurso.getText().length() < 5 || txtNomeCurso.getText().length() > 100) {
            mostrarAlerta("Nome deve ter entre 5 e 100 caracteres!");
            return false;
        }

        return true;
    }

    private void limparCampos() {
        txtNomeCurso.clear();
        comboPeriodos.setValue(null);
        comboCoordenadores.setValue(null);
        cursosTable.getSelectionModel().clearSelection();
    }

    private void carregarDados() {
        try {
            cursosList.setAll(cursoDAO.getAll());
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar cursos: " + e.getMessage());
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
    private void btnVoltarClick(ActionEvent event) {
        ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
    }
}