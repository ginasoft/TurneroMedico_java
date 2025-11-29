package servicio;

import java.util.List;
import dao.ConsultorioDAO;
import modelo.Consultorio;
import conexiondb.DataAccessException;
import servicio.excepciones.ServicioException;

public class ConsultorioService {

    private final ConsultorioDAO consultorioDAO;

    public ConsultorioService(ConsultorioDAO consultorioDAO) {
        this.consultorioDAO = consultorioDAO;
    }

    public List<Consultorio> obtenerTodos() throws ServicioException {
        try {
            return consultorioDAO.obtenerTodos();
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al obtener la lista de consultorios", ex);
        }
    }

    public Consultorio obtenerPorId(Integer id) throws ServicioException {
        try {
            return consultorioDAO.obtenerPorId(id);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al buscar consultorio con ID " + id, ex);
        }
    }

    public Integer guardar(Consultorio consultorio) throws ServicioException {
        if (consultorio.getNombre() == null || consultorio.getNombre().isBlank()) {
            throw new ServicioException("El nombre del consultorio no puede estar vacío");
        }
        if (consultorio.getUbicacion() == null || consultorio.getUbicacion().isBlank()) {
            throw new ServicioException("La ubicación del consultorio no puede estar vacía");
        }
        try {
            return consultorioDAO.guardar(consultorio);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al guardar el consultorio", ex);
        }
    }

    public void actualizar(Consultorio consultorio) throws ServicioException {
        if (consultorio.getId() == null) {
            throw new ServicioException("El consultorio debe tener un ID para actualizarse");
        }
        if (consultorio.getNombre() == null || consultorio.getNombre().isBlank()) {
            throw new ServicioException("El nombre del consultorio no puede estar vacío");
        }
        if (consultorio.getUbicacion() == null || consultorio.getUbicacion().isBlank()) {
            throw new ServicioException("La ubicación del consultorio no puede estar vacía");
        }
        try {
            consultorioDAO.actualizar(consultorio);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al actualizar el consultorio", ex);
        }
    }

    public void eliminar(Integer id) throws ServicioException {
        try {
            consultorioDAO.eliminar(id);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al eliminar el consultorio con ID " + id, ex);
        }
    }
}
