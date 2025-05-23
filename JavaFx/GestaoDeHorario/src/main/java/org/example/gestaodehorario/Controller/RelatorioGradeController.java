// RelatorioGradeController.java
package org.example.gestaodehorario.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.dao.AlocacaoDAO;
import org.example.gestaodehorario.model.RelatorioAlocacao;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;


import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.io.FileOutputStream;


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
            List<RelatorioAlocacao> list = alocDao.getRelatorioAll();
            data.setAll(list);
            tableRelatorio.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void exportToPdf() {
        // 1) Pergunta ao usuário onde salvar
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Salvar Relatório em PDF");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Arquivo PDF", "*.pdf"));
        chooser.setInitialFileName("relatorio_horarios.pdf");
        Window window = btnExportPdf.getScene().getWindow();
        File file = chooser.showSaveDialog(window);
        if (file == null) {
            return;  // cancelado
        }

        Document doc = new Document();
        try {
            // 2) Cria o FileOutputStream apenas para o PdfWriter
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();

            // título
            Paragraph title = new Paragraph(
                    "Relatório de Horários",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
            );
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph("\n"));

            // tabela
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            for (String h : new String[]{
                    "Curso","Semestre","Período","Dia","Horário","Matéria","Professor"
            }) {
                PdfPCell cell = new PdfPCell(new Paragraph(
                        h,
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)
                ));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            for (RelatorioAlocacao r : data) {
                table.addCell(r.getCurso());
                table.addCell(r.getSemestre());
                table.addCell(r.getPeriodo());
                table.addCell(r.getDia());
                table.addCell(r.getHorario());
                table.addCell(r.getMateria());
                table.addCell(r.getProfessor());
            }
            doc.add(table);

            showInfo("PDF salvo em:\n" + file.getAbsolutePath());
        } catch (Exception ex) {
            showError("Falha ao gerar PDF:\n" + ex.getMessage());
        } finally {
            // fecha o Document (e o OutputStream interno do writer)
            if (doc.isOpen()) {
                doc.close();
            }
        }
    }


    /** Utilitários para alertas */
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