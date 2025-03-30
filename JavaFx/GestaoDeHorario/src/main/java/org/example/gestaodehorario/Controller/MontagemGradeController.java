package org.example.gestaodehorario.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.dao.*;
import org.example.gestaodehorario.model.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class MontagemGradeController {

    @FXML private GridPane gradeAlocacao;
    @FXML private ComboBox<Curso> cbCursos;
    @FXML private ComboBox<Semestre> cbSemestres;
    @FXML private ComboBox<Materia> cbMaterias;

    private final Map<String, Map<String, CheckBox>> grade = new HashMap<>();
    private final SlotDAO slotDAO = new SlotDAO();
    private final CursoDAO cursoDAO = new CursoDAO();
    private final SemestreDAO semestreDAO = new SemestreDAO();
    private final MateriaDAO materiaDAO = new MateriaDAO();
    private final AlocacaoDAO alocacaoDAO = new AlocacaoDAO();
    private Professor professorLogado;

    @FXML
    public void initialize() {
        try {
            carregarDadosIniciais();
            configurarListeners();
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar dados: " + e.getMessage());
        }
    }

    public void setProfessorLogado(Professor professor) {
        this.professorLogado = professor;
    }

    private void carregarDadosIniciais() throws SQLException {
        cbCursos.getItems().setAll(cursoDAO.getAll());
        cbSemestres.getItems().setAll(semestreDAO.getAll());
        cbMaterias.getItems().setAll(materiaDAO.getAll());
    }

    private void configurarListeners() {
        cbCursos.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                if (newVal != null) {
                    cbMaterias.getItems().setAll(materiaDAO.getByCurso(newVal.getIdCurso()));
                }
                atualizarGrade();
            } catch (SQLException e) {
                mostrarErro("Erro ao carregar matérias: " + e.getMessage());
            }
        });

        cbSemestres.valueProperty().addListener((obs, oldVal, newVal) -> atualizarGrade());
        cbMaterias.valueProperty().addListener((obs, oldVal, newVal) -> atualizarGrade());
    }

    private void atualizarGrade() {
        try {
            Curso curso = cbCursos.getValue();
            Semestre semestre = cbSemestres.getValue();
            Materia materia = cbMaterias.getValue();

            if (curso != null && semestre != null) {
                List<Slot> slots = slotDAO.getByCursoSemestre(curso.getIdCurso(), semestre.getIdSemestre());
                construirGrade(slots, materia);
            }
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar grade: " + e.getMessage());
        }
    }

    private void construirGrade(List<Slot> slots, Materia materia) {
        gradeAlocacao.getChildren().clear();
        grade.clear();

        // Cabeçalhos
        String[] dias = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
        for (int i = 0; i < dias.length; i++) {
            Label diaLabel = new Label(dias[i]);
            diaLabel.getStyleClass().add("header-cell");
            gradeAlocacao.add(diaLabel, i + 1, 0);
        }

        // Agrupar slots por horário
        Map<String, List<Slot>> slotsPorHorario = slots.stream()
                .collect(Collectors.groupingBy(s -> s.getHora_inicio() + " - " + s.getHora_fim()));

        int linha = 1;
        for (String horario : slotsPorHorario.keySet()) {
            // Horário na coluna 0
            Label horarioLabel = new Label(horario);
            horarioLabel.getStyleClass().add("time-cell");
            gradeAlocacao.add(horarioLabel, 0, linha);

            // Slots para cada dia
            List<Slot> slotsHorario = slotsPorHorario.get(horario);
            for (Slot slot : slotsHorario) {
                int coluna = Arrays.asList(dias).indexOf(slot.getDia_semana()) + 1;

                CheckBox checkBox = new CheckBox();
                checkBox.setDisable(!slot.getStatus().equals("Disponível"));
                checkBox.getStyleClass().add("slot-" + slot.getStatus().toLowerCase());

                if (materia != null) {
                    checkBox.setText(materia.getNome());
                }

                gradeAlocacao.add(checkBox, coluna, linha);
                grade.computeIfAbsent(slot.getDia_semana(), k -> new HashMap<>())
                        .put(slot.getHora_inicio(), checkBox);
            }
            linha++;
        }
    }

    @FXML
    private void salvarAlocacoes() {
        try {
            Curso curso = cbCursos.getValue();
            Semestre semestre = cbSemestres.getValue();
            Materia materia = cbMaterias.getValue();

            if (curso == null || semestre == null || materia == null || professorLogado == null) {
                mostrarErro("Selecione todos os campos!");
                return;
            }

            // 1. Buscar a associação MateriaProfessor
            MateriaProfessorDAO mpDAO = new MateriaProfessorDAO();
            Optional<MateriaProfessor> mp = mpDAO.getByMateriaProfessor(
                    materia.getIdMateria(),
                    professorLogado.getId()
            );

            if (mp.isEmpty()) {
                mostrarErro("Professor não está vinculado a esta matéria!");
                return;
            }

            // 2. Criar alocações
            List<Alocacao> alocacoes = new ArrayList<>();
            grade.forEach((dia, horarios) -> horarios.forEach((horaInicio, checkBox) -> {
                if (checkBox.isSelected()) {
                    try {
                        Optional<Slot> slot = slotDAO.getByHorarioDia(horaInicio, dia, curso.getIdPeriodo());
                        if (slot.isPresent()) {
                            alocacoes.add(new Alocacao(
                                    mp.get(),
                                    slot.get(),
                                    semestre
                            ));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }));

            // 3. Salvar
            alocacaoDAO.salvar(alocacoes);
            mostrarSucesso("Alocações salvas!");
            atualizarGrade();

        } catch (Exception e) {
            mostrarErro("Erro: " + e.getMessage());
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