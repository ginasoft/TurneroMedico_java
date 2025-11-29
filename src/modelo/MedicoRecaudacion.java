package modelo;

import java.util.Objects;

public class MedicoRecaudacion {
    private final Medico medico;
    private final int cantidadTurnos;
    private final double totalRecaudado;

    public MedicoRecaudacion(Medico medico, int cantidadTurnos, double totalRecaudado) {
        this.medico = medico;
        this.cantidadTurnos = cantidadTurnos;
        this.totalRecaudado = totalRecaudado;
    }

    public Medico getMedico() {
        return medico;
    }

    public int getCantidadTurnos() {
        return cantidadTurnos;
    }

    public double getTotalRecaudado() {
        return totalRecaudado;
    }

    @Override
    public String toString() {
        return String.format("MedicoRecaudacion[medico=%s, turnos=%d, total=%.2f]",
            medico.getNombre(), cantidadTurnos, totalRecaudado);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicoRecaudacion)) return false;
        MedicoRecaudacion that = (MedicoRecaudacion) o;
        return cantidadTurnos == that.cantidadTurnos &&
               Double.compare(that.totalRecaudado, totalRecaudado) == 0 &&
               Objects.equals(medico, that.medico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medico, cantidadTurnos, totalRecaudado);
    }
}
