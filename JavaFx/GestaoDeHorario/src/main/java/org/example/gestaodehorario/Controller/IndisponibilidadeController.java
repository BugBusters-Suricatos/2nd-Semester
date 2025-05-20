package org.example.gestaodehorario.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.dao.*;
import org.example.gestaodehorario.model.*;

import java.sql.SQLException;
import java.util.List;

public class IndisponibilidadeController {

    @FXML private ComboBox<Curso> cbCursos;
    @FXML private ComboBox<Professor> cbProfessores;
    @FXML private ComboBox<String> cbDiaSemana;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFim;
    @FXML private TableView<Indisponibilidade> tabelaIndisponibilidades;
    @FXML private TableColumn<Indisponibilidade, String> colCurso;
    @FXML private TableColumn<Indisponibilidade, String> colProfessor;
    @FXML private TableColumn<Indisponibilidade, String> colDiaSemana;
    @FXML private TableColumn<Indisponibilidade, String> colHoraInicio;
    @FXML private TableColumn<Indisponibilidade, String> colHoraFim;



    private final CursoDAO cursoDAO = new CursoDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private final IndisponibilidadeDAO indisponibilidadeDAO = new IndisponibilidadeDAO();

    private ObservableList<Indisponibilidade> listaIndisponibilidades = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            cbCursos.getItems().setAll(cursoDAO.getAll());
            cbProfessores.getItems().setAll(professorDAO.getAllComMaterias());

            cbDiaSemana.getItems().setAll("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado");

            configurarColunasTabela();
            atualizarTabela();
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar dados iniciais: " + e.getMessage());
        }
    }

    private void configurarColunasTabela() {
        colCurso.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getId_curso() != null && cbCursos.getItems() != null
                                ? getCursoNomeById(data.getValue().getId_curso()) : "")
        );
        colProfessor.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getId_professor() != null && cbProfessores.getItems() != null
                                ? getProfessorNomeById(data.getValue().getId_professor()) : "")
        );
        colDiaSemana.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDia_semana()));
        colHoraInicio.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHora_inicio()));
        colHoraFim.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHora_fim()));
        tabelaIndisponibilidades.setItems(listaIndisponibilidades);
    }

    private String getCursoNomeById(Integer idCurso) {
        if (idCurso == null) return "";
        return cbCursos.getItems().stream()
                .filter(c -> c.getIdCurso() == idCurso)
                .map(Curso::getNome)
                .findFirst().orElse("");
    }

    private String getProfessorNomeById(Integer idProfessor) {
        if (idProfessor == null) return "";
        return cbProfessores.getItems().stream()
                .filter(p -> p.getId() == idProfessor)
                .map(Professor::getNome)
                .findFirst().orElse("");
    }

    @FXML
    private void onAdicionarClick() {
        Curso curso = cbCursos.getValue();
        Professor professor = cbProfessores.getValue();
        String diaSemana = cbDiaSemana.getValue();
        String horaInicio = txtHoraInicio.getText();
        String horaFim = txtHoraFim.getText();

        if (professor == null || diaSemana == null || horaInicio.isEmpty() || horaFim.isEmpty()) {
            mostrarErro("Preencha todos os campos obrigatórios.");
            return;
        }

        Indisponibilidade nova = new Indisponibilidade();
        nova.setId_professor(professor.getId());
        nova.setDia_semana(diaSemana);
        nova.setHora_inicio(horaInicio);
        nova.setHora_fim(horaFim);

        // opcional: setar curso (se ainda quiser mostrar na tabela)
        nova.setId_curso(curso != null ? curso.getIdCurso() : null);

        listaIndisponibilidades.add(nova);
        limparCampos();
    }

    @FXML
    private void onRemoverSelecionadoClick() {
        Indisponibilidade selecionada = tabelaIndisponibilidades.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            listaIndisponibilidades.remove(selecionada);
        }
    }

    @FXML
    private void onSalvarTodasClick() {
        try {
            ObservableList<Indisponibilidade> indisponibilidades = tabelaIndisponibilidades.getItems();
            for (Indisponibilidade ind : indisponibilidades) {
                indisponibilidadeDAO.salvarIndisponibilidade(ind);
            }
            mostrarSucesso("Todas as indisponibilidades foram salvas no banco de dados!");
            // Após salvar, pode limpar a tabela se quiser:
            // tabelaIndisponibilidades.getItems().clear();
        } catch (Exception e) {
            mostrarErro("Erro ao salvar todas as indisponibilidades: " + e.getMessage());
        }
    }

    private void atualizarTabela() {
        try {
            // Se quiser mostrar as já salvas no banco, implemente getAll()
            // List<Indisponibilidade> lista = indisponibilidadeDAO.getAll();
            // listaIndisponibilidades.setAll(lista);
        } catch (Exception e) {
            // Não exibe erro se não usar
        }
    }

    private void limparCampos() {
        cbDiaSemana.getSelectionModel().clearSelection();
        txtHoraInicio.clear();
        txtHoraFim.clear();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void btnVoltarClick() {
        ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
    }
}
