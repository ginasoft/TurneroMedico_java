package conexiondb;

public class DataAccessException extends Exception {
    public DataAccessException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
