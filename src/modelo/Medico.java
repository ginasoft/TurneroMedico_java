package modelo;

public class Medico {
    private Integer id;
    private String nombre;
    private double valorConsulta;
    private ObraSocial obraSocial;

    public Medico(Integer id, String nombre, double valorConsulta, ObraSocial obraSocial) {
        this.id = id;
        this.nombre = nombre;
        this.valorConsulta = valorConsulta;
        this.obraSocial = obraSocial;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getValorConsulta() {
        return valorConsulta;
    }

    public ObraSocial getObraSocial()      { return obraSocial; }
    
    @Override
    public String toString() {
        return String.format("Medico[id=%d,nombre=%s,valorConsulta=%.2f]",
                             id, nombre, valorConsulta);
    }
}
