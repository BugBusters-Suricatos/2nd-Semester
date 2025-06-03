package org.example.gestaodehorario.Controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.dao.*;
import org.example.gestaodehorario.model.Curso;
import org.example.gestaodehorario.model.Professor;
import org.example.gestaodehorario.model.Semestre;
import org.example.gestaodehorario.model.Slot;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class IndisponibilidadeController {

    @FXML private ComboBox<Curso> cbCursos;
    @FXML private ComboBox<Semestre> cbSemestres; // Alterado para Semestre
    @FXML private ComboBox<Professor> cbProfessores;
    @FXML private Button btnSalvarTodas;
    @FXML private GridPane gridSlots;

    private final CursoDAO cursoDAO = new CursoDAO();
    private final SemestreDAO semestreDAO = new SemestreDAO(); // Adicionado
    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private final SlotDAO slotDAO = new SlotDAO();
    private final IndisponibilidadeDAO indisponibilidadeDAO = new IndisponibilidadeDAO();

    private Map<Slot, CheckBox> slotCheckBoxMap = new HashMap<>();
    private List<Slot> slots = new ArrayList<>();

    @FXML
    public void initialize() {
        // Configura conversores
        cbCursos.setConverter(new StringConverter<Curso>() {
            @Override public String toString(Curso curso) { return curso != null ? curso.getNome() : ""; }
            @Override public Curso fromString(String string) { return null; }
        });

        cbSemestres.setConverter(new StringConverter<Semestre>() {
            @Override public String toString(Semestre semestre) { return semestre != null ? semestre.getNome() : ""; }
            @Override public Semestre fromString(String string) { return null; }
        });

        cbProfessores.setConverter(new StringConverter<Professor>() {
            @Override public String toString(Professor professor) { return professor != null ? professor.getNome() : ""; }
            @Override public Professor fromString(String string) { return null; }
        });

        carregarCursos();
        carregarSemestres();
        carregarProfessores();

        cbProfessores.setOnAction(e -> montarGridSlots());
        btnSalvarTodas.setOnAction(e -> salvarIndisponibilidades());
    }

    private void carregarCursos() {
        try {
            List<Curso> cursos = cursoDAO.getAll();
            cursos.sort(Comparator.comparing(Curso::getNome));
            cbCursos.getItems().setAll(cursos);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erro ao carregar cursos: " + e.getMessage());
        }
    }

    private void carregarSemestres() {
        try {
            List<Semestre> semestres = semestreDAO.getAll();
            semestres.sort(Comparator.comparing(Semestre::getNome));
            cbSemestres.getItems().setAll(semestres);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erro ao carregar semestres: " + e.getMessage());
        }
    }

    private void carregarProfessores() {
        try {
            List<Professor> professores = professorDAO.getAllComMaterias();
            professores.sort(Comparator.comparing(Professor::getNome));
            cbProfessores.getItems().setAll(professores);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erro ao carregar professores: " + e.getMessage());
        }
    }

    private void montarGridSlots() {
        gridSlots.getChildren().clear();
        slotCheckBoxMap.clear();

        Professor professor = cbProfessores.getValue();
        if (professor == null) return;

        try {
            slots = slotDAO.getAll();
            List<Integer> indisponiveisIds = indisponibilidadeDAO.getSlotIdsPorProfessor(professor.getId());

            // Dias da semana (mantendo de Segunda a Sexta)
            List<String> dias = Arrays.asList("Segunda", "Terça", "Quarta", "Quinta", "Sexta");

            List<String> horarios = slots.stream()
                    .map(s -> s.getHora_inicio() + " - " + s.getHora_fim())
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            // Cabeçalhos
            for (int col = 0; col < dias.size(); col++) {
                Label diaLabel = new Label(dias.get(col));
                gridSlots.add(diaLabel, col + 1, 0);
            }
            for (int row = 0; row < horarios.size(); row++) {
                Label horaLabel = new Label(horarios.get(row));
                gridSlots.add(horaLabel, 0, row + 1);
            }

            // Preencher o grid com checkboxes (apenas de Segunda a Sexta)
            for (int row = 0; row < horarios.size(); row++) {
                String horario = horarios.get(row);
                for (int col = 0; col < dias.size(); col++) {
                    String dia = dias.get(col);
                    Optional<Slot> slot = slots.stream()
                            .filter(s -> s.getDia_semana().equals(dia) &&
                                    (s.getHora_inicio() + " - " + s.getHora_fim()).equals(horario))
                            .findFirst();

                    CheckBox cb = new CheckBox();
                    if (slot.isPresent()) {
                        if (indisponiveisIds.contains(slot.get().getId_slot())) {
                            cb.setSelected(true);
                        }
                        slotCheckBoxMap.put(slot.get(), cb);
                    } else {
                        cb.setDisable(true);
                    }
                    gridSlots.add(cb, col + 1, row + 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erro ao carregar slots: " + e.getMessage());
        }
    }

    private void salvarIndisponibilidades() {
        Professor professor = cbProfessores.getValue();
        if (professor == null) {
            showError("Selecione um professor!");
            return;
        }

        List<Integer> slotsMarcados = new ArrayList<>();
        for (Slot slot : slotCheckBoxMap.keySet()) {
            if (slotCheckBoxMap.get(slot).isSelected()) {
                slotsMarcados.add(slot.getId_slot());
            }
        }
        indisponibilidadeDAO.salvarSlotsIndisponiveis(professor.getId(), slotsMarcados);
        showInfo("Indisponibilidades salvas com sucesso!");
    }

    @FXML
    private void btnVoltarClick() {
        ScreenManager.changeScreen("/view/home-view.fxml", "/styles/customHome.css");
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}