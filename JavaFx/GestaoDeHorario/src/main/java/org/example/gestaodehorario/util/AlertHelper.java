package org.example.gestaodehorario.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

public class AlertHelper {

    // Método para alertas de informação
    public static void showInfo(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, null, message, null);
    }

    // Método para alertas de erro (sobrecarregado)
    public static void showError(String message) {
        showAlert(Alert.AlertType.ERROR, "Erro", null, message, null);
    }

    public static void showError(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, null, message, null);
    }

    // Método para confirmação
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                message,
                ButtonType.YES,
                ButtonType.NO
        );
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    // Método genérico
    private static void showAlert(
            Alert.AlertType type,
            String title,
            String header,
            String content,
            Window owner
    ) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initOwner(owner);
        alert.showAndWait();
    }
}