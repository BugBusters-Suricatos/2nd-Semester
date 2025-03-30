package org.example.gestaodehorario.model;

import javafx.beans.property.*;

public class Materia {
    private final IntegerProperty idMateria = new SimpleIntegerProperty();
    private final StringProperty nome = new SimpleStringProperty();
    private final IntegerProperty cargaHoraria = new SimpleIntegerProperty();
    private final ObjectProperty<Curso> curso = new SimpleObjectProperty<>();

    // Construtor padrão (necessário para serialização)
    public Materia() {}

    // Construtor simplificado (para criação básica)
    public Materia(int idMateria, String nome) {
        this.idMateria.set(idMateria);
        this.nome.set(nome);
    }

    // Construtor completo (para operações com banco de dados)
    public Materia(int idMateria, String nome, int cargaHoraria, Curso curso) {
        this.idMateria.set(idMateria);
        this.nome.set(nome);
        this.cargaHoraria.set(cargaHoraria);
        this.curso.set(curso);
    }

    public Materia(int idMateria, String nome, int cargaHoraria, int idCurso) {
    }

    // Property Accessors
    public IntegerProperty idMateriaProperty() {
        return idMateria;
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public IntegerProperty cargaHorariaProperty() {
        return cargaHoraria;
    }

    public ObjectProperty<Curso> cursoProperty() {
        return curso;
    }

    // Getters e Setters
    public int getIdMateria() {
        return idMateria.get();
    }

    public void setIdMateria(int idMateria) {
        this.idMateria.set(idMateria);
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public int getCargaHoraria() {
        return cargaHoraria.get();
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria.set(cargaHoraria);
    }

    public Curso getCurso() {
        return curso.get();
    }

    public void setCurso(Curso curso) {
        this.curso.set(curso);
    }

    @Override
    public String toString() {
        return nome.get() + " (" + cargaHoraria.get() + "h)";
    }
}