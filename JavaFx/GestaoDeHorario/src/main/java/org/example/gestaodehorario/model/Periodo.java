package org.example.gestaodehorario.model;

/**
 * Representa um período acadêmico no sistema, contendo informações básicas
 * de identificação e nomenclatura.
 */
public class Periodo {
    private int idPeriodo;
    private String nome;

    /**
     * Construtor padrão que cria uma instância vazia de período
     */
    public Periodo() {
    }

    /**
     * Construtor completo para criação de períodos
     * @param idPeriodo Identificador único do período
     * @param nome Nome descritivo do período (ex: "2023.1", "Primeiro Semestre")
     */
    public Periodo(int idPeriodo, String nome) {
        this.idPeriodo = idPeriodo;
        this.nome = nome;
    }

    /**
     * @return ID único do período
     */
    public int getIdPeriodo() {
        return idPeriodo;
    }

    /**
     * Define o ID do período
     * @param idPeriodo Novo identificador único
     */
    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    /**
     * @return Nome descritivo do período
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do período
     * @param nome Novo nome descritivo
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
}