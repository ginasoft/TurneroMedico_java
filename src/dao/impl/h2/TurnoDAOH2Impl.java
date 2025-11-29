package dao.impl.h2;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import modelo.Turno;
import modelo.Consultorio;
import modelo.Medico;
import modelo.Paciente;
import dao.TurnoDAO;
import conexiondb.OperacionBD;
import conexiondb.DataAccessException;

public class TurnoDAOH2Impl implements TurnoDAO {

    @Override
    public List<Turno> obtenerTodos() throws DataAccessException {
        return new OperacionBD<List<Turno>>() {
            @Override
            protected List<Turno> ejecutarOperacion(Connection c) throws SQLException {
                List<Turno> lista = new ArrayList<>();
                String sql =
                    "SELECT t.id, t.fecha_hora, " +
                    "  m.id AS mid, m.nombre AS mnombre, m.valor_consulta, " +
                    "  p.id AS pid, p.nombre AS pnombre, p.dni " +
                    "FROM turnos t " +
                    "JOIN medicos m ON t.medico_id = m.id " +
                    "JOIN pacientes p ON t.paciente_id = p.id";
                try (Statement st = c.createStatement();
                     ResultSet rs = st.executeQuery(sql)) {

                    while (rs.next()) {
                        Medico medico = new Medico(
                            rs.getInt("mid"),
                            rs.getString("mnombre"),
                            rs.getDouble("valor_consulta"),
                            null
                        );
                        Paciente paciente = new Paciente(
                            rs.getInt("pid"),
                            rs.getString("pnombre"),
                            rs.getString("dni"),
                            null
                        );
                        Timestamp ts = rs.getTimestamp("fecha_hora");
                        LocalDateTime fechaHora = ts.toLocalDateTime();
                        lista.add(new Turno(
                            rs.getInt("id"),
                            medico,
                            paciente,
                            fechaHora,
                            null
                        ));
                    }
                }
                return lista;
            }
        }.ejecutar();
    }

    @Override
    public List<Turno> obtenerPorMedicoYFecha(Medico medico, LocalDate fecha) throws DataAccessException {
        return new OperacionBD<List<Turno>>() {
            @Override
            protected List<Turno> ejecutarOperacion(Connection c) throws SQLException {
                List<Turno> lista = new ArrayList<>();
                String sql =
                    "SELECT t.id, t.fecha_hora, " +
                    "  p.id AS pid, p.nombre AS pnombre, p.dni " +
                    "FROM turnos t " +
                    "JOIN pacientes p ON t.paciente_id = p.id " +
                    "WHERE t.medico_id = ? AND CAST(t.fecha_hora AS DATE) = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, medico.getId());
                    ps.setDate(2, Date.valueOf(fecha));
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Paciente paciente = new Paciente(
                                rs.getInt("pid"),
                                rs.getString("pnombre"),
                                rs.getString("dni")
                            );
                            LocalDateTime fechaHora = rs.getTimestamp("fecha_hora")
                                                        .toLocalDateTime();
                            lista.add(new Turno(
                                rs.getInt("id"),
                                medico,
                                paciente,
                                fechaHora
                            ));
                        }
                    }
                }
                return lista;
            }
        }.ejecutar();
    }

    @Override
    public List<Turno> obtenerPorMedicoYRangoFechas(Medico medico,LocalDate fechaInicio, LocalDate fechaFin)
            throws DataAccessException {
        return new OperacionBD<List<Turno>>() {
            @Override
            protected List<Turno> ejecutarOperacion(Connection c) throws SQLException {
                List<Turno> lista = new ArrayList<>();
                String sql =
                    "SELECT t.id, t.fecha_hora, " +
                    "  p.id AS pid, p.nombre AS pnombre, p.dni " +
                    "FROM turnos t " +
                    "JOIN pacientes p ON t.paciente_id = p.id " +
                    "WHERE t.medico_id = ? " +
                    "  AND t.fecha_hora BETWEEN ? AND ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, medico.getId());
                    ps.setTimestamp(2, Timestamp.valueOf(
                        fechaInicio.atStartOfDay()
                    ));
                    ps.setTimestamp(3, Timestamp.valueOf(
                        fechaFin.atTime(LocalTime.MAX)
                    ));
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Paciente paciente = new Paciente(
                                rs.getInt("pid"),
                                rs.getString("pnombre"),
                                rs.getString("dni")
                            );
                            LocalDateTime fechaHora = rs.getTimestamp("fecha_hora").toLocalDateTime();
                            lista.add(new Turno(
                                rs.getInt("id"),
                                medico,
                                paciente,
                                fechaHora
                            ));
                        }
                    }
                }
                return lista;
            }
        }.ejecutar();
    }
    
    
    @Override
    public List<Turno> obtenerPorPacienteYRangoFechas(Paciente paciente, LocalDate inicio, LocalDate fin)
        throws DataAccessException {
      return new OperacionBD<List<Turno>>() {
        @Override
        protected List<Turno> ejecutarOperacion(Connection c) throws SQLException {
            List<Turno> lista = new ArrayList<>();
            String sql =
                "SELECT t.id, t.fecha_hora, " +
                "  m.id AS mid, m.nombre AS mnombre, m.valor_consulta " +
                "FROM turnos t " +
                "JOIN medicos m ON t.medico_id = m.id " +
                "WHERE t.paciente_id = ? " +
                "  AND t.fecha_hora BETWEEN ? AND ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, paciente.getId());
                ps.setTimestamp(2, Timestamp.valueOf(inicio.atStartOfDay()));
                ps.setTimestamp(3, Timestamp.valueOf(fin.atTime(LocalTime.MAX)));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Medico medico = new Medico(
                            rs.getInt("mid"),
                            rs.getString("mnombre"),
                            rs.getDouble("valor_consulta"),
                            null
                        );
                        LocalDateTime fechaHora = rs.getTimestamp("fecha_hora").toLocalDateTime();
                        lista.add(new Turno(
                            rs.getInt("id"),
                            medico,
                            paciente,
                            fechaHora
                        ));
                    }
                }
            }
            return lista;
        }
    }.ejecutar();
   }

    @Override
    public List<Turno> obtenerPorConsultorioYRango(Consultorio consultorio, LocalDate inicio, LocalDate fin)
            throws DataAccessException {
        return new OperacionBD<List<Turno>>() {
            @Override
            protected List<Turno> ejecutarOperacion(Connection c) throws SQLException {
                List<Turno> lista = new ArrayList<>();
                String sql =
                    "SELECT t.id, t.fecha_hora, " +
                    "  m.id AS mid, m.nombre AS mnombre, m.valor_consulta, " +
                    "  p.id AS pid, p.nombre AS pnombre, p.dni " +
                    "FROM turnos t " +
                    "JOIN medicos m ON t.medico_id = m.id " +
                    "JOIN pacientes p ON t.paciente_id = p.id " +
                    "WHERE t.consultorio_id = ? " +
                    "  AND t.fecha_hora BETWEEN ? AND ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, consultorio.getId());
                    ps.setTimestamp(2,
                        Timestamp.valueOf(inicio.atStartOfDay()));
                    ps.setTimestamp(3,
                        Timestamp.valueOf(fin.atTime(LocalTime.MAX)));
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Medico med = new Medico(
                                rs.getInt("mid"),
                                rs.getString("mnombre"),
                                rs.getDouble("valor_consulta"),
                                null
                            );
                            Paciente pac = new Paciente(
                                rs.getInt("pid"),
                                rs.getString("pnombre"),
                                rs.getString("dni")
                            );
                            LocalDateTime fh = rs.getTimestamp("fecha_hora").toLocalDateTime();
                            lista.add(new Turno(
                                rs.getInt("id"),
                                med,
                                pac,
                                fh,
                                consultorio
                            ));
                        }
                    }
                }
                return lista;
            }
        }.ejecutar();
    }

    @Override
    public Integer guardar(Turno turno) throws DataAccessException {
        return new OperacionBD<Integer>() {
            @Override
            protected Integer ejecutarOperacion(Connection c) throws SQLException {
                String sql =
                    "INSERT INTO turnos (medico_id, paciente_id, consultorio_id, fecha_hora) " +
                    "VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, turno.getMedico().getId());
                    ps.setInt(2, turno.getPaciente().getId());
                    ps.setInt(3, turno.getConsultorio().getId());        // <-- nuevo
                    ps.setTimestamp(4, Timestamp.valueOf(turno.getFechaHora()));
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            return keys.getInt(1);
                        } else {
                            throw new SQLException("No se pudo obtener ID generado para turno");
                        }
                    }
                }
            }
        }.ejecutar();
    }


    @Override
    public void eliminar(Integer id) throws DataAccessException {
        new OperacionBD<Void>() {
            @Override
            protected Void ejecutarOperacion(Connection c) throws SQLException {
                String sql = "DELETE FROM turnos WHERE id = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                return null;
            }
        }.ejecutar();
    }
}
