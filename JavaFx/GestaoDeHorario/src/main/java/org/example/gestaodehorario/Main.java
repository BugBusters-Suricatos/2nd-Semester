package org.example.gestaodehorario;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Alerta de Erro Geral
//        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Erro Crítico");
//            alert.setHeaderText("Ocorreu um erro inesperado");
//            alert.setContentText(throwable.getMessage());
//            alert.showAndWait();
//        });

        ScreenManager.setPrimaryStage(stage);
        ScreenManager.changeScreen("view/login-view.fxml", "styles/customlogin.css");
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}