package org.example.gestaodehorario.model;

/**
 * Representa a alocação de um professor para ministrar uma matéria em um slot horário específico
 * durante um semestre acadêmico.
 */
public class Alocacao {
    private Integer idAlocacao;
    private MateriaProfessor materiaProfessor;
    private Slot slot;
    private Semestre semestre;

    /**
     * Construtor padrão que cria uma alocação vazia.
     */
    public Alocacao() {
    }

    /**
     * Construtor para nova alocação (sem ID)
     * @param materiaProfessor Relação professor-matéria a ser alocada
     * @param slot Slot horário para a alocação
     * @param semestre Semestre acadêmico da alocação
     */
    public Alocacao(MateriaProfessor materiaProfessor, Slot slot, Semestre semestre) {
        this.materiaProfessor = materiaProfessor;
        this.slot = slot;
        this.semestre = semestre;
    }

    /**
     * Construtor completo para alocação existente
     * @param idAlocacao Identificador único da alocação
     * @param materiaProfessor Relação professor-matéria alocada
     * @param slot Slot horário reservado
     * @param semestre Semestre acadêmico vinculado
     */
    public Alocacao(Integer idAlocacao, MateriaProfessor materiaProfessor, Slot slot, Semestre semestre) {
        this.idAlocacao = idAlocacao;
        this.materiaProfessor = materiaProfessor;
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
     * @return {@link MateriaProfessor} contendo a relação professor-matéria alocada
     */
    public MateriaProfessor getMateriaProfessor() {
        return materiaProfessor;
    }

    /**
     * Define a relação professor-matéria para esta alocação
     * @param materiaProfessor Nova relação professor-matéria
     */
    public void setMateriaProfessor(MateriaProfessor materiaProfessor) {
        this.materiaProfessor = materiaProfessor;
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