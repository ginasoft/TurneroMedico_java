package dao;

import java.time.LocalDate;
import java.util.List;
import modelo.Turno;
import modelo.Consultorio;
import modelo.Medico;
import modelo.Paciente;
import conexiondb.DataAccessException;

public interface TurnoDAO {

    List<Turno> obtenerTodos() throws DataAccessException;
    List<Turno> obtenerPorMedicoYFecha(Medico medico, LocalDate fecha) throws DataAccessException;
    List<Turno> obtenerPorMedicoYRangoFechas(Medico medico, LocalDate fechaInicio, LocalDate fechaFin) throws DataAccessException;
    List<Turno> obtenerPorPacienteYRangoFechas(Paciente paciente, LocalDate inicio, LocalDate fin) throws DataAccessException;
    List<Turno> obtenerPorConsultorioYRango(Consultorio c, LocalDate inicio, LocalDate fin) throws DataAccessException;
    Integer guardar(Turno turno) throws DataAccessException;
    void eliminar(Integer id) throws DataAccessException;
}
