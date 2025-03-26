package org.example.gestaodehorario;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.gestaodehorario.util.AlertHelper;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.net.URL;

public class ScreenManager {
    private static Stage primaryStage;

    // Configurar estágio principal
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
        stage.setMaximized(true); // Tela cheia
        primaryStage.setTitle("Sistema de Gestão");
    }

    // Método para trocar telas
    public static void changeScreen(String fxmlPath, String cssPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource("/" + fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Aplicar CSS específico
            if (cssPath != null && !cssPath.isEmpty()) {
                URL cssResource = ScreenManager.class.getResource("/" + cssPath);
                if (cssResource != null) {
                    scene.getStylesheets().add(cssResource.toExternalForm());
                }
            }

            // CSS global (BootstrapFX)
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
        } catch (IOException e) {
            AlertHelper.showError(
                    "Erro de Navegação",
                    "Falha ao carregar: " + fxmlPath + "\nErro: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }
}
