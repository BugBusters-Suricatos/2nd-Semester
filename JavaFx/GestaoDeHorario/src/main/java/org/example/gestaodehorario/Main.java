package org.example.gestaodehorario;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Classe principal que inicia a aplicação JavaFX e configura o ambiente inicial.
 * <p>
 * Responsável por:
 * <ul>
 *   <li>Inicializar o palco (stage) principal</li>
 *   <li>Configurar o tratamento de exceções não capturadas</li>
 *   <li>Gerenciar a navegação entre telas</li>
 * </ul>
 * </p>
 */
public class Main extends Application {

    /**
     * Método de inicialização da aplicação JavaFX
     * @param stage Palco principal fornecido pelo JavaFX
     */
    @Override
    public void start(Stage stage) {
        // Configuração de exemplo para tratamento global de erros (desativado)
        /*
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Crítico");
            alert.setHeaderText("Ocorreu um erro inesperado");
            alert.setContentText(throwable.getMessage());
            alert.showAndWait();
        });
        */

        // Configuração inicial da interface
        ScreenManager.setPrimaryStage(stage);
        ScreenManager.changeScreen("view/login-view.fxml", "styles/customlogin.css");
        stage.show();
    }

    /**
     * Ponto de entrada padrão para aplicações JavaFX
     * @param args Argumentos de linha de comando
     */
    public static void main(String[] args) {
        launch(args);
    }
}