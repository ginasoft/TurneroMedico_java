package modelo;

import java.util.Objects;

public class Paciente {
    private Integer id;
    private String nombre;
    private String dni;
    private ObraSocial obraSocial; 
    
    public Paciente(Integer id, String nombre, String dni, ObraSocial obraSocial) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.obraSocial = obraSocial;
    }

    public Paciente(Integer id,
            String nombre,
            String dni) {
		this(id, nombre, dni, null);
		}

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }


    public ObraSocial getObraSocial()      { return obraSocial; }
    
    @Override
    public String toString() {
        return String.format("Paciente[id=%d,nombre=%s,dni=%s]",
                             id, nombre, dni);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
}
