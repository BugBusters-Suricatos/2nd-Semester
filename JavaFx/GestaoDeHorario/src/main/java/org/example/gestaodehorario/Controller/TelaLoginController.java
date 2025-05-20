package org.example.gestaodehorario.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.connect.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controlador JavaFX responsável pela tela de login do sistema.
 * <p>
 * Gerencia a autenticação de usuário e senha e navega para a tela principal em caso de sucesso.
 * </p>
 */
public class TelaLoginController {

    /** Campo de texto para entrada do e-mail/usuário. */
    @FXML private TextField txtUsuario;

    /** Campo de texto para entrada da senha. */
    @FXML private PasswordField txtSenha;

    /**
     * Handler para o evento de clique no botão de login.
     * <p>
     * Valida campos obrigatórios, tenta autenticar e navega para a tela inicial ou exibe erro.
     * </p>
     *
     * @param event evento de ação gerado pelo clique no botão de login
     */
    @FXML
    private void LoginClick(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String senha = txtSenha.getText().trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            mostrarErro("Campos obrigatórios!", "Preencha usuário e senha.");
            return;
        }

        try {
            if (autenticar(usuario, senha)) {
                ScreenManager.changeScreen("/view/home-view.fxml", "/styles/customHome.css");
            } else {
                mostrarErro("Falha no login", "Credenciais inválidas.");
            }
        } catch (SQLException e) {
            mostrarErro("Erro de Banco de Dados", "Não foi possível conectar ao sistema.");
            e.printStackTrace();
        }
    }

    /**
     * Realiza a autenticação do usuário no banco de dados.
     *
     * @param email usuário (e-mail) informado
     * @param senha senha informada
     * @return true se as credenciais forem válidas; false caso contrário
     * @throws SQLException se ocorrer erro ao acessar o banco de dados
     */
    private boolean autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT id_coordenador FROM Coordenador WHERE email = ? AND senha = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, senha);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Retorna true se encontrou um registro
            }
        }
    }

    /**
     * Exibe um alerta de erro com título e mensagem especificados.
     *
     * @param titulo título do diálogo de erro
     * @param mensagem mensagem detalhada do erro
     */
    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}