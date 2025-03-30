package org.example.gestaodehorario.model;

public class Curso {
    private int idCurso;
    private String nome;
    private int idPeriodo;
    private Coordenador Coordenador;


    public Curso() {
    }

    // Construtor
    public Curso(int idCurso, String nome, int idPeriodo, Coordenador Coordenador) {
        this.idCurso = idCurso;
        this.nome = nome;
        this.idPeriodo = idPeriodo;
        this.Coordenador = Coordenador;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Coordenador getCoordenador() {
        return Coordenador;
    }

    public void setCoordenador(Coordenador coordenador) {
        Coordenador = coordenador;
    }

    @Override
    public String toString() {
        return getNome();
    }
}