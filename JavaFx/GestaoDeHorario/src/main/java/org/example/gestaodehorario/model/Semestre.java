package org.example.gestaodehorario.model;

public class Semestre {
    private Integer idSemestre;
    private String nome;
    private Integer ano;

    public Semestre() {
    }

    public Semestre(Integer idSemestre, String nome, Integer ano) {
        this.idSemestre = idSemestre;
        this.nome = nome;
        this.ano = ano;
    }

    public Integer getIdSemestre() {
        return idSemestre;
    }

    public void setIdSemestre(Integer idSemestre) {
        this.idSemestre = idSemestre;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    @Override
    public String toString() {
        return nome;
    }
}
