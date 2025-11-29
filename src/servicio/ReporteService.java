package servicio;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import dao.TurnoDAO;
import modelo.Turno;
import modelo.Medico;
import modelo.MedicoRecaudacion;
import conexiondb.DataAccessException;
import servicio.excepciones.ServicioException;

public class ReporteService {

    private final MedicoService medicoService;
    private final TurnoDAO turnoDAO;

    public ReporteService(MedicoService medicoService, TurnoDAO turnoDAO) {
        this.medicoService = medicoService;
        this.turnoDAO      = turnoDAO;
    }

    public List<Turno> obtenerTurnosPorMedicoYRango(Medico medico, LocalDate inicio, LocalDate fin) throws ServicioException {
        try {
            medicoService.obtenerPorId(medico.getId());
            return turnoDAO.obtenerPorMedicoYRangoFechas(medico, inicio, fin);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al obtener reporte de turnos", ex);
        }
    }

    public double calcularRecaudacion(Medico medico,
                                      LocalDate inicio, LocalDate fin) throws ServicioException {
        List<Turno> turnos = obtenerTurnosPorMedicoYRango(medico, inicio, fin);
        return turnos.stream()
                     .mapToDouble(t -> t.getMedico().getValorConsulta())
                     .sum();
    }

    public List<MedicoRecaudacion> listarRecaudacionPorMedicoEntreFechas(LocalDate inicio, LocalDate fin)
            throws ServicioException {
        try {
            List<MedicoRecaudacion> resultado = new ArrayList<>();
            List<Medico> medicos = medicoService.obtenerTodos();
            for (Medico m : medicos) {
                List<Turno> turnos = turnoDAO.obtenerPorMedicoYRangoFechas(m, inicio, fin);
                int cantidad = turnos.size();
                double total  = turnos.stream()
                                      .mapToDouble(t -> t.getMedico().getValorConsulta())
                                      .sum();
                resultado.add(new MedicoRecaudacion(m, cantidad, total));
            }
            return resultado;
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al listar recaudación por médico", ex);
        }
    }

    public int contarTurnos(Medico medico,
                            LocalDate inicio,
                            LocalDate fin) throws ServicioException {
        return obtenerTurnosPorMedicoYRango(medico, inicio, fin).size();
    }
}
