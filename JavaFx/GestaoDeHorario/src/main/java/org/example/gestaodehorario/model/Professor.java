package org.example.gestaodehorario.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Professor {
    private final StringProperty nome;
    private final StringProperty email;
    private Materia materia;
    private int id;

    public Professor(String nome, String email, Materia materia) {
        this.nome = new SimpleStringProperty(nome);
        this.email = new SimpleStringProperty(email);
        this.materia = materia;
    }

    // Getters e Setters
    public String getNome() { return nome.get(); }
    public StringProperty nomeProperty() { return nome; }

    public String getEmail() { return email.get(); }
    public StringProperty emailProperty() { return email; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return getNome();
    }
}