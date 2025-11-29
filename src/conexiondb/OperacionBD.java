package conexiondb;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class OperacionBD<T> {

    public T ejecutar() throws DataAccessException {
        Connection c = null;
        try {
            c = ConexionBD.abrir();
            T resultado = ejecutarOperacion(c);
            c.commit();
            return resultado;
        } catch (SQLException e) {
            if (c != null) try { c.rollback(); } catch (SQLException ignore) {}
            throw new DataAccessException("Error en operación de BD", e);
        } finally {
            ConexionBD.cerrar(c);
        }
    }

    protected abstract T ejecutarOperacion(Connection c) throws SQLException;
}
