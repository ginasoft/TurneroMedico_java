package dao;

import java.util.List;
import modelo.Paciente;
import conexiondb.DataAccessException;

public interface PacienteDAO {

    List<Paciente> obtenerTodos() throws DataAccessException;
    Paciente obtenerPorId(Integer id) throws DataAccessException;
    Integer guardar(Paciente paciente) throws DataAccessException;
    void actualizar(Paciente paciente) throws DataAccessException;
    void eliminar(Integer id) throws DataAccessException;
}
