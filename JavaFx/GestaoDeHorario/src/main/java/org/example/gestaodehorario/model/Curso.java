package org.example.gestaodehorario.model;

/**
 * Representa um curso acadêmico no sistema, contendo informações básicas
 * e relacionamento com período e coordenador responsável.
 */
public class Curso {
    private int idCurso;
    private String nome;
    private int idPeriodo;
    private Coordenador Coordenador;

    /**
     * Construtor padrão que cria uma instância vazia de Curso
     */
    public Curso() {
    }

    /**
     * Construtor completo para criação de um curso
     * @param idCurso Identificador único do curso
     * @param nome Nome completo do curso
     * @param idPeriodo ID do período acadêmico associado
     * @param Coordenador Coordenador responsável pelo curso
     */
    public Curso(int idCurso, String nome, int idPeriodo, Coordenador Coordenador) {
        this.idCurso = idCurso;
        this.nome = nome;
        this.idPeriodo = idPeriodo;
        this.Coordenador = Coordenador;
    }

    /**
     * @return ID único do curso
     */
    public int getIdCurso() {
        return idCurso;
    }

    /**
     * Define o ID do curso
     * @param idCurso Novo identificador único
     */
    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    /**
     * @return Nome completo do curso
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do curso
     * @param nome Novo nome do curso
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return ID do período acadêmico associado
     */
    public int getIdPeriodo() {
        return idPeriodo;
    }

    /**
     * Define o período acadêmico associado
     * @param idPeriodo Novo ID de período
     */
    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    /**
     * @return {@link Coordenador} responsável pelo curso
     */
    public Coordenador getCoordenador() {
        return Coordenador;
    }

    /**
     * Define o coordenador responsável
     * @param coordenador Novo objeto {@link Coordenador}
     */
    public void setCoordenador(Coordenador coordenador) {
        Coordenador = coordenador;
    }

    /**
     * Representação textual simplificada do curso
     * @return Nome do curso (para exibição em interfaces)
     */
    @Override
    public String toString() {
        return getNome();
    }
}