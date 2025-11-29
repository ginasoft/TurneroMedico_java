package dao.impl.h2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import modelo.Medico;
import modelo.ObraSocial;
import dao.MedicoDAO;
import conexiondb.OperacionBD;
import conexiondb.DataAccessException;

public class MedicoDAOH2Impl implements MedicoDAO {

    @Override
    public List<Medico> obtenerTodos() throws DataAccessException {
        return new OperacionBD<List<Medico>>() {
            @Override
            protected List<Medico> ejecutarOperacion(Connection c) throws SQLException {
                List<Medico> lista = new ArrayList<>();
                String sql =
                  "SELECT m.id, m.nombre, m.valor_consulta, " +
                  "       os.id AS osid, os.nombre AS osnombre, os.porcentaje_descuento AS osdesc " +
                  "FROM medicos m " +
                  "JOIN obras_sociales os ON m.obra_social_id = os.id";
                try (Statement st = c.createStatement();
                     ResultSet rs = st.executeQuery(sql)) {
                    while (rs.next()) {
                        ObraSocial os = new ObraSocial(
                            rs.getInt("osid"),
                            rs.getString("osnombre"),
                            rs.getDouble("osdesc")
                        );
                        lista.add(new Medico(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getDouble("valor_consulta"),
                            os
                        ));
                    }
                }
                return lista;
            }
        }.ejecutar();
    }

    @Override
    public Medico obtenerPorId(Integer id) throws DataAccessException {
        return new OperacionBD<Medico>() {
            @Override
            protected Medico ejecutarOperacion(Connection c) throws SQLException {
                String sql =
                  "SELECT m.id, m.nombre, m.valor_consulta, " +
                  "       os.id AS osid, os.nombre AS osnombre, os.porcentaje_descuento AS osdesc " +
                  "FROM medicos m " +
                  "JOIN obras_sociales os ON m.obra_social_id = os.id " +
                  "WHERE m.id = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            ObraSocial os = new ObraSocial(
                                rs.getInt("osid"),
                                rs.getString("osnombre"),
                                rs.getDouble("osdesc")
                            );
                            return new Medico(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getDouble("valor_consulta"),
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
    public Integer guardar(Medico medico) throws DataAccessException {
        return new OperacionBD<Integer>() {
            @Override
            protected Integer ejecutarOperacion(Connection c) throws SQLException {
                String sql = 
                  "INSERT INTO medicos (nombre, valor_consulta, obra_social_id) VALUES (?, ?, ?)";
                try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, medico.getNombre());
                    ps.setDouble(2, medico.getValorConsulta());
                    ps.setInt(3, medico.getObraSocial().getId());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            return keys.getInt(1);
                        } else {
                            throw new SQLException("No se pudo obtener ID generado para médico");
                        }
                    }
                }
            }
        }.ejecutar();
    }

    @Override
    public void actualizar(Medico medico) throws DataAccessException {
        new OperacionBD<Void>() {
            @Override
            protected Void ejecutarOperacion(Connection c) throws SQLException {
                String sql =
                  "UPDATE medicos " +
                  "SET nombre = ?, valor_consulta = ?, obra_social_id = ? " +
                  "WHERE id = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, medico.getNombre());
                    ps.setDouble(2, medico.getValorConsulta());
                    ps.setInt(3, medico.getObraSocial().getId());
                    ps.setInt(4, medico.getId());
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
                String sql = "DELETE FROM medicos WHERE id = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                return null;
            }
        }.ejecutar();
    }
}
