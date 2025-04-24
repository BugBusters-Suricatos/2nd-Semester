// RelatorioGradeController.java
package org.example.gestaodehorario.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.dao.AlocacaoDAO;
import org.example.gestaodehorario.model.RelatorioAlocacao;

import java.io.FileOutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;


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

    private void exportToPdf() {
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream("relatorio_horarios.pdf"));
            doc.open();
            Paragraph title = new Paragraph("Relatório de Horários",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            // Cabeçalhos
            for (String header : new String[]{"Curso","Semestre","Período","Dia","Horário","Matéria","Professor"}) {
                PdfPCell cell = new PdfPCell(new Paragraph(header,
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            // Linhas
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
            doc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnVoltarClick() {
        ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
    }
}