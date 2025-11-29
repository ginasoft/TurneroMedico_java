package dao;

import java.util.List;
import modelo.ObraSocial;
import conexiondb.DataAccessException;

public interface ObraSocialDAO {
    List<ObraSocial> obtenerTodos() throws DataAccessException;
    ObraSocial obtenerPorId(Integer id) throws DataAccessException;
    Integer guardar(ObraSocial obraSocial) throws DataAccessException;
    void actualizar(ObraSocial obraSocial) throws DataAccessException;
    void eliminar(Integer id) throws DataAccessException;
}
