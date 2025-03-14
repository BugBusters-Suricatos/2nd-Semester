package org.example.gestaodehorario;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ScreenManager {
    private static Stage stage; // Stage principal da aplicação

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void changeScreen(String fxmlFile) {
        if (stage == null) {
            System.err.println("Erro: Stage principal não foi definido. Chame setStage() antes de changeScreen().");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource("/org/example/gestaodehorario/" + fxmlFile));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela: " + fxmlFile);
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Erro: Arquivo FXML '" + fxmlFile + "' não encontrado.");
        }
    }
}
