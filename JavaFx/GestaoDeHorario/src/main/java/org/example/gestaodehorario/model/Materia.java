package org.example.gestaodehorario.model;

import javafx.beans.property.*;

/**
 * Representa uma matéria acadêmica com propriedades observáveis para integração com JavaFX.
 * Mantém informações básicas e relacionamento com um curso.
 */
public class Materia {
    private final IntegerProperty idMateria = new SimpleIntegerProperty();
    private final StringProperty nome = new SimpleStringProperty();
    private final IntegerProperty cargaHoraria = new SimpleIntegerProperty();
    private final ObjectProperty<Curso> curso = new SimpleObjectProperty<>();

    /**
     * Construtor padrão necessário para serialização e inicialização de frameworks
     */
    public Materia() {}

    /**
     * Construtor simplificado para criação básica (sem carga horária e curso)
     * @param idMateria Identificador único da matéria
     * @param nome Nome da matéria
     */
    public Materia(int idMateria, String nome) {
        this.idMateria.set(idMateria);
        this.nome.set(nome);
    }

    /**
     * Construtor completo para operações com banco de dados
     * @param idMateria Identificador único da matéria
     * @param nome Nome da matéria
     * @param cargaHoraria Carga horária total em horas
     * @param curso {@link Curso} ao qual a matéria pertence
     */
    public Materia(int idMateria, String nome, int cargaHoraria, Curso curso) {
        this.idMateria.set(idMateria);
        this.nome.set(nome);
        this.cargaHoraria.set(cargaHoraria);
        this.curso.set(curso);
    }

    /**
     * Construtor alternativo para inicialização parcial (implementação pendente)
     */
    public Materia(int idMateria, String nome, int cargaHoraria, int idCurso) {
        // Implementação pendente - necessário vincular curso através do ID
    }

    // Property Accessors ======================================================

    /**
     * @return Propriedade observável do ID da matéria
     */
    public IntegerProperty idMateriaProperty() {
        return idMateria;
    }

    /**
     * @return Propriedade observável do nome da matéria
     */
    public StringProperty nomeProperty() {
        return nome;
    }

    /**
     * @return Propriedade observável da carga horária
     */
    public IntegerProperty cargaHorariaProperty() {
        return cargaHoraria;
    }

    /**
     * @return Propriedade observável do curso associado
     */
    public ObjectProperty<Curso> cursoProperty() {
        return curso;
    }

    // Getters/Setters =========================================================

    public int getIdMateria() {
        return idMateria.get();
    }

    public void setIdMateria(int idMateria) {
        this.idMateria.set(idMateria);
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public int getCargaHoraria() {
        return cargaHoraria.get();
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria.set(cargaHoraria);
    }

    public Curso getCurso() {
        return curso.get();
    }

    public void setCurso(Curso curso) {
        this.curso.set(curso);
    }

    /**
     * Representação textual para exibição em componentes UI
     * @return Nome da matéria e carga horária formatados
     */
    @Override
    public String toString() {
        return nome.get() + " (" + cargaHoraria.get() + "h)";
    }
}