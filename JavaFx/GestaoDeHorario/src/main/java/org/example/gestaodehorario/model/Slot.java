package org.example.gestaodehorario.model;

public class Slot {
    private Integer id_slot;
    private String dia_semana;
    private String hora_inicio;
    private String hora_fim;
    private int id_periodo;
    private String status;

    public Slot() {
    }

    public Slot(Integer idSlot, String diaSemana, String horaInicio, String horaFim, String status, int id_periodo) {
        this.id_slot = idSlot;
        this.dia_semana = diaSemana;
        this.hora_inicio = horaInicio;
        this.hora_fim = horaFim;
        this.status = status;
        this.id_periodo = id_periodo;
    }

    public Integer getId_slot() {
        return id_slot;
    }

    public void setId_slot(Integer id_slot) {
        this.id_slot = id_slot;
    }

    public String getDia_semana() {
        return dia_semana;
    }

    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fim() {
        return hora_fim;
    }

    public void sethora_fim(String hora_fim) {
        this.hora_fim = hora_fim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHora_fim(String hora_fim) {
        this.hora_fim = hora_fim;
    }

    public int getId_periodo() {
        return id_periodo;
    }

    public void setId_periodo(int id_periodo) {
        this.id_periodo = id_periodo;
    }
}
