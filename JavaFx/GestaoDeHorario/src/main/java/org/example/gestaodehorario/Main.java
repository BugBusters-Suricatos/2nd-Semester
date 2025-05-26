package org.example.gestaodehorario;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Carrega a tela inicial (login)
            Parent root = FXMLLoader.load(getClass().getResource("/view/login-view.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/customlogin.css").toExternalForm());

            // Define título e estilo da janela
            primaryStage.setTitle("Sistema de Gestão Acadêmica");
            primaryStage.initStyle(StageStyle.UNDECORATED); // Sem barra

            // Pega o tamanho da tela atual
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Seta o tamanho da janela para ocupar toda a tela
            primaryStage.setX(screenBounds.getMinX());
            primaryStage.setY(screenBounds.getMinY());
            primaryStage.setWidth(screenBounds.getWidth());
            primaryStage.setHeight(screenBounds.getHeight());

            // Impede redimensionamento e mostra a janela
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Registra o stage no ScreenManager
            ScreenManager.setPrimaryStage(primaryStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}