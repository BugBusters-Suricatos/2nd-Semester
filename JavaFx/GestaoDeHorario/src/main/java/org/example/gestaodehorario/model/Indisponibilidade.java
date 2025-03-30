package org.example.gestaodehorario.model;

public class Indisponibilidade {
    private Integer id;
    private Integer id_professor;
    private String dia_semana;
    private String hora_inicio;
    private String hora_fim;
    private Integer id_curso;
    private Integer id_semestre;

    public Indisponibilidade() {
    }

    // Construtor completo para integração com o banco
    public Indisponibilidade(Integer id, Integer id_professor, String dia_semana,
                             String hora_inicio, String hora_fim, Integer id_curso,
                             Integer id_semestre) {
        this.id = id;
        this.id_professor = id_professor;
        this.dia_semana = dia_semana;
        this.hora_inicio = hora_inicio;
        this.hora_fim = hora_fim;
        this.id_curso = id_curso;
        this.id_semestre = id_semestre;
    }

    // Construtor para criação de novas indisponibilidades (sem ID)
    public Indisponibilidade(Integer id_professor, String dia_semana, String hora_inicio,
                             String hora_fim, Integer id_curso, Integer id_semestre) {
        this.id_professor = id_professor;
        this.dia_semana = dia_semana;
        this.hora_inicio = hora_inicio;
        this.hora_fim = hora_fim;
        this.id_curso = id_curso;
        this.id_semestre = id_semestre;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_professor() {
        return id_professor;
    }

    public void setId_professor(Integer id_professor) {
        this.id_professor = id_professor;
    }

    public String getDia_semana() {
        return dia_semana;
    }

    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fim() {
        return hora_fim;
    }

    public void setHora_fim(String hora_fim) {
        this.hora_fim = hora_fim;
    }

    public Integer getId_curso() {
        return id_curso;
    }

    public void setId_curso(Integer id_curso) {
        this.id_curso = id_curso;
    }

    public Integer getId_semestre() {
        return id_semestre;
    }

    public void setId_semestre(Integer id_semestre) {
        this.id_semestre = id_semestre;
    }
}