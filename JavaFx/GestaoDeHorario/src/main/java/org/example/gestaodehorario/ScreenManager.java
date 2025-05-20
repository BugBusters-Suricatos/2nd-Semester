package org.example.gestaodehorario;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.gestaodehorario.util.AlertHelper;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.net.URL;

/**
 * Classe utilitária para gerenciamento de telas e navegação na aplicação JavaFX.
 * <p>
 * Responsável por:
 * <ul>
 *   <li>Configurar o palco (stage) principal</li>
 *   <li>Carregar telas FXML</li>
 *   <li>Aplicar estilos CSS</li>
 *   <li>Manter configurações consistentes de exibição</li>
 * </ul>
 * </p>
 */
public class ScreenManager {
    private static Stage primaryStage;

    /**
     * Configura o palco principal da aplicação
     * @param stage Palco JavaFX a ser configurado
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
        stage.setMaximized(true); // Tela cheia
        primaryStage.setTitle("Sistema de Gestão");
    }

    /**
     * Carrega uma nova tela substituindo a cena atual
     * @param fxmlPath Caminho relativo do arquivo FXML (a partir de resources/)
     * @param cssPath Caminho opcional para arquivo CSS personalizado (a partir de resources/)
     * @throws RuntimeException Se ocorrer erro no carregamento do FXML/CSS
     */
    public static void changeScreen(String fxmlPath, String cssPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Aplica CSS específico se fornecido
            if (cssPath != null && !cssPath.isEmpty()) {
                URL cssResource = ScreenManager.class.getResource(cssPath);
                if (cssResource != null) {
                    scene.getStylesheets().add(cssResource.toExternalForm());
                }
            }

            // Adiciona BootstrapFX como CSS global
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

            primaryStage.setScene(scene);
            primaryStage.setMaximized(true); // Mantém tela cheia
        } catch (IOException e) {
            AlertHelper.showError(
                    "Erro de Navegação",
                    "Falha ao carregar: " + fxmlPath + "\nErro: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }

}