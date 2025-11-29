package interfaz.tabla;

import modelo.Consultorio;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ConsultorioTableModel extends AbstractTableModel {
    private final String[] columnas = { "ID", "Nombre", "Ubicación" };
    private List<Consultorio> contenido = new ArrayList<>();

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
        Consultorio c = contenido.get(row);
        switch (col) {
            case 0: return c.getId();
            case 1: return c.getNombre();
            case 2: return c.getUbicacion();
            default: return null;
        }
    }

    public void setContenido(List<Consultorio> lista) {
        this.contenido = lista;
        fireTableDataChanged();
    }

    public void removeRow(int row) {
        contenido.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public Consultorio getConsultorioAt(int row) {
        return contenido.get(row);
    }
}
