package org.example.gestaodehorario.model;

public class MateriaProfessor {
    private Integer idMateriaProfessor;
    private Materia materia;
    private Professor professor;

    public MateriaProfessor() {
    }

    public MateriaProfessor(Integer idMateriaProfessor, Materia materia, Professor professor) {
        this.idMateriaProfessor = idMateriaProfessor;
        this.materia = materia;
        this.professor = professor;
    }

    public Integer getIdMateriaProfessor() {
        return idMateriaProfessor;
    }

    public void setIdMateriaProfessor(Integer idMateriaProfessor) {
        this.idMateriaProfessor = idMateriaProfessor;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
