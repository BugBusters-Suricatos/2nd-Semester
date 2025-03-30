package org.example.gestaodehorario.model;

public class Periodo {
    private int idPeriodo;
    private String nome;

    public Periodo() {
    }

    public Periodo(int idPeriodo, String nome) {
        this.idPeriodo = idPeriodo;
        this.nome = nome;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
