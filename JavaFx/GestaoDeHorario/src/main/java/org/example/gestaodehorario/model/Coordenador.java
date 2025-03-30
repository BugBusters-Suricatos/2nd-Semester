package org.example.gestaodehorario.model;

public class Coordenador {
    private Integer idCoordenador;
    private String nome;
    private String email;
    private String senha;

    public Coordenador() {
    }

    public Coordenador(Integer idCoordenador, String nome, String email, String senha) {
        this.idCoordenador = idCoordenador;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Integer getIdCoordenador() {
        return idCoordenador;
    }

    public void setIdCoordenador(Integer idCoordenador) {
        this.idCoordenador = idCoordenador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return nome;
    }
}
