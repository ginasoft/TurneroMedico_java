package servicio;

import java.time.LocalDate;
import java.util.List;
import dao.PacienteDAO;
import dao.TurnoDAO;
import modelo.Paciente;
import modelo.Turno;
import conexiondb.DataAccessException;
import servicio.excepciones.ServicioException;

public class PacienteService {

    private final PacienteDAO pacienteDAO;
    private final TurnoDAO turnoDAO; 

    public PacienteService(PacienteDAO pacienteDAO, TurnoDAO turnoDAO) {
        this.pacienteDAO = pacienteDAO;
        this.turnoDAO = turnoDAO;
    }

    public List<Paciente> obtenerTodos() throws ServicioException {
        try {
            return pacienteDAO.obtenerTodos();
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al obtener lista de pacientes", ex);
        }
    }
    public List<Turno> obtenerTurnosPorPacienteYRango(Paciente p, LocalDate inicio, LocalDate fin)
		throws ServicioException {
		try {
		return turnoDAO.obtenerPorPacienteYRangoFechas(p, inicio, fin);
		} catch (DataAccessException ex) {
		throw new ServicioException("Error al obtener turnos del paciente", ex);
		}
		}


    public Paciente obtenerPorId(Integer id) throws ServicioException {
        try {
            return pacienteDAO.obtenerPorId(id);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al buscar paciente con ID " + id, ex);
        }
    }

    public Integer guardar(Paciente paciente) throws ServicioException {
        try {
            if (paciente.getNombre().isBlank()) {
                throw new ServicioException("El nombre del paciente no puede estar vacío");
            }
            if (paciente.getDni().isBlank()) {
                throw new ServicioException("El DNI del paciente no puede estar vacío");
            }
            return pacienteDAO.guardar(paciente);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al guardar paciente", ex);
        }
    }

    public void actualizar(Paciente paciente) throws ServicioException {
        try {
            if (paciente.getId() == null) {
                throw new ServicioException("El paciente debe tener un ID para actualizarse");
            }
            if (paciente.getNombre().isBlank()) {
                throw new ServicioException("El nombre del paciente no puede estar vacío");
            }
            if (paciente.getDni().isBlank()) {
                throw new ServicioException("El DNI del paciente no puede estar vacío");
            }
            pacienteDAO.actualizar(paciente);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al actualizar paciente", ex);
        }
    }

    public void eliminar(Integer id) throws ServicioException {
        try {
            pacienteDAO.eliminar(id);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al eliminar paciente con ID " + id, ex);
        }
    }
}
