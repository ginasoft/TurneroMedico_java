package dao;

import modelo.Consultorio;
import conexiondb.DataAccessException;
import java.util.List;

public interface ConsultorioDAO {
    List<Consultorio> obtenerTodos() throws DataAccessException;
    Consultorio obtenerPorId(Integer id) throws DataAccessException;
    Integer guardar(Consultorio consultorio) throws DataAccessException;
    void actualizar(Consultorio consultorio) throws DataAccessException;
    void eliminar(Integer id) throws DataAccessException;
}
