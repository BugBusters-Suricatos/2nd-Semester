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

/**
 * Controlador JavaFX responsável pela montagem da grade de alocação de aulas.
 * <p>
 * Permite selecionar curso, semestre e matéria, exibe a grade horária com slots e
 * persiste as alocações feitas pelo professor logado.
 * </p>
 */
public class MontagemGradeController {

    /** GridPane que exibe a grade de alocação com checkboxes para cada slot. */
    @FXML private GridPane gradeAlocacao;

    /** ComboBox para seleção do curso cujas matérias serão exibidas. */
    @FXML private ComboBox<Curso> cbCursos;

    /** ComboBox para seleção do semestre que define a grade de horários. */
    @FXML private ComboBox<Semestre> cbSemestres;

    /** ComboBox para seleção da matéria a ser alocada nos slots. */
    @FXML private ComboBox<Materia> cbMaterias;

    /**
     * Mapa que associa dia da semana a um mapa de horários e seus respectivos CheckBoxes.
     */
    private final Map<String, Map<String, CheckBox>> grade = new HashMap<>();

    /** DAO para acesso aos slots de horários disponíveis. */
    private final SlotDAO slotDAO = new SlotDAO();

    /** DAO para acesso aos cursos cadastrados. */
    private final CursoDAO cursoDAO = new CursoDAO();

    /** DAO para acesso aos semestres cadastrados. */
    private final SemestreDAO semestreDAO = new SemestreDAO();

    /** DAO para acesso às matérias cadastradas. */
    private final MateriaDAO materiaDAO = new MateriaDAO();

    /** DAO para persistência das alocações feitas. */
    private final AlocacaoDAO alocacaoDAO = new AlocacaoDAO();

    /** Professor autenticado cujas alocações serão salvas. */
    private Professor professorLogado;

    /**
     * Inicializa o controlador após injeção dos componentes FXML.
     * <p>
     * Carrega dados iniciais de cursos, semestres e matérias, e configura listeners.
     * </p>
     */
    @FXML
    public void initialize() {
        try {
            carregarDadosIniciais();
            configurarListeners();
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar dados: " + e.getMessage());
        }
    }

    /**
     * Define o professor logado no sistema.
     *
     * @param professor instância {@link Professor} autenticada
     */
    public void setProfessorLogado(Professor professor) {
        this.professorLogado = professor;
    }

    /**
     * Carrega cursos, semestres e matérias para preenchimento dos ComboBoxes.
     *
     * @throws SQLException se ocorrer falha de acesso ao banco
     */
    private void carregarDadosIniciais() throws SQLException {
        cbCursos.getItems().setAll(cursoDAO.getAll());
        cbSemestres.getItems().setAll(semestreDAO.getAll());
        cbMaterias.getItems().setAll(materiaDAO.getAll());
    }

    /**
     * Configura listeners para atualizar a grade ao mudar seleções.
     */
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

    /**
     * Atualiza a grade horária com base em curso, semestre e matéria selecionados.
     */
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

    /**
     * Constrói a grade de alocação, agrupando slots por horário e posicionando CheckBoxes.
     *
     * @param slots lista de {@link Slot} contendo dia, horário e status
     * @param materia matéria a ser exibida nos CheckBoxes (pode ser null)
     */
    private void construirGrade(List<Slot> slots, Materia materia) {
        gradeAlocacao.getChildren().clear();
        grade.clear();
        String[] dias = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
        for (int i = 0; i < dias.length; i++) {
            Label diaLabel = new Label(dias[i]);
            diaLabel.getStyleClass().add("header-cell");
            gradeAlocacao.add(diaLabel, i + 1, 0);
        }
        Map<String, List<Slot>> slotsPorHorario = slots.stream()
                .collect(Collectors.groupingBy(s -> s.getHora_inicio() + " - " + s.getHora_fim()));
        int linha = 1;
        for (String horario : slotsPorHorario.keySet()) {
            Label horarioLabel = new Label(horario);
            horarioLabel.getStyleClass().add("time-cell");
            gradeAlocacao.add(horarioLabel, 0, linha);
            List<Slot> slotsHorario = slotsPorHorario.get(horario);
            for (Slot slot : slotsHorario) {
                int coluna = Arrays.asList(dias).indexOf(slot.getDia_semana()) + 1;
                CheckBox checkBox = new CheckBox();
                checkBox.setDisable(!"Disponível".equals(slot.getStatus()));
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

    /**
     * Persiste as alocações selecionadas na grade para o professor logado.
     */
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
            MateriaProfessorDAO mpDAO = new MateriaProfessorDAO();
            Optional<MateriaProfessor> mp = mpDAO.getByMateriaProfessor(
                    materia.getIdMateria(),
                    professorLogado.getId()
            );
            if (mp.isEmpty()) {
                mostrarErro("Professor não está vinculado a esta matéria!");
                return;
            }
            List<Alocacao> alocacoes = new ArrayList<>();
            grade.forEach((dia, horarios) -> horarios.forEach((horaInicio, checkBox) -> {
                if (checkBox.isSelected()) {
                    try {
                        Optional<Slot> slot = slotDAO.getByHorarioDia(horaInicio, dia, curso.getIdPeriodo());
                        slot.ifPresent(s -> alocacoes.add(new Alocacao(mp.get(), s, semestre)));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }));
            alocacaoDAO.salvar(alocacoes);
            mostrarSucesso("Alocações salvas!");
            atualizarGrade();
        } catch (Exception e) {
            mostrarErro("Erro: " + e.getMessage());
        }
    }

    /**
     * Exibe alerta de erro com texto especificado.
     *
     * @param mensagem mensagem de erro a ser exibida
     */
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Exibe alerta de informação com texto especificado.
     *
     * @param mensagem mensagem de sucesso a ser exibida
     */
    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Ação executada ao clicar no botão de voltar, retornando à tela principal.
     */
    @FXML
    private void btnVoltarClick() {
        ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
    }
}
