package org.example.gestaodehorario.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.gestaodehorario.ScreenManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TelaLoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;

    @FXML
    public void initialize() {

    }

    @FXML
    private void LoginClick(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String senha = txtSenha.getText().trim();

//        if (usuario.isEmpty() || senha.isEmpty()) {
//            mostrarErro("Campos obrigatórios!", "Preencha usuário e senha.");
//            return;
//        }
//
//        if (autenticar(usuario, senha)) {
//            ScreenManager.changeScreen("home-view.fxml"); // Redireciona para a tela principal
//        } else {
//            mostrarErro("Falha no login", "Usuário ou senha incorretos.");
//        }
    }

//    private boolean autenticar(String usuario, String senha) {
//        String sql = "SELECT id_coordenador FROM Coordenador WHERE email = ? AND senha = ?";
//
//    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

