package org.example.gestaodehorario.model;

/**
 * Representa a relação entre uma matéria e um professor, permitindo associar
 * professores específicos às matérias que estão qualificados para lecionar.
 */
public class MateriaProfessor {
    private Integer idMateriaProfessor;
    private Materia materia;
    private Professor professor;

    /**
     * Construtor padrão que cria uma relação vazia
     */
    public MateriaProfessor() {
    }

    /**
     * Construtor completo para uma relação existente
     * @param idMateriaProfessor ID único da relação
     * @param materia {@link Materia} associada
     * @param professor {@link Professor} vinculado
     */
    public MateriaProfessor(Integer idMateriaProfessor, Materia materia, Professor professor) {
        this.idMateriaProfessor = idMateriaProfessor;
        this.materia = materia;
        this.professor = professor;
    }

    /**
     * @return ID único desta relação matéria-professor
     */
    public Integer getIdMateriaProfessor() {
        return idMateriaProfessor;
    }

    /**
     * Define o ID da relação
     * @param idMateriaProfessor Novo identificador único
     */
    public void setIdMateriaProfessor(Integer idMateriaProfessor) {
        this.idMateriaProfessor = idMateriaProfessor;
    }

    /**
     * @return {@link Materia} associada ao professor
     */
    public Materia getMateria() {
        return materia;
    }

    /**
     * Define a matéria da relação
     * @param materia Nova {@link Materia} a ser associada
     */
    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    /**
     * @return {@link Professor} vinculado à matéria
     */
    public Professor getProfessor() {
        return professor;
    }

    /**
     * Define o professor da relação
     * @param professor Novo {@link Professor} a ser vinculado
     */
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}