package org.example.gestaodehorario.model;

public class Alocacao {
    private Integer idAlocacao;
    private MateriaProfessor materiaProfessor;
    private Slot slot;
    private Semestre semestre;

    public Alocacao() {
    }

    public Alocacao(MateriaProfessor materiaProfessor, Slot slot, Semestre semestre) {
        this.materiaProfessor = materiaProfessor;
        this.slot = slot;
        this.semestre = semestre;
    }

    public Alocacao(Integer idAlocacao, MateriaProfessor materiaProfessor, Slot slot, Semestre semestre) {
        this.idAlocacao = idAlocacao;
        this.materiaProfessor = materiaProfessor;
        this.slot = slot;
        this.semestre = semestre;
    }

    public Integer getIdAlocacao() {
        return idAlocacao;
    }

    public void setIdAlocacao(Integer idAlocacao) {
        this.idAlocacao = idAlocacao;
    }

    public MateriaProfessor getMateriaProfessor() {
        return materiaProfessor;
    }

    public void setMateriaProfessor(MateriaProfessor materiaProfessor) {
        this.materiaProfessor = materiaProfessor;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }
}
