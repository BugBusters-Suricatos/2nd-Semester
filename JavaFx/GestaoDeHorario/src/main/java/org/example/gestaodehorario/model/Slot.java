package org.example.gestaodehorario.model;

/**
 * Representa um slot de horário no sistema, contendo informações sobre disponibilidade,
 * período associado e horários específicos. Utilizado para alocação de aulas.
 */
public class Slot {
    private Integer id_slot;
    private String dia_semana;
    private String hora_inicio;
    private String hora_fim;
    private int id_periodo;
    private String status;

    /**
     * Construtor padrão para inicialização genérica
     */
    public Slot() {
    }

    /**
     * Construtor completo para criação de slots
     * @param idSlot Identificador único do slot
     * @param diaSemana Dia da semana (ex: "Segunda-feira", "Terça-feira")
     * @param horaInicio Hora de início no formato HH:MM
     * @param horaFim Hora de término no formato HH:MM
     * @param status Status de disponibilidade (ex: "disponível", "ocupado")
     * @param id_periodo ID do período acadêmico associado
     */
    public Slot(Integer idSlot, String diaSemana, String horaInicio, String horaFim, String status, int id_periodo) {
        this.id_slot = idSlot;
        this.dia_semana = diaSemana;
        this.hora_inicio = horaInicio;
        this.hora_fim = horaFim;
        this.status = status;
        this.id_periodo = id_periodo;
    }

    /**
     * @return ID único deste slot
     */
    public Integer getId_slot() {
        return id_slot;
    }

    /**
     * Define o ID do slot
     * @param id_slot Novo identificador único
     */
    public void setId_slot(Integer id_slot) {
        this.id_slot = id_slot;
    }

    /**
     * @return Dia da semana associado ao slot
     */
    public String getDia_semana() {
        return dia_semana;
    }

    /**
     * Define o dia da semana
     * @param dia_semana Novo dia no formato extenso (ex: "Quarta-feira")
     */
    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }

    /**
     * @return Hora de início no formato HH:MM
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
     * @return Hora de término no formato HH:MM
     */
    public String getHora_fim() {
        return hora_fim;
    }

    /**
     * Define a hora de término (nome alternativo)
     * @deprecated Preferir usar {@link #setHora_fim(String)}
     */
    public void sethora_fim(String hora_fim) {
        this.hora_fim = hora_fim;
    }

    /**
     * Define a hora de término
     * @param hora_fim Nova hora no formato HH:MM
     */
    public void setHora_fim(String hora_fim) {
        this.hora_fim = hora_fim;
    }

    /**
     * @return Status atual do slot (ex: "disponível", "reservado", "ocupado")
     */
    public String getStatus() {
        return status;
    }

    /**
     * Atualiza o status do slot
     * @param status Novo status (deve seguir convenções do sistema)
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return ID do {@link Periodo} associado
     */
    public int getId_periodo() {
        return id_periodo;
    }

    /**
     * Define o período acadêmico associado
     * @param id_periodo Novo ID de período
     */
    public void setId_periodo(int id_periodo) {
        this.id_periodo = id_periodo;
    }
}