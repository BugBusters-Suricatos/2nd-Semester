package org.example.gestaodehorario.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import org.example.gestaodehorario.ScreenManager;
import org.kordamp.bootstrapfx.BootstrapFX;

public class TelaGerenciamentoMateriaController {

    @FXML
    private void btnVoltarClick(ActionEvent event) {
        ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
    }
}
