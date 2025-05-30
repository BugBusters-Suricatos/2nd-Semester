package org.example.gestaodehorario.Controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.dao.AlocacaoDAO;
import org.example.gestaodehorario.model.RelatorioAlocacao;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioGradeController implements Initializable {

    @FXML private TableView<RelatorioAlocacao> tableRelatorio;
    @FXML private TableColumn<RelatorioAlocacao, String> colCurso;
    @FXML private TableColumn<RelatorioAlocacao, String> colSemestre;
    @FXML private TableColumn<RelatorioAlocacao, String> colPeriodo;
    @FXML private TableColumn<RelatorioAlocacao, String> colDia;
    @FXML private TableColumn<RelatorioAlocacao, String> colHorario;
    @FXML private TableColumn<RelatorioAlocacao, String> colMateria;
    @FXML private TableColumn<RelatorioAlocacao, String> colProfessor;
    @FXML private Button btnExportPdf;

    private final AlocacaoDAO alocDao = new AlocacaoDAO();
    private final ObservableList<RelatorioAlocacao> data = FXCollections.observableArrayList();

    // --- CONSTANTES CORRIGIDAS DE ACORDO COM SEU DEBUG ---
    private static final List<String> DIAS_SEMANA = Arrays.asList(
            "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"
    );

    // ATENÇÃO: Verifique se os horários da manhã também estão corretos no seu BD.
    private static final List<String> HORARIOS_MANHA = Arrays.asList(
            "07:30 - 08:20", "08:30 - 09:20", "09:30 - 10:20", "10:30 - 11:20", "11:30 - 12:20"
    );

    // Lista de horários da noite corrigida conforme a saída do seu console.
    private static final List<String> HORARIOS_NOITE = Arrays.asList(
            "18:45 - 19:35", "19:45 - 20:35", "20:45 - 21:35", "21:45 - 22:35"
    );


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCurso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));
        colPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        colDia.setCellValueFactory(new PropertyValueFactory<>("dia"));
        colHorario.setCellValueFactory(new PropertyValueFactory<>("horario"));
        colMateria.setCellValueFactory(new PropertyValueFactory<>("materia"));
        colProfessor.setCellValueFactory(new PropertyValueFactory<>("professor"));

        loadData();
        btnExportPdf.setOnAction(e -> exportToPdf());
    }

    private void loadData() {
        try {
            data.setAll(alocDao.getRelatorioAll());
            tableRelatorio.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exportToPdf() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Salvar Relatório em PDF");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo PDF", "*.pdf"));
        chooser.setInitialFileName("relatorio_grade_horaria.pdf");
        Window window = btnExportPdf.getScene().getWindow();
        File file = chooser.showSaveDialog(window);

        if (file == null) return;

        Document doc = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();

            Font FONT_TITULO_CURSO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font FONT_TITULO_SEMESTRE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font FONT_CABECALHO_TABELA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
            Font FONT_CONTEUDO_TABELA = FontFactory.getFont(FontFactory.HELVETICA, 8);
            Font FONT_HORARIO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);

            Map<String, List<RelatorioAlocacao>> dadosPorCurso = data.stream()
                    .collect(Collectors.groupingBy(RelatorioAlocacao::getCurso));
            List<String> cursosOrdenados = dadosPorCurso.keySet().stream().sorted().collect(Collectors.toList());

            for (String nomeCurso : cursosOrdenados) {
                doc.newPage();
                Paragraph tituloCurso = new Paragraph("Curso: " + nomeCurso, FONT_TITULO_CURSO);
                tituloCurso.setAlignment(Element.ALIGN_CENTER);
                tituloCurso.setSpacingAfter(15f);
                doc.add(tituloCurso);

                Map<String, List<RelatorioAlocacao>> dadosPorSemestre = dadosPorCurso.get(nomeCurso).stream()
                        .collect(Collectors.groupingBy(RelatorioAlocacao::getSemestre));
                List<String> semestresOrdenados = dadosPorSemestre.keySet().stream()
                        .sorted(Comparator.comparingInt(s -> Integer.parseInt(s.replaceAll("[^0-9]", ""))))
                        .collect(Collectors.toList());

                for (String numeroSemestre : semestresOrdenados) {
                    List<RelatorioAlocacao> alocacoesDoSemestre = dadosPorSemestre.get(numeroSemestre);
                    if (alocacoesDoSemestre == null || alocacoesDoSemestre.isEmpty()) continue;

                    String periodo = alocacoesDoSemestre.get(0).getPeriodo();
                    List<String> horariosDoPeriodo = "Matutino".equalsIgnoreCase(periodo) ? HORARIOS_MANHA : HORARIOS_NOITE;

                    Paragraph tituloSemestre = new Paragraph(numeroSemestre + "º Semestre (" + periodo + ")", FONT_TITULO_SEMESTRE);
                    doc.add(tituloSemestre);

                    PdfPTable grade = new PdfPTable(DIAS_SEMANA.size() + 1);
                    grade.setWidthPercentage(100);
                    grade.setSpacingBefore(5f);
                    grade.setSpacingAfter(15f);
                    grade.addCell(new PdfPCell(new Phrase(""))).setBorder(Rectangle.NO_BORDER);

                    for (String dia : DIAS_SEMANA) {
                        PdfPCell headerCell = new PdfPCell(new Phrase(dia, FONT_CABECALHO_TABELA));
                        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        grade.addCell(headerCell);
                    }

                    Map<String, Map<String, String>> gradeDoSemestre = new HashMap<>();
                    for (RelatorioAlocacao alocacao : alocacoesDoSemestre) {
                        if (alocacao.getDia() != null && alocacao.getHorario() != null) {
                            gradeDoSemestre
                                    .computeIfAbsent(alocacao.getDia().trim(), k -> new HashMap<>())
                                    .put(alocacao.getHorario().trim(), alocacao.getMateria() + "\n" + alocacao.getProfessor());
                        }
                    }

                    for (String horario : horariosDoPeriodo) {
                        PdfPCell horarioCell = new PdfPCell(new Phrase(horario, FONT_HORARIO));
                        horarioCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        horarioCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        grade.addCell(horarioCell);

                        for (String dia : DIAS_SEMANA) {
                            String conteudo = gradeDoSemestre.getOrDefault(dia, Collections.emptyMap()).get(horario);
                            PdfPCell aulaCell = new PdfPCell(new Phrase(conteudo != null ? conteudo : "", FONT_CONTEUDO_TABELA));
                            aulaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            aulaCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            aulaCell.setMinimumHeight(40f);
                            grade.addCell(aulaCell);
                        }
                    }
                    doc.add(grade);
                }
            }

            showInfo("PDF salvo com sucesso em:\n" + file.getAbsolutePath());
        } catch (Exception ex) {
            showError("Falha ao gerar PDF:\n" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (doc.isOpen()) {
                doc.close();
            }
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    @FXML
    private void btnVoltarClick() {
        ScreenManager.changeScreen("/view/home-view.fxml", "styles/customHome.css");
    }
}