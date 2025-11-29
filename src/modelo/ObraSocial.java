package modelo;

import java.util.Objects;

public class ObraSocial {
    private Integer id;
    private String nombre;
    private double porcentajeDescuento;

    public ObraSocial(Integer id, String nombre, double porcentajeDescuento) {
        this.id = id;
        this.nombre = nombre;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public ObraSocial(Integer id, String nombre) {
        this(id, nombre, 0.0);
    }
    
    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    @Override
    public String toString() {
        return String.format("ObraSocial[id=%d,nombre=%s,descuento=%.0f%%]",
            id, nombre, porcentajeDescuento * 100);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObraSocial)) return false;
        ObraSocial os = (ObraSocial) o;
        return Objects.equals(id, os.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
