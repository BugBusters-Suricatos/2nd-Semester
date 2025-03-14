package org.example.gestaodehorario;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.gestaodehorario.connect.DatabaseConnection;

import java.net.URL;
import java.sql.Connection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Configura o stage no ScreenManager
        ScreenManager.setStage(primaryStage);

        // Define o título da janela
        primaryStage.setTitle("Login - Sistema de Gestão de Horários");

        // Muda para a tela de login
        ScreenManager.changeScreen("login-view.fxml"); // Começa na tela de login

        // Verifica se o arquivo FXML foi carregado corretamente
        URL fxmlLocation = getClass().getResource("/org/example/gestaodehorario/login-view.fxml");
        if (fxmlLocation == null) {
            System.out.println("Arquivo FXML não encontrado!");
        } else {
            System.out.println("Carregando: " + fxmlLocation.toExternalForm());
        }

        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("Conexão bem-sucedida!");
        } else {
            System.out.println("Falha na conexão!");
        }
    }

    public static void main(String[] args) {
        launch(args); // Inicia a aplicação JavaFX


    }
}
