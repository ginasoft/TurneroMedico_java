package conexiondb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableManager {

    public void crearTablas() throws DataAccessException {
        crearTablaObrasSociales();
        crearTablaMedicos();
        crearTablaPacientes();
        crearTablaConsultorios();
        crearTablaTurnos();
    }

    private void crearTablaObrasSociales() throws DataAccessException {
        new OperacionBD<Void>() {
            @Override
            protected Void ejecutarOperacion(Connection c) throws SQLException {
                String sql =
                    "CREATE TABLE IF NOT EXISTS obras_sociales (" +
                    "  id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "  nombre VARCHAR(100) NOT NULL, " +
                    "  porcentaje_descuento DOUBLE NOT NULL" +
                    ")";
                try (Statement st = c.createStatement()) {
                    st.execute(sql);
                }
                return null;
            }
        }.ejecutar();
    }

    private void crearTablaMedicos() throws DataAccessException {
        new OperacionBD<Void>() {
            @Override
            protected Void ejecutarOperacion(Connection c) throws SQLException {
                String sql =
                    "CREATE TABLE IF NOT EXISTS medicos (" +
                    "  id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "  nombre VARCHAR(100) NOT NULL, " +
                    "  valor_consulta DECIMAL(10,2) NOT NULL, " +
                    "  obra_social_id INTEGER NOT NULL, " +
                    "  FOREIGN KEY (obra_social_id) REFERENCES obras_sociales(id)" +
                    ")";
                try (Statement st = c.createStatement()) {
                    st.execute(sql);
                }
                return null;
            }
        }.ejecutar();
    }

    private void crearTablaPacientes() throws DataAccessException {
        new OperacionBD<Void>() {
            @Override
            protected Void ejecutarOperacion(Connection c) throws SQLException {
                String sql =
                    "CREATE TABLE IF NOT EXISTS pacientes (" +
                    "  id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "  nombre VARCHAR(100) NOT NULL, " +
                    "  dni VARCHAR(20) NOT NULL, " +
                    "  obra_social_id INTEGER NOT NULL, " +
                    "  FOREIGN KEY (obra_social_id) REFERENCES obras_sociales(id)" +
                    ")";
                try (Statement st = c.createStatement()) {
                    st.execute(sql);
                }
                return null;
            }
        }.ejecutar();
    }

    private void crearTablaConsultorios() throws DataAccessException {
        new OperacionBD<Void>() {
            @Override
            protected Void ejecutarOperacion(Connection c) throws SQLException {
                String sql =
                    "CREATE TABLE IF NOT EXISTS consultorios (" +
                    "  id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "  nombre VARCHAR(100) NOT NULL, " +
                    "  ubicacion VARCHAR(200) NOT NULL" +
                    ")";
                try (Statement st = c.createStatement()) {
                    st.execute(sql);
                }
                return null;
            }
        }.ejecutar();
    }

    private void crearTablaTurnos() throws DataAccessException {
        new OperacionBD<Void>() {
            @Override
            protected Void ejecutarOperacion(Connection c) throws SQLException {
                String sql =
                    "CREATE TABLE IF NOT EXISTS turnos (" +
                    "  id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "  medico_id INTEGER NOT NULL, " +
                    "  paciente_id INTEGER NOT NULL, " +
                    "  consultorio_id INTEGER NOT NULL, " +
                    "  fecha_hora TIMESTAMP NOT NULL, " +
                    "  UNIQUE (medico_id, fecha_hora), " +
                    "  UNIQUE (consultorio_id, fecha_hora), " +
                    "  FOREIGN KEY (medico_id) REFERENCES medicos(id), " +
                    "  FOREIGN KEY (paciente_id) REFERENCES pacientes(id), " +
                    "  FOREIGN KEY (consultorio_id) REFERENCES consultorios(id)" +
                    ")";
                try (Statement st = c.createStatement()) {
                    st.execute(sql);
                }
                return null;
            }
        }.ejecutar();
    }
}
