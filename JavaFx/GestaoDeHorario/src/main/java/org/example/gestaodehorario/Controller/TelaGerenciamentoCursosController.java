package org.example.gestaodehorario.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.gestaodehorario.ScreenManager;

public class TelaGerenciamentoCursosController {

    @FXML
    private TextField txtNomeCurso;

    @FXML
    private TextField txtCargaHoraria;

    @FXML
    private ListView<String> listViewCursos;

    @FXML
    private Button btnAdicionar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnRemover;

    // Método para adicionar um curso
    @FXML
    private void adicionarCurso() {
        String nomeCurso = txtNomeCurso.getText();
        String cargaHoraria = txtCargaHoraria.getText();

        if (!nomeCurso.isEmpty() && !cargaHoraria.isEmpty()) {
            listViewCursos.getItems().add(nomeCurso + " - " + cargaHoraria + " horas");
            limparCampos();
        } else {
            // Exibir um alerta de erro ou mensagem informando que os campos estão vazios
        }
    }

    // Método para editar o curso selecionado
    @FXML
    private void editarCurso() {
        String cursoSelecionado = listViewCursos.getSelectionModel().getSelectedItem();
        if (cursoSelecionado != null) {
            String nomeCurso = txtNomeCurso.getText();
            String cargaHoraria = txtCargaHoraria.getText();

            // Atualizar o curso na lista
            listViewCursos.getItems().set(listViewCursos.getSelectionModel().getSelectedIndex(),
                    nomeCurso + " - " + cargaHoraria + " horas");
            limparCampos();
        }
    }

    // Método para remover o curso selecionado
    @FXML
    private void removerCurso() {
        String cursoSelecionado = listViewCursos.getSelectionModel().getSelectedItem();
        if (cursoSelecionado != null) {
            listViewCursos.getItems().remove(cursoSelecionado);
        }
    }

    // Método para limpar os campos de texto
    private void limparCampos() {
        txtNomeCurso.clear();
        txtCargaHoraria.clear();
    }

    @FXML
    private void btnVoltarClick(ActionEvent event) {
        ScreenManager.changeScreen("home-view.fxml"); // Troca para a tela principal
    }
}
