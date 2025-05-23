package org.example.gestaodehorario.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.gestaodehorario.dao.IndisponibilidadeDAO;
import org.example.gestaodehorario.dao.ProfessorDAO;
import org.example.gestaodehorario.dao.SlotDAO;
import org.example.gestaodehorario.model.Professor;
import org.example.gestaodehorario.model.Slot;

import java.sql.SQLException;
import java.util.*;

public class IndisponibilidadeController {

    @FXML
    private ComboBox<String> cbCursos;
    @FXML
    private ComboBox<String> cbSemestres;
    @FXML
    private ComboBox<Professor> cbProfessores;
    @FXML
    private Button btnSalvarTodas;
    @FXML
    private GridPane gridSlots;

    private Map<Slot, CheckBox> slotCheckBoxMap = new HashMap<>();
    private List<Slot> slots = new ArrayList<>();

    @FXML
    public void initialize() {
        carregarCursos();
        carregarSemestres();
        carregarProfessores();

        cbProfessores.setOnAction(e -> montarGridSlots());
        btnSalvarTodas.setOnAction(e -> salvarIndisponibilidades());
    }

    private void carregarCursos() {
        cbCursos.getItems().clear();
        cbCursos.getItems().add("Banco de Dados (Noite)");
        cbCursos.getItems().add("Análise e Desenvolvimento de Sistemas");
    }

    private void carregarSemestres() {
        cbSemestres.getItems().clear();
        cbSemestres.getItems().addAll("1", "2", "3", "4");
    }

    private void carregarProfessores() {
        ProfessorDAO professorDAO = new ProfessorDAO();
        List<Professor> listaProf = new ArrayList<>();
        try {
            listaProf = professorDAO.getAllComMaterias(); // NOME REAL DO MÉTODO
        } catch (Exception e) {
            e.printStackTrace();
        }
        cbProfessores.getItems().clear();
        cbProfessores.getItems().addAll(listaProf);
    }

    private void montarGridSlots() {
        gridSlots.getChildren().clear();
        slotCheckBoxMap.clear();

        Professor professor = cbProfessores.getValue();
        if (professor == null) return;

        SlotDAO slotDAO = new SlotDAO();
        try {
            slots = slotDAO.getAll(); // NOME REAL DO MÉTODO
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        IndisponibilidadeDAO indisponibilidadeDAO = new IndisponibilidadeDAO();
        List<Integer> indisponiveisIds = indisponibilidadeDAO.getSlotIdsPorProfessor(professor.getId());

        // Descobrir todos os dias e horários únicos
        List<String> dias = new ArrayList<>();
        List<String> horarios = new ArrayList<>();
        for (Slot s : slots) {
            if (!dias.contains(s.getDia_semana())) dias.add(s.getDia_semana());
            String horaStr = s.getHora_inicio() + " - " + s.getHora_fim();
            if (!horarios.contains(horaStr)) horarios.add(horaStr);
        }

        // Cabeçalhos
        for (int col = 0; col < dias.size(); col++) {
            Label diaLabel = new Label(dias.get(col));
            gridSlots.add(diaLabel, col + 1, 0);
        }
        for (int row = 0; row < horarios.size(); row++) {
            Label horaLabel = new Label(horarios.get(row));
            gridSlots.add(horaLabel, 0, row + 1);
        }

        // Preencher o grid com checkboxes
        for (int row = 0; row < horarios.size(); row++) {
            String horario = horarios.get(row);
            for (int col = 0; col < dias.size(); col++) {
                String dia = dias.get(col);
                Slot slotAchado = null;
                for (Slot s : slots) {
                    String horaStr = s.getHora_inicio() + " - " + s.getHora_fim();
                    if (s.getDia_semana().equals(dia) && horaStr.equals(horario)) {
                        slotAchado = s;
                        break;
                    }
                }
                CheckBox cb = new CheckBox();
                if (slotAchado != null && indisponiveisIds.contains(slotAchado.getId_slot())) {
                    cb.setSelected(true);
                }
                if (slotAchado != null) {
                    slotCheckBoxMap.put(slotAchado, cb);
                } else {
                    cb.setDisable(true);
                }
                gridSlots.add(cb, col + 1, row + 1);
            }
        }
    }

    private void salvarIndisponibilidades() {
        Professor professor = cbProfessores.getValue();
        if (professor == null) return;

        IndisponibilidadeDAO indisponibilidadeDAO = new IndisponibilidadeDAO();
        List<Integer> slotsMarcados = new ArrayList<>();
        for (Slot slot : slotCheckBoxMap.keySet()) {
            if (slotCheckBoxMap.get(slot).isSelected()) {
                slotsMarcados.add(slot.getId_slot());
            }
        }
        indisponibilidadeDAO.salvarSlotsIndisponiveis(professor.getId(), slotsMarcados);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Indisponibilidades salvas com sucesso!");
        alert.showAndWait();
    }
}
