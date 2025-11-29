package servicio;

import java.time.LocalDate;
import java.util.List;

import dao.TurnoDAO;
import modelo.Turno;
import modelo.Medico;
import modelo.Paciente;
import modelo.ObraSocial;
import conexiondb.DataAccessException;
import servicio.excepciones.ServicioException;

public class TurnoService {

    private final TurnoDAO turnoDAO;
    private final MedicoService medicoService;
    private final PacienteService pacienteService;

    public TurnoService(TurnoDAO turnoDAO,MedicoService medicoService,PacienteService pacienteService) {
        this.turnoDAO        = turnoDAO;
        this.medicoService   = medicoService;
        this.pacienteService = pacienteService;
    }

    public List<Turno> obtenerTodos() throws ServicioException {
        try {
            return turnoDAO.obtenerTodos();
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al obtener lista de turnos", ex);
        }
    }

    public List<Turno> obtenerPorMedicoYFecha(Medico medico, LocalDate fecha) throws ServicioException {
        try {
            medicoService.obtenerPorId(medico.getId());
            return turnoDAO.obtenerPorMedicoYFecha(medico, fecha);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al obtener turnos", ex);
        }
    }

    public Integer guardar(Turno turno) throws ServicioException {
        try {
            if (turno.getMedico() == null || turno.getPaciente() == null || turno.getFechaHora() == null) {
                throw new ServicioException("Médico, paciente y fecha/hora son obligatorios");
            }
            medicoService.obtenerPorId(turno.getMedico().getId());
            pacienteService.obtenerPorId(turno.getPaciente().getId());

            List<Turno> existentes = turnoDAO.obtenerPorMedicoYFecha(
                turno.getMedico(), turno.getFechaHora().toLocalDate()
            );
            boolean duplicado = existentes.stream()
                .anyMatch(t -> t.getFechaHora().equals(turno.getFechaHora()));
            if (duplicado) {
                throw new ServicioException(
                    "Ya existe un turno para el médico " 
                    + turno.getMedico().getNombre() 
                    + " a las " 
                    + turno.getFechaHora().toLocalTime()
                );
            }

            List<Turno> ocupados = turnoDAO.obtenerPorConsultorioYRango(
                turno.getConsultorio(),
                turno.getFechaHora().toLocalDate(),
                turno.getFechaHora().toLocalDate()
            );
            boolean consultorioOcupado = ocupados.stream()
                .anyMatch(t -> t.getFechaHora().equals(turno.getFechaHora()));
            if (consultorioOcupado) {
                throw new ServicioException(
                    "El consultorio " 
                    + turno.getConsultorio().getNombre() 
                    + " ya está ocupado a esa hora"
                );
            }

            return turnoDAO.guardar(turno);

        } catch (DataAccessException ex) {
            throw new ServicioException("Error al guardar turno", ex);
        }
    }

    public void eliminar(Integer id) throws ServicioException {
        try {
            turnoDAO.eliminar(id);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al eliminar turno con ID " + id, ex);
        }
    }

    public List<Turno> obtenerPorMedicoYRangoFechas(Medico medico,LocalDate inicio,LocalDate fin) throws ServicioException {
        try {
            medicoService.obtenerPorId(medico.getId());
            return turnoDAO.obtenerPorMedicoYRangoFechas(medico, inicio, fin);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al obtener turnos en rango de fechas", ex);
        }
    }
    
    public double calcularPrecioConDescuento(Turno turno) {
        double valor = turno.getMedico().getValorConsulta();
        ObraSocial osMedico   = turno.getMedico().getObraSocial();
        ObraSocial osPaciente = turno.getPaciente().getObraSocial();
        if (osMedico != null && osMedico.equals(osPaciente)) {
            valor *= 1 - osMedico.getPorcentajeDescuento();
        }
        return valor;
    }
}
