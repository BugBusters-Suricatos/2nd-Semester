package org.example.gestaodehorario.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.dao.*;
import org.example.gestaodehorario.model.*;
import java.sql.SQLException;
import java.util.*;

public class IndisponibilidadeController {

    @FXML private GridPane gradeHoraria;
    @FXML private ComboBox<Curso> cbCursos;
    @FXML private ComboBox<Semestre> cbSemestres;

    private final Map<String, Map<String, CheckBox>> grade = new HashMap<>();
    private final SlotDAO slotDAO = new SlotDAO();
    private final CursoDAO cursoDAO = new CursoDAO();
    private final SemestreDAO semestreDAO = new SemestreDAO();
    private final IndisponibilidadeDAO indisponibilidadeDAO = new IndisponibilidadeDAO();
    private Professor professorLogado; // Supondo que o professor está autenticado

    @FXML
    public void initialize() {
        try {
            carregarDadosIniciais();
            configurarListeners();
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar dados: " + e.getMessage());
        }
    }

    // Método para definir o professor logado (chamado após o login)
    public void setProfessorLogado(Professor professor) {
        this.professorLogado = professor;
    }

    private void carregarDadosIniciais() throws SQLException {
        cbCursos.getItems().setAll(cursoDAO.getAll());
        cbSemestres.getItems().setAll(semestreDAO.getAll());
    }

    private void configurarListeners() {
        cbCursos.valueProperty().addListener((obs, oldVal, newVal) -> atualizarGrade());
        cbSemestres.valueProperty().addListener((obs, oldVal, newVal) -> atualizarGrade());
    }

    private void atualizarGrade() {
        try {
            Curso curso = cbCursos.getValue();
            Semestre semestre = cbSemestres.getValue();
            if (curso != null && semestre != null) {
                List<Slot> slots = slotDAO.getByCursoSemestre(curso.getIdCurso(), semestre.getIdSemestre());
                construirGrade(slots);
            }
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar grade: " + e.getMessage());
        }
    }

    private void construirGrade(List<Slot> slots) {
        gradeHoraria.getChildren().clear();
        grade.clear();

        // Cabeçalhos dos dias
        String[] dias = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
        for (int i = 0; i < dias.length; i++) {
            gradeHoraria.add(new Label(dias[i]), i + 1, 0);
            grade.put(dias[i], new HashMap<>());
        }

        // Mapeamento de horários para linhas
        Map<String, Integer> linhaHorario = new HashMap<>();
        int linhaAtual = 1;

        for (Slot slot : slots) {
            String horario = slot.getHora_inicio() + " - " + slot.getHora_fim();

            // Adiciona horário na coluna 0 se não existir
            if (!linhaHorario.containsKey(horario)) {
                gradeHoraria.add(new Label(horario), 0, linhaAtual);
                linhaHorario.put(horario, linhaAtual);
                linhaAtual++;
            }

            // Adiciona checkbox na coluna do dia
            CheckBox checkBox = new CheckBox();
            checkBox.setDisable("Ocupado".equals(slot.getStatus()));
            int colunaDia = Arrays.asList(dias).indexOf(slot.getDia_semana()) + 1;
            gradeHoraria.add(checkBox, colunaDia, linhaHorario.get(horario));
            grade.get(slot.getDia_semana()).put(slot.getHora_inicio(), checkBox);
        }
    }

    @FXML
    private void salvarIndisponibilidade() {
        try {
            Curso curso = cbCursos.getValue();
            Semestre semestre = cbSemestres.getValue();

            if (professorLogado == null || curso == null || semestre == null) {
                mostrarErro("Selecione todos os campos obrigatórios!");
                return;
            }

            List<Indisponibilidade> indisponibilidades = new ArrayList<>();
            grade.forEach((dia, horarios) -> {
                horarios.forEach((horaInicio, checkBox) -> {
                    if (checkBox.isSelected()) {
                        try {
                            String horaFim = slotDAO.findHoraFimByHoraInicio(horaInicio, curso.getIdPeriodo());
                            indisponibilidades.add(new Indisponibilidade(
                                    professorLogado.getId(),
                                    dia,
                                    horaInicio,
                                    horaFim,
                                    curso.getIdCurso(),
                                    semestre.getIdSemestre()
                            ));
                        } catch (SQLException e) {
                            // Trate o erro, por exemplo, logando ou mostrando uma mensagem
                            mostrarErro("Erro ao obter hora fim: " + e.getMessage());
                        }
                    }
                });
            });

            indisponibilidadeDAO.salvar(indisponibilidades);
            mostrarSucesso("Indisponibilidades salvas com sucesso!");
        } catch (Exception e) {
            mostrarErro("Erro ao salvar: " + e.getMessage());
        }
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