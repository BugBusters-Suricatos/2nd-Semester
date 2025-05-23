package org.example.gestaodehorario.model;

public class RelatorioAlocacao {
    private String curso;
    private String semestre;
    private String periodo;
    private String dia;
    private String horario;
    private String materia;
    private String professor;

    public RelatorioAlocacao(String curso, String semestre, String periodo, String dia, String horario, String materia, String professor) {
        this.curso = curso;
        this.semestre = semestre;
        this.periodo = periodo;
        this.dia = dia;
        this.horario = horario;
        this.materia = materia;
        this.professor = professor;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}
