package org.example.gestaodehorario.model;

/**
 * Representa a alocação de um professor para ministrar uma matéria em um slot horário específico
 * durante um semestre acadêmico.
 */
public class Alocacao {
    private Integer idAlocacao;
    private Professor professor;
    private Materia materia;
    private Slot slot;
    private Semestre semestre;

    /**
     * Construtor padrão que cria uma alocação vazia.
     */
    public Alocacao() {
    }

    /**
     * Construtor para nova alocação (sem ID)
     * @param professor Relação professor-matéria a ser alocada
     * @param materia Relação professor-matéria a ser alocada
     * @param slot Slot horário para a alocação
     * @param semestre Semestre acadêmico da alocação
     */
    public Alocacao(Professor professor, Materia materia, Slot slot, Semestre semestre) {
        this.professor = professor;
        this.materia = materia;
        this.slot = slot;
        this.semestre = semestre;
    }

    /**
     * Construtor completo para alocação existente
     * @param idAlocacao Identificador único da alocação
     * @param professor Relação professor-matéria alocada
     * @param materia Relação professor-matéria alocada
     * @param slot Slot horário reservado
     * @param semestre Semestre acadêmico vinculado
     */
    public Alocacao(Integer idAlocacao, Professor professor, Materia materia, Slot slot, Semestre semestre) {
        this.idAlocacao = idAlocacao;
        this.professor = professor;
        this.materia = materia;
        this.slot = slot;
        this.semestre = semestre;
    }

    /**
     * @return ID único desta alocação
     */
    public Integer getIdAlocacao() {
        return idAlocacao;
    }

    /**
     * Define o ID da alocação
     * @param idAlocacao Novo identificador único
     */
    public void setIdAlocacao(Integer idAlocacao) {
        this.idAlocacao = idAlocacao;
    }

    /**
     * @return {@link Professor} contendo a relação professor-matéria alocada
     */
    public Professor getProfessor() {
        return professor;
    }

    /**
     * Define a relação professor-matéria para esta alocação
     * @param professor Nova relação professor-matéria
     */
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    /**
     * @return {@link Materia} contendo a relação professor-matéria alocada
     */
    public Materia getMateria() {
        return materia;
    }

    /**
     * Define a relação professor-matéria para esta alocação
     * @param materia Nova relação professor-matéria
     */
    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    /**
     * @return {@link Slot} com o horário alocado
     */
    public Slot getSlot() {
        return slot;
    }

    /**
     * Define o slot horário para esta alocação
     * @param slot Novo slot horário
     */
    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    /**
     * @return {@link Semestre} vinculado à alocação
     */
    public Semestre getSemestre() {
        return semestre;
    }

    /**
     * Define o semestre acadêmico para esta alocação
     * @param semestre Novo semestre acadêmico
     */
    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }
}