package org.example.gestaodehorario.Controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.dao.*;
import org.example.gestaodehorario.model.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MontagemGradeController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(MontagemGradeController.class.getName());

    @FXML private ComboBox<Curso> cbCursos;
    @FXML private ComboBox<Semestre> cbSemestres;
    @FXML private ComboBox<Periodo> cbPeriodos;
    @FXML private ListView<Materia> lvMaterias;
    @FXML private GridPane gradeAlocacao;

    private final CursoDAO cursoDAO = new CursoDAO();
    private final SemestreDAO semestreDAO = new SemestreDAO();
    private final PeriodoDAO periodoDAO = new PeriodoDAO();
    private final MateriaDAO materiaDAO = new MateriaDAO();
    private final SlotDAO slotDAO = new SlotDAO();
    private final AlocacaoDAO alocacaoDAO = new AlocacaoDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChangeListener<Object> reloadListener = (obs, oldVal, newVal) -> rebuildUI();
        try {
            // Configura conversores e itens dos combo boxes
            if (cbPeriodos != null) {
                cbPeriodos.setConverter(new StringConverter<Periodo>() {
                    @Override public String toString(Periodo p) { return p != null ? p.getNome() : ""; }
                    @Override public Periodo fromString(String s) { return null; }
                });
                cbPeriodos.getItems().setAll(periodoDAO.getAll());
                cbPeriodos.valueProperty().addListener(reloadListener);
            }
            cbCursos.getItems().setAll(cursoDAO.getAll());
            cbSemestres.getItems().setAll(semestreDAO.getAll());
            cbCursos.valueProperty().addListener(reloadListener);
            cbSemestres.valueProperty().addListener(reloadListener);

            lvMaterias.setCellFactory(lv -> new ListCell<>() {
                @Override protected void updateItem(Materia m, boolean empty) {
                    super.updateItem(m, empty);
                    setText(empty || m == null ? "" : m.getNome());
                }
            });

            rebuildUI();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao inicializar", e);
            showError("Erro ao inicializar: " + e.getMessage());
        }
    }

    private void rebuildUI() {
        try {
            // Limpa grade e paleta
            gradeAlocacao.getChildren().clear();
            lvMaterias.getItems().clear();
            // Popula apenas se todos selecionados
            Curso curso = cbCursos.getValue();
            Semestre sem = cbSemestres.getValue();
            Periodo per = cbPeriodos.getValue();
            if (curso == null || sem == null || per == null) {
                return;
            }
            updateMaterias();
            buildGrid();
            configureDragFromPalette();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao reconstruir UI", e);
            showError("Erro ao reconstruir UI: " + e.getMessage());
        }
    }

    private void updateMaterias() throws SQLException {
        Curso curso = cbCursos.getValue();
        Semestre sem = cbSemestres.getValue();
        Periodo per = cbPeriodos.getValue();

        // 1) Monta lista “bruta” com cada matéria repetida conforme carga horária
        List<Materia> list = new ArrayList<>();
        for (Materia m : materiaDAO.getAll()) {
            if (m.getCurso().getIdCurso() == curso.getIdCurso()) {
                for (int i = 0; i < m.getCargaHoraria(); i++) {
                    list.add(m);
                }
            }
        }

        // 2) Pega IDs das matérias já salvas para este semestre/período
        Set<Integer> savedIds = alocacaoDAO.getBySemestre(sem.getIdSemestre()).stream()
                .filter(a -> a.getSlot().getIdPeriodo() == per.getIdPeriodo())
                .filter(a -> a.getMateria().getCurso().getIdCurso() == curso.getIdCurso())
                .map(a -> a.getMateria().getIdMateria())
                .collect(Collectors.toSet());

        // 3) Remove **todas** as instâncias cujo ID está em savedIds
        list.removeIf(m -> savedIds.contains(m.getIdMateria()));

        // 4) Atualiza a ListView
        lvMaterias.getItems().setAll(list);
    }

    private void buildGrid() {
        try {
            Curso curso = cbCursos.getValue();
            Semestre sem = cbSemestres.getValue();
            Periodo per = cbPeriodos.getValue();
            List<Slot> slots = slotDAO.getByCurso(curso.getIdCurso()).stream()
                    .filter(s -> s.getIdPeriodo() == per.getIdPeriodo())
                    .collect(Collectors.toList());

            List<String> dias = Arrays.asList("Segunda","Terça","Quarta","Quinta","Sexta","Sábado");
            List<String> horarios = slots.stream()
                    .map(s -> s.getHora_inicio() + " - " + s.getHora_fim())
                    .distinct().sorted().toList();

            // Cabeçalhos de dias e horários
            for (int i = 0; i < dias.size(); i++) {
                Label l = new Label(dias.get(i));
                GridPane.setHalignment(l, HPos.CENTER);
                GridPane.setValignment(l, VPos.CENTER);
                gradeAlocacao.add(l, i + 1, 0);
            }
            for (int i = 0; i < horarios.size(); i++) {
                Label l = new Label(horarios.get(i));
                GridPane.setHalignment(l, HPos.CENTER);
                GridPane.setValignment(l, VPos.CENTER);
                gradeAlocacao.add(l, 0, i + 1);
            }

            // Células de alocação
            for (int r = 1; r <= horarios.size(); r++) {
                for (int c = 1; c <= dias.size(); c++) {
                    StackPane cell = new StackPane();
                    cell.setPrefSize(120, 80);
                    configureDropTarget(cell, dias.get(c - 1), horarios.get(r - 1));
                    gradeAlocacao.add(cell, c, r);
                }
            }

            loadSavedAlocacoes(curso, sem, per, slots, dias, horarios);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao construir grade", e);
            showError("Erro ao construir grade: " + e.getMessage());
        }
    }

    private void loadSavedAlocacoes(Curso curso, Semestre sem, Periodo per,
                                    List<Slot> slots, List<String> dias, List<String> horarios) throws SQLException {
        Set<Integer> slotIds = new HashSet<>();
        slots.forEach(s -> slotIds.add(s.getId_slot()));

        // Filtra alocações apenas do curso e período atuais
        List<Alocacao> alocs = alocacaoDAO.getBySemestre(sem.getIdSemestre()).stream()
                .filter(a -> a.getMateria().getCurso().getIdCurso() == curso.getIdCurso())
                .filter(a -> a.getSlot().getIdPeriodo() == per.getIdPeriodo())
                .collect(Collectors.toList());

        for (Alocacao a : alocs) {
            Slot s = a.getSlot();
            String range = s.getHora_inicio() + " - " + s.getHora_fim();
            Materia m = a.getMateria();
            StackPane sp = createMateriaNode(m, s.getDia_semana(), range);
            int row = horarios.indexOf(range) + 1;
            int col = dias.indexOf(s.getDia_semana()) + 1;
            for (Node node : gradeAlocacao.getChildren()) {
                Integer r = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);
                if (r != null && c != null && r == row && c == col && node instanceof StackPane cell) {
                    cell.getChildren().add(sp);
                    break;
                }
            }
        }
    }

    private void configureDragFromPalette() {
        lvMaterias.setOnDragDetected(evt -> {
            Materia m = lvMaterias.getSelectionModel().getSelectedItem();
            if (m == null) return;
            Dragboard db = lvMaterias.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cc = new ClipboardContent();
            cc.putString(String.valueOf(m.getIdMateria()));
            db.setContent(cc);
            /* Remove imediatamente da paleta ao arrastar */
            lvMaterias.getItems().remove(m);
            evt.consume();
        });
    }

    private void configureDropTarget(StackPane cell, String dia, String horaRange) {
        cell.setOnDragOver(evt -> {
            if (evt.getGestureSource() != cell && evt.getDragboard().hasString()) {
                evt.acceptTransferModes(TransferMode.MOVE);
            }
            evt.consume();
        });
        cell.setOnDragDropped(evt -> {
            Dragboard db = evt.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                int id = Integer.parseInt(db.getString());
                Materia m = null;
                try {
                    for (Materia m2 : materiaDAO.getAll()) {
                        if (m2.getIdMateria() == id) {
                            m = m2;
                            break;
                        }
                    }
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erro ao carregar matéria durante drop", e);
                    showError("Erro ao recuperar matéria: " + e.getMessage());
                }

                if (m != null) {
                    StackPane sp = createMateriaNode(m, dia, horaRange);
                    cell.getChildren().add(sp);
                    success = true;
                }
            }
            evt.setDropCompleted(success);
            evt.consume();
        });
    }

    private StackPane createMateriaNode(Materia m, String dia, String hora) {
        Rectangle rect = new Rectangle(100, 60);
        rect.setFill(Color.LIGHTBLUE);
        Label lbl = new Label(m.getNome());
        StackPane sp = new StackPane(rect, lbl);
        sp.setUserData(Map.of("materia", m, "dia", dia, "hora", hora));
        sp.setOnDragDetected(evt -> {
            Dragboard db = sp.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cc = new ClipboardContent();
            cc.putString(String.valueOf(m.getIdMateria()));
            db.setContent(cc);
            /* Remove do grid ao iniciar arraste */
            ((StackPane) sp.getParent()).getChildren().remove(sp);
            evt.consume();
        });
        return sp;
    }

    @FXML
    private void salvarAlocacoes() {
        try {
            Curso curso = cbCursos.getValue();
            Semestre sem = cbSemestres.getValue();
            Periodo per = cbPeriodos.getValue();
            if (curso == null || sem == null || per == null) {
                showError("Selecione curso, semestre e período!");
                return;
            }
            List<Alocacao> lista = new ArrayList<>();
            for (Node node : gradeAlocacao.getChildren()) {
                if (node instanceof StackPane cell) {
                    for (Node child : cell.getChildren()) {
                        if (child instanceof StackPane sp) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> data = (Map<String, Object>) sp.getUserData();
                            Materia m = (Materia) data.get("materia");
                            String dia = (String) data.get("dia");
                            String hi = ((String) data.get("hora")).split(" - ")[0].trim();
                            Professor p = professorDAO.getByMateria(m.getIdMateria()).stream()
                                    .findFirst().orElseThrow(() -> new Exception("Professor não encontrado"));
                            Slot s = slotDAO.getByHorarioDia(hi, dia, per.getIdPeriodo())
                                    .orElseThrow(() -> new Exception("Slot não encontrado"));
                            lista.add(new Alocacao(p, m, s, sem));
                        }
                    }
                }
            }
            alocacaoDAO.salvar(lista);
            showInfo(lista.size() + " alocações salvas com sucesso!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar alocações", e);
            showError("Erro: " + e.getMessage());
        }
    }

    @FXML
    private void btnVoltarClick() {
        ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}