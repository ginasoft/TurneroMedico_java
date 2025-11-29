package conexiondb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String DRIVER = "org.h2.Driver";
    private static final String URL = "jdbc:h2:tcp://localhost:9092/turnera_medica";
    private static final String USUARIO = "sa";
    private static final String CONTRASENA = "";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection abrir() throws SQLException {
        Connection c = DriverManager.getConnection(URL, "sa", "");
        c.setAutoCommit(false);
        return c;
    }

    public static void cerrar(Connection c) {
        if (c != null) try { c.close(); } catch (SQLException ignored) {}
    }
}
