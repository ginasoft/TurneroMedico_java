package dao;

import java.util.List;
import modelo.Medico;
import conexiondb.DataAccessException;

public interface MedicoDAO {

    List<Medico> obtenerTodos() throws DataAccessException;
    Medico obtenerPorId(Integer id) throws DataAccessException;
    Integer guardar(Medico medico) throws DataAccessException;
    void actualizar(Medico medico) throws DataAccessException;
    void eliminar(Integer id) throws DataAccessException;
}
