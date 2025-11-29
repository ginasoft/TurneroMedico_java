package interfaz.tabla;

import modelo.ObraSocial;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ObraSocialTableModel extends AbstractTableModel {
    private final List<ObraSocial> contenido = new ArrayList<>();

    private final String[] columnas = { "ID", "Nombre" };

    @Override
    public int getRowCount() {
        return contenido.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ObraSocial os = contenido.get(rowIndex);
        switch (columnIndex) {
            case 0: return os.getId();
            case 1: return os.getNombre();
            default: return null;
        }
    }

    public void setContenido(List<ObraSocial> lista) {
        contenido.clear();
        contenido.addAll(lista);
        fireTableDataChanged();
    }

    public ObraSocial getObraSocialAt(int row) {
        return contenido.get(row);
    }

    public void removeRow(int row) {
        contenido.remove(row);
        fireTableRowsDeleted(row, row);
    }
}
