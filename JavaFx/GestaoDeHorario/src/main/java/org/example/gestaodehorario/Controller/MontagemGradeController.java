// Arquivo: MontagemGradeController.java
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

    private void sortMaterias() {
        lvMaterias.getItems().sort(Comparator.comparing(m -> m.getNome().toLowerCase()));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ChangeListener<Object> reloadL = (obs, o, n) -> rebuildUI();
        try {
            cbPeriodos.setConverter(new StringConverter<Periodo>() {
                @Override public String toString(Periodo p) { return p != null ? p.getNome() : ""; }
                @Override public Periodo fromString(String s) { return null; }
            });
            cbPeriodos.getItems().setAll(periodoDAO.getAll());
            cbPeriodos.valueProperty().addListener(reloadL);

            cbCursos.getItems().setAll(cursoDAO.getAll());
            cbSemestres.getItems().setAll(semestreDAO.getAll());
            cbCursos.valueProperty().addListener(reloadL);
            cbSemestres.valueProperty().addListener(reloadL);

            lvMaterias.setCellFactory(lv -> {
                ListCell<Materia> cell = new ListCell<>() {
                    @Override protected void updateItem(Materia m, boolean empty) {
                        super.updateItem(m, empty);
                        setText(empty || m == null ? "" : m.getNome());
                    }
                };
                cell.setOnDragDetected(evt -> {
                    Materia m = cell.getItem();
                    if (m == null) return;
                    Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString(String.valueOf(m.getIdMateria()));
                    db.setContent(cc);
                    evt.consume();
                });
                return cell;
            });

            lvMaterias.setOnDragOver(evt -> {
                if (evt.getGestureSource() != lvMaterias && evt.getDragboard().hasString()) {
                    evt.acceptTransferModes(TransferMode.MOVE);
                }
                evt.consume();
            });

            lvMaterias.setOnDragDropped(evt -> {
                Dragboard db = evt.getDragboard();
                boolean ok = false;
                if (db.hasString() && evt.getGestureSource() instanceof StackPane) {
                    int id = Integer.parseInt(db.getString());
                    try {
                        Materia m = materiaDAO.getAll().stream()
                                .filter(x -> x.getIdMateria() == id)
                                .findFirst().orElse(null);
                        if (m != null) {
                            lvMaterias.getItems().add(m);
                            sortMaterias();
                            ok = true;
                        }
                    } catch (SQLException e) {
                        LOGGER.log(Level.SEVERE, "Erro ao devolver matéria", e);
                        showError("Erro: " + e.getMessage());
                    }
                }
                evt.setDropCompleted(ok);
                evt.consume();
            });

            rebuildUI();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro init", e);
            showError("Erro init: " + e.getMessage());
        }
    }

    private void rebuildUI() {
        try {
            gradeAlocacao.getChildren().clear();
            lvMaterias.getItems().clear();

            Curso c = cbCursos.getValue();
            Semestre s = cbSemestres.getValue();
            Periodo p = cbPeriodos.getValue();
            if (c == null || s == null || p == null) return;

            List<Materia> materias = materiaDAO.getAll().stream()
                    .filter(m -> m.getCurso().getIdCurso() == c.getIdCurso())
                    .flatMap(m -> Collections.nCopies(m.getCargaHoraria(), m).stream())
                    .collect(Collectors.toList());

            Set<Integer> jaAlocadas = alocacaoDAO.getBySemestre(s.getIdSemestre()).stream()
                    .filter(a -> a.getSlot().getIdPeriodo() == p.getIdPeriodo())
                    .filter(a -> a.getMateria().getCurso().getIdCurso() == c.getIdCurso())
                    .map(a -> a.getMateria().getIdMateria())
                    .collect(Collectors.toSet());

            materias.removeIf(m -> jaAlocadas.contains(m.getIdMateria()));
            lvMaterias.getItems().setAll(materias);
            sortMaterias();

            List<Slot> slots = slotDAO.getByCurso(c.getIdCurso()).stream()
                    .filter(sl -> sl.getIdPeriodo() == p.getIdPeriodo())
                    .collect(Collectors.toList());

            List<String> dias = Arrays.asList("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado");
            List<String> horas = slots.stream()
                    .map(sl -> sl.getHora_inicio() + " - " + sl.getHora_fim())
                    .distinct().sorted().collect(Collectors.toList());

            for (int i = 0; i < dias.size(); i++) {
                Label l = new Label(dias.get(i));
                GridPane.setHalignment(l, HPos.CENTER);
                GridPane.setValignment(l, VPos.CENTER);
                gradeAlocacao.add(l, i + 1, 0);
            }
            for (int i = 0; i < horas.size(); i++) {
                Label l = new Label(horas.get(i));
                GridPane.setHalignment(l, HPos.CENTER);
                GridPane.setValignment(l, VPos.CENTER);
                gradeAlocacao.add(l, 0, i + 1);
            }

            for (int row = 1; row <= horas.size(); row++) {
                for (int col = 1; col <= dias.size(); col++) {
                    String dia = dias.get(col - 1);
                    String hora = horas.get(row - 1);
                    StackPane cell = new StackPane();
                    cell.setPrefSize(120, 80);
                    configureDropTarget(cell, dia, hora);
                    gradeAlocacao.add(cell, col, row);
                }
            }

            for (Alocacao a : alocacaoDAO.getBySemestre(s.getIdSemestre()).stream()
                    .filter(a -> a.getMateria().getCurso().getIdCurso() == c.getIdCurso())
                    .filter(a -> a.getSlot().getIdPeriodo() == p.getIdPeriodo())
                    .collect(Collectors.toList())) {

                String range = a.getSlot().getHora_inicio() + " - " + a.getSlot().getHora_fim();
                String dia = a.getSlot().getDia_semana();
                int row = horas.indexOf(range) + 1;
                int col = dias.indexOf(dia) + 1;
                StackPane sp = createMateriaNode(a.getMateria(), dia, range);

                for (Node node : gradeAlocacao.getChildren()) {
                    Integer rr = GridPane.getRowIndex(node);
                    Integer cc = GridPane.getColumnIndex(node);
                    if (rr != null && cc != null && rr == row && cc == col && node instanceof StackPane st) {
                        st.getChildren().add(sp);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro rebuild", e);
            showError("Erro rebuild: " + e.getMessage());
        }
    }

    private void configureDropTarget(StackPane cell, String dia, String horaRange) {
        cell.setOnDragOver(evt -> {
            if (evt.getGestureSource() != cell && evt.getDragboard().hasString())
                evt.acceptTransferModes(TransferMode.MOVE);
            evt.consume();
        });

        cell.setOnDragDropped(evt -> {
            Dragboard db = evt.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                int id = Integer.parseInt(db.getString());
                try {
                    Materia m = materiaDAO.getAll().stream()
                            .filter(x -> x.getIdMateria() == id)
                            .findFirst().orElse(null);
                    if (m != null) {
                        StackPane sp = createMateriaNode(m, dia, horaRange);
                        cell.getChildren().add(sp);

                        for (int i = 0; i < lvMaterias.getItems().size(); i++) {
                            if (lvMaterias.getItems().get(i).getIdMateria() == id) {
                                lvMaterias.getItems().remove(i);
                                break;
                            }
                        }
                        success = true;
                    }
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erro no drop da grade", e);
                    showError("Erro: " + e.getMessage());
                }
            }
            evt.setDropCompleted(success);
            evt.consume();
        });
    }

    private StackPane createMateriaNode(Materia m, String dia, String hr) {
        Rectangle r = new Rectangle(100, 60);
        r.setFill(Color.LIGHTBLUE);
        Label lbl = new Label(m.getNome());
        StackPane sp = new StackPane(r, lbl);
        sp.setUserData(Map.of("materia", m, "dia", dia, "hora", hr));

        sp.setOnDragDetected(evt -> {
            Dragboard db = sp.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cc = new ClipboardContent();
            cc.putString(String.valueOf(m.getIdMateria()));
            db.setContent(cc);
            ((StackPane) sp.getParent()).getChildren().remove(sp);
            evt.consume();
        });
        return sp;
    }

    @FXML private void salvarAlocacoes() {
        try {
            Curso c = cbCursos.getValue();
            Semestre s = cbSemestres.getValue();
            Periodo p = cbPeriodos.getValue();
            if (c == null || s == null || p == null) {
                showError("Selecione curso, semestre e período!");
                return;
            }
            List<Alocacao> lista = new ArrayList<>();
            for (Node node : gradeAlocacao.getChildren()) {
                if (node instanceof StackPane cell) {
                    for (Node ch : cell.getChildren()) {
                        if (ch instanceof StackPane sp) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> dt = (Map<String, Object>) sp.getUserData();
                            Materia m = (Materia) dt.get("materia");
                            String dia = (String) dt.get("dia");
                            String hi = ((String) dt.get("hora")).split(" - ")[0].trim();
                            Professor pof = professorDAO.getByMateria(m.getIdMateria())
                                    .stream().findFirst()
                                    .orElseThrow(() -> new Exception("Professor não encontrado"));
                            Slot sld = slotDAO.getByHorarioDia(hi, dia, p.getIdPeriodo())
                                    .orElseThrow(() -> new Exception("Slot não encontrado"));
                            lista.add(new Alocacao(pof, m, sld, s));
                        }
                    }
                }
            }
            alocacaoDAO.salvar(lista);
            showInfo(lista.size() + " alocações salvas com sucesso!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro save", e);
            showError("Erro save: " + e.getMessage());
        }
    }

    @FXML private void btnVoltarClick() {
        ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}