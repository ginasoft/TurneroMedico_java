package servicio;

import java.util.List;
import dao.ObraSocialDAO;
import modelo.ObraSocial;
import conexiondb.DataAccessException;
import servicio.excepciones.ServicioException;

public class ObraSocialService {
    private final ObraSocialDAO dao;

    public ObraSocialService(ObraSocialDAO dao) {
        this.dao = dao;
    }

    public List<ObraSocial> obtenerTodas() throws ServicioException {
        try {
            return dao.obtenerTodos();
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al obtener obras sociales", ex);
        }
    }

    public ObraSocial obtenerPorId(Integer id) throws ServicioException {
        try {
            return dao.obtenerPorId(id);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al buscar obra social con ID " + id, ex);
        }
    }

    public Integer guardar(ObraSocial os) throws ServicioException {
        try {
            if (os.getNombre().isBlank()) {
                throw new ServicioException("El nombre no puede estar vacío");
            }
            return dao.guardar(os);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al guardar obra social", ex);
        }
    }

    public void actualizar(ObraSocial os) throws ServicioException {
        try {
            if (os.getId() == null) {
                throw new ServicioException("Debe seleccionar una obra social a actualizar");
            }
            if (os.getNombre().isBlank()) {
                throw new ServicioException("El nombre no puede estar vacío");
            }
            dao.actualizar(os);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al actualizar obra social", ex);
        }
    }

    public void eliminar(Integer id) throws ServicioException {
        try {
            dao.eliminar(id);
        } catch (DataAccessException ex) {
            throw new ServicioException("Error al eliminar obra social con ID " + id, ex);
        }
    }
}
