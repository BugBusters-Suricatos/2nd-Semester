package org.example.gestaodehorario;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Classe principal que inicia a aplicação JavaFX e configura o ambiente inicial.
 * <p>
 * Responsável por:
 * <ul>
 *   <li>Inicializar o palco (stage) principal</li>
 *   <li>Configurar o tratamento de exceções não capturadas</li>
 *   <li>Gerenciar a navegação entre telas </li>
 * </ul>
 * </p>
 */
public class Main extends Application {


    @Override

    public void start(Stage primaryStage) {
        ScreenManager.setPrimaryStage(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        ScreenManager.changeScreen("/view/login-view.fxml", "/styles/customlogin.css");
    }
    /**
     * Ponto de entrada padrão para aplicações JavaFX k
     * @param args Argumentos de linha de comando
     */
    public static void main(String[] args) {
        launch(args);
    }
}