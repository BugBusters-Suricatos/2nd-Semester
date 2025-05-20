package org.example.gestaodehorario.model;

import org.example.gestaodehorario.connect.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Representa um período de indisponibilidade de um professor, vinculado a um curso e semestre específicos.
 * Define um intervalo de tempo em que o professor não pode ser alocado para ministrar aulas.
 */
public class Indisponibilidade {
    private Integer id;
    private Integer id_professor;
    private String dia_semana;
    private String hora_inicio;
    private String hora_fim;
    private Integer id_curso;
    private Integer id_semestre;

    /**
     * Construtor padrão que cria uma instância vazia de Indisponibilidade
     */
    public Indisponibilidade() {
    }

    /**
     * Construtor completo para instâncias existentes (com ID)
     * @param id Identificador único da indisponibilidade
     * @param id_professor ID do professor indisponível
     * @param dia_semana Dia da semana no formato 'Segunda', 'Terça', etc.
     * @param hora_inicio Hora de início no formato HH:MM
     * @param hora_fim Hora de término no formato HH:MM
     * @param id_curso ID do curso relacionado
     * @param id_semestre ID do semestre acadêmico
     */
    public Indisponibilidade(Integer id, Integer id_professor, String dia_semana,
                             String hora_inicio, String hora_fim, Integer id_curso,
                             Integer id_semestre) {
        this.id = id;
        this.id_professor = id_professor;
        this.dia_semana = dia_semana;
        this.hora_inicio = hora_inicio;
        this.hora_fim = hora_fim;
        this.id_curso = id_curso;
        this.id_semestre = id_semestre;
    }

    /**
     * Construtor para novas indisponibilidades (sem ID)
     * @param id_professor ID do professor indisponível
     * @param dia_semana Dia da semana no formato 'Segunda', 'Terça', etc.
     * @param hora_inicio Hora de início no formato HH:MM
     * @param hora_fim Hora de término no formato HH:MM
     * @param id_curso ID do curso relacionado
     * @param id_semestre ID do semestre acadêmico
     */
    public Indisponibilidade(Integer id_professor, String dia_semana, String hora_inicio,
                             String hora_fim, Integer id_curso, Integer id_semestre) {
        this.id_professor = id_professor;
        this.dia_semana = dia_semana;
        this.hora_inicio = hora_inicio;
        this.hora_fim = hora_fim;
        this.id_curso = id_curso;
        this.id_semestre = id_semestre;
    }

    /**
     * @return ID único desta indisponibilidade
     */
    public Integer getId() {
        return id;
    }

    /**
     * Define o ID da indisponibilidade
     * @param id Novo identificador único
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return ID do professor relacionado
     */
    public Integer getId_professor() {
        return id_professor;
    }

    /**
     * Define o professor relacionado
     * @param id_professor Novo ID de professor
     */
    public void setId_professor(Integer id_professor) {
        this.id_professor = id_professor;
    }

    /**
     * @return Dia da semana da indisponibilidade
     */
    public String getDia_semana() {
        return dia_semana;
    }

    /**
     * Define o dia da semana da indisponibilidade
     * @param dia_semana Novo dia no formato 'Segunda', 'Terça', etc.
     */
    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }

    /**
     * @return Hora de início da indisponibilidade
     */
    public String getHora_inicio() {
        return hora_inicio;
    }

    /**
     * Define a hora de início
     * @param hora_inicio Nova hora no formato HH:MM
     */
    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    /**
     * @return Hora de término da indisponibilidade
     */
    public String getHora_fim() {
        return hora_fim;
    }

    /**
     * Define a hora de término
     * @param hora_fim Nova hora no formato HH:MM
     */
    public void setHora_fim(String hora_fim) {
        this.hora_fim = hora_fim;
    }

    /**
     * @return ID do curso relacionado
     */
    public Integer getId_curso() {
        return id_curso;
    }

    /**
     * Define o curso relacionado
     * @param id_curso Novo ID de curso
     */
    public void setId_curso(Integer id_curso) {
        this.id_curso = id_curso;
    }

    /**
     * @return ID do semestre acadêmico
     */
    public Integer getId_semestre() {
        return id_semestre;
    }

    /**
     * Define o semestre acadêmico
     * @param id_semestre Novo ID de semestre
     */
    public void setId_semestre(Integer id_semestre) {
        this.id_semestre = id_semestre;
    }


}