package servicio.excepciones;

public class ServicioException extends Exception {

    public ServicioException(String mensaje) {
        super(mensaje);
    }

    public ServicioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
