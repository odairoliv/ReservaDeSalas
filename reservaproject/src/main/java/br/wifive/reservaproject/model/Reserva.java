package br.wifive.reservaproject.model;

public class Reserva {
    private String id;
    private String salaId;
    private String professor;
    private String dataReserva;
    private String horario;

    public Reserva() {}

    public Reserva(String id, String salaId, String professor, String dataReserva, String horario) {
        this.id = id;
        this.salaId = salaId;
        this.professor = professor;
        this.dataReserva = dataReserva;
        this.horario = horario;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSalaId() { return salaId; }
    public void setSalaId(String salaId) { this.salaId = salaId; }

    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }

    public String getDataReserva() { return dataReserva; }
    public void setDataReserva(String dataReserva) { this.dataReserva = dataReserva; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
}