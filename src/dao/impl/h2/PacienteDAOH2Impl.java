package dao.impl.h2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import modelo.Paciente;
import modelo.ObraSocial;
import dao.PacienteDAO;
import conexiondb.OperacionBD;
import conexiondb.DataAccessException;

public class PacienteDAOH2Impl implements PacienteDAO {

    @Override
    public List<Paciente> obtenerTodos() throws DataAccessException {
        return new OperacionBD<List<Paciente>>() {
            @Override
            protected List<Paciente> ejecutarOperacion(Connection c) throws SQLException {
                List<Paciente> lista = new ArrayList<>();
                String sql =
                  "SELECT p.id, p.nombre, p.dni, " +
                  " os.id AS osid, os.nombre AS osnombre, os.porcentaje_descuento AS osdesc " +
                  "FROM pacientes p " +
                  "JOIN obras_sociales os ON p.obra_social_id = os.id";
                try (Statement st = c.createStatement();
                     ResultSet rs = st.executeQuery(sql)) {
                    while (rs.next()) {
                        ObraSocial os = new ObraSocial(
                            rs.getInt("osid"),
                            rs.getString("osnombre"),
                            rs.getDouble("osdesc")
                        );
                        lista.add(new Paciente(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("dni"),
                            os
                        ));
                    }
                }
                return lista;
            }
        }.ejecutar();
    }

    @Override
    public Paciente obtenerPorId(Integer id) throws DataAccessException {
        return new OperacionBD<Paciente>() {
            @Override
            protected Paciente ejecutarOperacion(Connection c) throws SQLException {
                String sql =
                  "SELECT p.id, p.nombre, p.dni, " +
                  " os.id AS osid, os.nombre AS osnombre, os.porcentaje_descuento AS osdesc " +
                  "FROM pacientes p " +
                  "JOIN obras_sociales os ON p.obra_social_id = os.id " +
                  "WHERE p.id = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            ObraSocial os = new ObraSocial(
                                rs.getInt("osid"),
                                rs.getString("osnombre"),
                                rs.getDouble("osdesc")
                            );
                            return new Paciente(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("dni"),
                                os
                            );
                        } else {
                            return null;
                        }
                    }
                }
            }
        }.ejecutar();
    }

    @Override
    public Integer guardar(Paciente paciente) throws DataAccessException {
        return new OperacionBD<Integer>() {
            @Override
            protected Integer ejecutarOperacion(Connection c) throws SQLException {
                String sql =
                  "INSERT INTO pacientes (nombre, dni, obra_social_id) VALUES (?, ?, ?)";
                try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, paciente.getNombre());
                    ps.setString(2, paciente.getDni());
                    ps.setInt(3, paciente.getObraSocial().getId());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            return keys.getInt(1);
                        } else {
                            throw new SQLException("No se pudo obtener ID generado para paciente");
                        }
                    }
                }
            }
        }.ejecutar();
    }

    @Override
    public void actualizar(Paciente paciente) throws DataAccessException {
        new OperacionBD<Void>() {
            @Override
            protected Void ejecutarOperacion(Connection c) throws SQLException {
                String sql =
                  "UPDATE pacientes " +
                  "SET nombre = ?, dni = ?, obra_social_id = ? " +
                  "WHERE id = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, paciente.getNombre());
                    ps.setString(2, paciente.getDni());
                    ps.setInt(3, paciente.getObraSocial().getId());
                    ps.setInt(4, paciente.getId());
                    ps.executeUpdate();
                }
                return null;
            }
        }.ejecutar();
    }

    @Override
    public void eliminar(Integer id) throws DataAccessException {
        new OperacionBD<Void>() {
            @Override
            protected Void ejecutarOperacion(Connection c) throws SQLException {
                String sql = "DELETE FROM pacientes WHERE id = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                return null;
            }
        }.ejecutar();
    }
}
