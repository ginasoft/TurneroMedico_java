package dao.impl.h2;

import dao.ConsultorioDAO;
import modelo.Consultorio;
import conexiondb.OperacionBD;
import conexiondb.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultorioDAOH2Impl implements ConsultorioDAO {

    @Override
    public List<Consultorio> obtenerTodos() throws DataAccessException {
        return new OperacionBD<List<Consultorio>>() {
            @Override
            protected List<Consultorio> ejecutarOperacion(Connection c) throws SQLException {
                List<Consultorio> lista = new ArrayList<>();
                String sql = "SELECT * FROM consultorios";
                try (Statement st = c.createStatement();
                     ResultSet rs = st.executeQuery(sql)) {
                    while (rs.next()) {
                        lista.add(new Consultorio(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("ubicacion")
                        ));
                    }
                }
                return lista;
            }
        }.ejecutar();
    }

    @Override
    public Consultorio obtenerPorId(Integer id) throws DataAccessException {
        return new OperacionBD<Consultorio>() {
            @Override
            protected Consultorio ejecutarOperacion(Connection c) throws SQLException {
                String sql = "SELECT * FROM consultorios WHERE id = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return new Consultorio(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("ubicacion")
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
    public Integer guardar(Consultorio consultorio) throws DataAccessException {
        return new OperacionBD<Integer>() {
            @Override
            protected Integer ejecutarOperacion(Connection c) throws SQLException {
                String sql = "INSERT INTO consultorios(nombre, ubicacion) VALUES(?, ?)";
                try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, consultorio.getNombre());
                    ps.setString(2, consultorio.getUbicacion());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        return keys.getInt(1);
                    }
                }
            }
        }.ejecutar();
    }

    @Override
    public void actualizar(Consultorio consultorio) throws DataAccessException {
        new OperacionBD<Void>() {
            @Override
            protected Void ejecutarOperacion(Connection c) throws SQLException {
                String sql = "UPDATE consultorios SET nombre = ?, ubicacion = ? WHERE id = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, consultorio.getNombre());
                    ps.setString(2, consultorio.getUbicacion());
                    ps.setInt(3, consultorio.getId());
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
                String sql = "DELETE FROM consultorios WHERE id = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                return null;
            }
        }.ejecutar();
    }
}
