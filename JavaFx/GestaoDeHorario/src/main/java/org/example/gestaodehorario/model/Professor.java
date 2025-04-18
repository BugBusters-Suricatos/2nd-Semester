package org.example.gestaodehorario.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Representa um professor no sistema, com propriedades observáveis para integração com JavaFX.
 * Mantém informações de contato e a matéria associada para lecionar.
 */
public class Professor {
    private final StringProperty nome;
    private final StringProperty email;
    private Materia materia;
    private int id;

    /**
     * Construtor principal para criação de professores
     * @param nome Nome completo do professor
     * @param email Email institucional
     * @param materia {@link Materia} que o professor está qualificado a lecionar
     */
    public Professor(String nome, String email, Materia materia) {
        this.nome = new SimpleStringProperty(nome);
        this.email = new SimpleStringProperty(email);
        this.materia = materia;
    }

    // Getters e Setters ======================================================

    /**
     * @return Nome do professor
     */
    public String getNome() { return nome.get(); }

    /**
     * @return Propriedade observável do nome
     */
    public StringProperty nomeProperty() { return nome; }

    /**
     * @return Email do professor
     */
    public String getEmail() { return email.get(); }

    /**
     * @return Propriedade observável do email
     */
    public StringProperty emailProperty() { return email; }

    /**
     * @return {@link Materia} associada ao professor
     */
    public Materia getMateria() { return materia; }

    /**
     * Define a matéria associada ao professor
     * @param materia Nova {@link Materia} a ser lecionada
     */
    public void setMateria(Materia materia) { this.materia = materia; }

    /**
     * @return ID único do professor
     */
    public int getId() { return id; }

    /**
     * Define o ID do professor
     * @param id Novo identificador único
     */
    public void setId(int id) { this.id = id; }

    /**
     * Representação textual para exibição em componentes UI
     * @return Nome do professor
     */
    @Override
    public String toString() {
        return getNome();
    }
}