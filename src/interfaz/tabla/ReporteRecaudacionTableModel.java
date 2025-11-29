package interfaz.tabla;

import modelo.MedicoRecaudacion;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

public class ReporteRecaudacionTableModel extends AbstractTableModel {

    private final String[] columnas = { "Médico", "Cantidad Turnos", "Total Recaudado" };
    private List<MedicoRecaudacion> contenido = new ArrayList<>();

    @Override
    public int getRowCount() {
        return contenido.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnas[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        MedicoRecaudacion mr = contenido.get(row);
        switch (col) {
            case 0: return mr.getMedico().getNombre();
            case 1: return mr.getCantidadTurnos();
            case 2: return mr.getTotalRecaudado();
            default: return null;
        }
    }

    public void setContenido(List<MedicoRecaudacion> lista) {
        this.contenido = lista;
        fireTableDataChanged();
    }
}
