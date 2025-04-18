package org.example.gestaodehorario.model;

/**
 * Representa um semestre acadêmico no sistema, contendo informações de identificação,
 * nomenclatura e ano de vigência.
 */
public class Semestre {
    private Integer idSemestre;
    private String nome;
    private Integer ano;

    /**
     * Construtor padrão necessário para frameworks e serialização
     */
    public Semestre() {
    }

    /**
     * Construtor completo para criação de semestres
     * @param idSemestre Identificador único do semestre
     * @param nome Nome do semestre (ex: "2023.1", "Primeiro Semestre")
     * @param ano Ano de vigência do semestre
     */
    public Semestre(Integer idSemestre, String nome, Integer ano) {
        this.idSemestre = idSemestre;
        this.nome = nome;
        this.ano = ano;
    }

    /**
     * @return ID único do semestre
     */
    public Integer getIdSemestre() {
        return idSemestre;
    }

    /**
     * Define o ID do semestre
     * @param idSemestre Novo identificador único
     */
    public void setIdSemestre(Integer idSemestre) {
        this.idSemestre = idSemestre;
    }

    /**
     * @return Nome descritivo do semestre
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do semestre
     * @param nome Novo nome descritivo
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return Ano de vigência do semestre
     */
    public Integer getAno() {
        return ano;
    }

    /**
     * Define o ano de vigência
     * @param ano Novo ano de vigência
     */
    public void setAno(Integer ano) {
        this.ano = ano;
    }

    /**
     * Representação textual para exibição em interfaces
     * @return Nome do semestre formatado
     */
    @Override
    public String toString() {
        return nome;
    }
}