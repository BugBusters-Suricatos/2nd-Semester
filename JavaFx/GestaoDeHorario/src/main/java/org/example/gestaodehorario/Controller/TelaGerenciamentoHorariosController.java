package org.example.gestaodehorario.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.gestaodehorario.ScreenManager;

public class TelaGerenciamentoHorariosController {

    @FXML
    private void btnVoltarClick(ActionEvent event) {
        ScreenManager.changeScreen("home-view.fxml"); // Troca para a tela principal
    }
}
