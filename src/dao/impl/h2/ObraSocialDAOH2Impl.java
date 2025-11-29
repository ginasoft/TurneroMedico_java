package dao.impl.h2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dao.ObraSocialDAO;
import modelo.ObraSocial;
import conexiondb.OperacionBD;
import conexiondb.DataAccessException;

public class ObraSocialDAOH2Impl implements ObraSocialDAO {

    @Override
    public List<ObraSocial> obtenerTodos() throws DataAccessException {
        return new OperacionBD<List<ObraSocial>>() {
            @Override
            protected List<ObraSocial> ejecutarOperacion(Connection c) throws SQLException {
                List<ObraSocial> lista = new ArrayList<>();
                String sql = "SELECT id, nombre, porcentaje_descuento FROM obras_sociales";
                try (Statement st = c.createStatement();
                     ResultSet rs = st.executeQuery(sql)) {
                    while (rs.next()) {
                        lista.add(new ObraSocial(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getDouble("porcentaje_descuento")
                        ));
                    }
                }
                return lista;
            }
        }.ejecutar();
    }

    @Override
    public ObraSocial obtenerPorId(Integer id) throws DataAccessException {
        return new OperacionBD<ObraSocial>() {
            @Override
            protected ObraSocial ejecutarOperacion(Connection c) throws SQLException {
                String sql = "SELECT id, nombre, porcentaje_descuento FROM obras_sociales WHERE id = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return new ObraSocial(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getDouble("porcentaje_descuento")
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
    public Integer guardar(ObraSocial os) throws DataAccessException {
        return new OperacionBD<Integer>() {
            @Override
            protected Integer ejecutarOperacion(Connection c) throws SQLException {
                String sql = "INSERT INTO obras_sociales (nombre, porcentaje_descuento) VALUES (?, ?)";
                try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, os.getNombre());
                    ps.setDouble(2, os.getPorcentajeDescuento());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            return keys.getInt(1);
                        } else {
                            throw new SQLException("No se pudo obtener ID generado para obra social");
                        }
                    }
                }
            }
        }.ejecutar();
    }

    @Override
    public void actualizar(ObraSocial os) throws DataAccessException {
        new OperacionBD<Void>() {
            @Override
            protected Void ejecutarOperacion(Connection c) throws SQLException {
                String sql = "UPDATE obras_sociales SET nombre = ?, porcentaje_descuento = ? WHERE id = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, os.getNombre());
                    ps.setDouble(2, os.getPorcentajeDescuento());
                    ps.setInt(3, os.getId());
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
                String sql = "DELETE FROM obras_sociales WHERE id = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                return null;
            }
        }.ejecutar();
    }
}
