package org.example.gestaodehorario.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.example.gestaodehorario.ScreenManager;
import org.example.gestaodehorario.connect.DatabaseManager;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TelaLoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;

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
                ScreenManager.changeScreen("view/home-view.fxml", "styles/customHome.css");
            } else {
                mostrarErro("Falha no login", "Credenciais inválidas.");
            }
        } catch (SQLException e) {
            mostrarErro("Erro de Banco de Dados", "Não foi possível conectar ao sistema.");
            e.printStackTrace();
        }
    }

    private boolean autenticar(String email, String senha) throws SQLException {
        //TODO
        //Atualizar o banco para SHA2
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

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}