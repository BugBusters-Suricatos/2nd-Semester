package org.example.gestaodehorario.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.dao.*;
import org.example.gestaodehorario.model.*;
import java.sql.SQLException;
import java.util.*;

/**
 * Controlador JavaFX responsável por gerenciar a disponibilidade de horários (indisponibilidade) de um professor.
 * <p>
 * Permite selecionar curso e semestre, marcar slots de tempo indisponíveis e salvar essas informações na base de dados.
 * </p>
 */
public class IndisponibilidadeController {

    /** Painel que exibe a grade horária com checkboxes para marcação de indisponibilidade. */
    @FXML private GridPane gradeHoraria;

    /** ComboBox para seleção do curso cujos slots serão exibidos. */
    @FXML private ComboBox<Curso> cbCursos;

    /** ComboBox para seleção do semestre cujos slots serão exibidos. */
    @FXML private ComboBox<Semestre> cbSemestres;

    /**
     * Estrutura que mapeia dia da semana e horário de início para o checkbox correspondente na grade.
     */
    private final Map<String, Map<String, CheckBox>> grade = new HashMap<>();

    /** DAO para acesso aos slots de horários disponíveis no sistema. */
    private final SlotDAO slotDAO = new SlotDAO();

    /** DAO para acesso a dados de cursos. */
    private final CursoDAO cursoDAO = new CursoDAO();

    /** DAO para acesso a dados de semestres. */
    private final SemestreDAO semestreDAO = new SemestreDAO();

    /** DAO para persistência das indisponibilidades marcadas. */
    private final IndisponibilidadeDAO indisponibilidadeDAO = new IndisponibilidadeDAO();

    /** Professor atualmente autenticado, cujas indisponibilidades serão salvas. */
    private Professor professorLogado;

    /**
     * Inicializa o controlador após a injeção dos componentes FXML.
     * <p>
     * Carrega cursos e semestres disponíveis e configura listeners para atualizar a grade.
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
     * @param professor instância de {@link Professor} autenticado
     */
    public void setProfessorLogado(Professor professor) {
        this.professorLogado = professor;
    }

    /**
     * Carrega dados iniciais de cursos e semestres para os ComboBoxes.
     *
     * @throws SQLException se ocorrer erro de acesso ao banco de dados
     */
    private void carregarDadosIniciais() throws SQLException {
        cbCursos.getItems().setAll(cursoDAO.getAll());
        cbSemestres.getItems().setAll(semestreDAO.getAll());
    }

    /**
     * Configura listeners para ComboBoxes de curso e semestre, invocando atualização da grade.
     */
    private void configurarListeners() {
        cbCursos.valueProperty().addListener((obs, oldVal, newVal) -> atualizarGrade());
        cbSemestres.valueProperty().addListener((obs, oldVal, newVal) -> atualizarGrade());
    }

    /**
     * Atualiza a grade horária exibida com base no curso e semestre selecionados.
     */
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

    /**
     * Constrói a grade de horários, adicionando labels de dias e horários e checkboxes para cada slot.
     *
     * @param slots lista de {@link Slot} contendo informações de dia, hora e status
     */
    private void construirGrade(List<Slot> slots) {
        gradeHoraria.getChildren().clear();
        grade.clear();

        String[] dias = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
        for (int i = 0; i < dias.length; i++) {
            gradeHoraria.add(new Label(dias[i]), i + 1, 0);
            grade.put(dias[i], new HashMap<>());
        }

        Map<String, Integer> linhaHorario = new HashMap<>();
        int linhaAtual = 1;

        for (Slot slot : slots) {
            String horario = slot.getHora_inicio() + " - " + slot.getHora_fim();
            if (!linhaHorario.containsKey(horario)) {
                gradeHoraria.add(new Label(horario), 0, linhaAtual);
                linhaHorario.put(horario, linhaAtual);
                linhaAtual++;
            }
            CheckBox checkBox = new CheckBox();
            checkBox.setDisable("Ocupado".equals(slot.getStatus()));
            int colunaDia = Arrays.asList(dias).indexOf(slot.getDia_semana()) + 1;
            gradeHoraria.add(checkBox, colunaDia, linhaHorario.get(horario));
            grade.get(slot.getDia_semana()).put(slot.getHora_inicio(), checkBox);
        }
    }

    /**
     * Persiste as indisponibilidades selecionadas pelo professor no banco de dados.
     */
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
                                    professorLogado.getId(), dia, horaInicio, horaFim,
                                    curso.getIdCurso(), semestre.getIdSemestre()));
                        } catch (SQLException e) {
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

    /**
     * Exibe um alerta de erro com a mensagem informada.
     *
     * @param mensagem texto a ser exibido no alerta de erro
     */
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Exibe um alerta de informação com a mensagem informada.
     *
     * @param mensagem texto a ser exibido no alerta de sucesso
     */
    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Ação executada ao clicar no botão de voltar, retorna para a tela principal.
     */
    @FXML
    private void btnVoltarClick() {
        ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
    }
}
