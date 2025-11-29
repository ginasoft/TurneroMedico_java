package modelo;

import java.util.Objects;

public class Consultorio {
    private Integer id;
    private String nombre;
    private String ubicacion;

    public Consultorio(Integer id, String nombre, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    @Override
    public String toString() {
        return String.format("Consultorio[id=%d,nombre=%s,ubicacion=%s]", id, nombre, ubicacion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consultorio)) return false;
        Consultorio c = (Consultorio) o;
        return Objects.equals(id, c.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
