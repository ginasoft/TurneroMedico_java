package servicio;

import java.util.List;
import dao.MedicoDAO;
import modelo.Medico;
import conexiondb.DataAccessException;
import servicio.excepciones.ServicioException;

public class MedicoService {

    private final MedicoDAO medicoDAO;

    public MedicoService(MedicoDAO medicoDAO) {
        this.medicoDAO = medicoDAO;
    }

    public List<Medico> obtenerTodos() throws ServicioException {
        try {
            return medicoDAO.obtenerTodos();
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al obtener lista de médicos", ex);
        }
    }

    public Medico obtenerPorId(Integer id) throws ServicioException {
        try {
            return medicoDAO.obtenerPorId(id);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al buscar médico con ID " + id, ex);
        }
    }

    public Integer guardar(Medico medico) throws ServicioException {
        try {
            if (medico.getNombre().isBlank()) {
                throw new ServicioException("El nombre del médico no puede estar vacío");
            }
            if (medico.getValorConsulta() <= 0) {
                throw new ServicioException("El valor de consulta debe ser mayor a cero");
            }
            return medicoDAO.guardar(medico);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al guardar médico", ex);
        }
    }

    public void actualizar(Medico medico) throws ServicioException {
        try {
            if (medico.getId() == null) {
                throw new ServicioException("El médico debe tener un ID para actualizarse");
            }
            if (medico.getNombre().isBlank()) {
                throw new ServicioException("El nombre del médico no puede estar vacío");
            }
            if (medico.getValorConsulta() <= 0) {
                throw new ServicioException("El valor de consulta debe ser mayor a cero");
            }
            medicoDAO.actualizar(medico);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al actualizar médico", ex);
        }
    }

    public void eliminar(Integer id) throws ServicioException {
        try {
            medicoDAO.eliminar(id);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al eliminar médico con ID " + id, ex);
        }
    }
}
