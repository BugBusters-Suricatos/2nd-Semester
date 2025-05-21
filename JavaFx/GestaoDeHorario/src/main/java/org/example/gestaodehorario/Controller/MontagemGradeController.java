package org.example.gestaodehorario.Controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    private final IndisponibilidadeDAO indisponibilidadeDAO = new IndisponibilidadeDAO();

    // Açúcar: guarda slots indisponíveis para cada professor (carrega uma vez por rebuild)
    private Map<Integer, Set<Integer>> indisponibilidadesPorProfessor = new HashMap<>();

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
                // Se o drag veio de um StackPane (ou seja, da grade)
                if (db.hasString() && evt.getGestureSource() instanceof StackPane spSrc) {
                    int id = Integer.parseInt(db.getString());
                    try {
                        // Encontra a Materia
                        Materia m = materiaDAO.getAll().stream()
                                .filter(x -> x.getIdMateria() == id)
                                .findFirst()
                                .orElse(null);
                        if (m != null) {
                            // 1) Reinsere UMA instância na lista
                            lvMaterias.getItems().add(m);
                            sortMaterias();

                            // 2) Remove o StackPane da célula de origem
                            Object origemObj = spSrc.getProperties().get("origem");
                            if (origemObj instanceof StackPane origemCell) {
                                origemCell.getChildren().remove(spSrc);
                            }

                            ok = true;
                        }
                    } catch (SQLException e) {
                        LOGGER.log(Level.SEVERE, "Erro ao devolver matéria", e);
                        showError("Erro ao devolver matéria:\n" + e.getMessage());
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

    // Açúcar: carregar indisponibilidades de todos os professores do sistema
    private void carregarIndisponibilidades() {
        indisponibilidadesPorProfessor.clear();
        try {
            // Aqui supõe que você tem um método getAll() no DAO ou equivalente
            List<Indisponibilidade> lista = indisponibilidadeDAO.getAll();
            for (Indisponibilidade ind : lista) {
                indisponibilidadesPorProfessor
                        .computeIfAbsent(ind.getIdProfessor(), k -> new HashSet<>())
                        .add(ind.getIdSlot());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar indisponibilidades", e);
        }
    }

    private void rebuildUI() {
        try {
            // Carrega as indisponibilidades antes de montar a grade
            carregarIndisponibilidades();

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

    // Açúcar: checa se professor está indisponível no slot ANTES de permitir alocação
    private void configureDropTarget(StackPane cell, String dia, String horaRange) {
        cell.setOnDragOver(evt -> {
            if (evt.getGestureSource() != cell
                    && evt.getDragboard().hasString()
                    && cell.getChildren().isEmpty()) {
                evt.acceptTransferModes(TransferMode.MOVE);
            }
            evt.consume();
        });

        cell.setOnDragDropped(evt -> {
            Dragboard db = evt.getDragboard();
            boolean sucesso = false;
            if (db.hasString()) {
                int idMat = Integer.parseInt(db.getString());
                try {
                    Materia m = materiaDAO.getAll().stream()
                            .filter(x -> x.getIdMateria() == idMat)
                            .findFirst().orElse(null);
                    if (m != null) {
                        // Descobre professor da matéria
                        Professor prof = professorDAO.getByMateria(m.getIdMateria())
                                .stream().findFirst().orElse(null);
                        if (prof == null) {
                            showError("Professor não encontrado para a matéria!");
                            evt.setDropCompleted(false);
                            evt.consume();
                            return;
                        }

                        // Descobre o slot correto (id) baseado em horário e dia
                        Curso c = cbCursos.getValue();
                        Periodo p = cbPeriodos.getValue();
                        Slot slot = slotDAO.getByHorarioDia(horaRange.split(" - ")[0].trim(), dia, p.getIdPeriodo())
                                .orElse(null);
                        if (slot == null) {
                            showError("Slot não encontrado!");
                            evt.setDropCompleted(false);
                            evt.consume();
                            return;
                        }

                        // Checa indisponibilidade desse professor nesse slot
                        Set<Integer> indisponiveis = indisponibilidadesPorProfessor.get(prof.getId());
                        if (indisponiveis != null && indisponiveis.contains(slot.getId_slot())) {
                            showError("Este professor está indisponível neste horário!");
                            evt.setDropCompleted(false);
                            evt.consume();
                            return;
                        }

                        Object src = evt.getGestureSource();

                        if (src instanceof ListCell<?>) {
                            StackPane novo = createMateriaNode(m, dia, horaRange);
                            cell.getChildren().add(novo);
                            List<Materia> lista = lvMaterias.getItems();
                            for (int i = 0; i < lista.size(); i++) {
                                if (lista.get(i).getIdMateria() == idMat) {
                                    lista.remove(i);
                                    break;
                                }
                            }

                        } else if (src instanceof StackPane spSrc) {
                            StackPane origem = (StackPane) spSrc.getProperties().get("origem");
                            if (origem != null) {
                                origem.getChildren().remove(spSrc);
                            }
                            cell.getChildren().add(spSrc);
                        }

                        sucesso = true;
                    }
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Erro no drop da grade", ex);
                    showError("Erro ao alocar matéria:\n" + ex.getMessage());
                }
            }
            evt.setDropCompleted(sucesso);
            evt.consume();
        });
    }

    private StackPane createMateriaNode(Materia m, String dia, String hr) {
        Rectangle bg = new Rectangle(100, 60);
        bg.setFill(Color.LIGHTBLUE);
        Label lbl = new Label(m.getNome());
        StackPane sp = new StackPane(bg, lbl);
        sp.setUserData(Map.of("materia", m, "dia", dia, "hora", hr));

        sp.setOnDragDetected(evt -> {
            StackPane origem = (StackPane) sp.getParent();
            sp.getProperties().put("origem", origem);

            Dragboard db = sp.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cc = new ClipboardContent();
            cc.putString(String.valueOf(m.getIdMateria()));
            db.setContent(cc);
            evt.consume();
        });

        sp.setOnDragDone(evt -> {
            if (evt.getTransferMode() == TransferMode.MOVE) {
                sp.getProperties().remove("origem");
            }
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
        ScreenManager.changeScreen("/view/home-view.fxml", "styles/customHome.css");
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
