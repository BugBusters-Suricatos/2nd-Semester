package org.example.gestaodehorario.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

/**
 * Classe utilitária para exibição de alertas na interface gráfica JavaFX.
 * Oferece métodos simplificados para mostrar diferentes tipos de diálogos:
 * <ul>
 * <li>Informacionais</li>
 * <li>Erros</li>
 * <li>Confirmações</li>
 * </ul>
 */
public class AlertHelper {

    /**
     * Exibe um alerta de informação
     * @param title Título da janela de diálogo
     * @param message Mensagem principal do alerta
     */
    public static void showInfo(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, null, message, null);
    }

    /**
     * Exibe um alerta de erro com título padrão "Erro"
     * @param message Mensagem de erro a ser exibida
     */
    public static void showError(String message) {
        showAlert(Alert.AlertType.ERROR, "Erro", null, message, null);
    }

    /**
     * Exibe um alerta de erro personalizado
     * @param title Título customizado da janela
     * @param message Mensagem de erro detalhada
     */
    public static void showError(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, null, message, null);
    }

    /**
     * Exibe um diálogo de confirmação com botões Sim/Não
     * @param title Título da janela de confirmação
     * @param message Mensagem de confirmação
     * @return true se o usuário selecionou Sim, false caso contrário
     */
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

    /**
     * Método genérico para exibição de alertas
     * @param type Tipo de alerta (ERROR, INFORMATION, etc)
     * @param title Título da janela
     * @param header Texto do cabeçalho (opcional)
     * @param content Mensagem principal
     * @param owner Janela pai (opcional)
     */
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