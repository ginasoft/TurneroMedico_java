package modelo;

import java.time.LocalDateTime;
import java.util.Objects;

public class Turno {
    private Integer id;
    private Medico medico;
    private Paciente paciente;
    private LocalDateTime fechaHora;
    private Consultorio consultorio;

    public Turno(Integer id, Medico medico, Paciente paciente, LocalDateTime fechaHora, Consultorio consultorio) {
        this.id = id;
        this.medico = medico;
        this.paciente = paciente;
        this.fechaHora = fechaHora;
        this.consultorio  = consultorio;
    }
    
    public Turno(Integer id,
            Medico medico,
            Paciente paciente,
            LocalDateTime fechaHora) {
   this(id, medico, paciente, fechaHora, null);
}

    public Integer getId() {
        return id;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public Consultorio getConsultorio() { 
        return consultorio;
    }

    @Override
    public String toString() {
        return String.format(
            "Turno[id=%d, medico=%s, paciente=%s, fechaHora=%s, consultorio=%s]",
            id,
            medico.getNombre(),
            paciente.getNombre(),
            fechaHora,
            consultorio != null ? consultorio.getNombre() : "N/A"
        );
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turno)) return false;
        Turno t = (Turno) o;
        return Objects.equals(id, t.id)
            && Objects.equals(medico, t.medico)
            && Objects.equals(paciente, t.paciente)
            && Objects.equals(fechaHora, t.fechaHora)
            && Objects.equals(consultorio, t.consultorio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, medico, paciente, fechaHora, consultorio);
    }
    
    
}
