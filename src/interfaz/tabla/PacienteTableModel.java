package interfaz.tabla;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.Paciente;

public class PacienteTableModel extends AbstractTableModel {

    private static final int COLUMNA_NOMBRE       = 0;
    private static final int COLUMNA_DNI          = 1;
    private static final int COLUMNA_OBRA_SOCIAL  = 2;

    private String[] nombres = { "Nombre", "DNI", "Obra Social" };
    private Class<?>[] tipos = { String.class, String.class, String.class };

    private List<Paciente> contenido = new ArrayList<>();

    @Override
    public int getRowCount() {
        return contenido.size();
    }

    @Override
    public int getColumnCount() {
        return nombres.length;
    }

    @Override
    public String getColumnName(int col) {
        return nombres[col];
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return tipos[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Paciente p = contenido.get(row);
        switch (col) {
            case COLUMNA_NOMBRE:
                return p.getNombre();
            case COLUMNA_DNI:
                return p.getDni();
            case COLUMNA_OBRA_SOCIAL:
                return p.getObraSocial() != null
                     ? p.getObraSocial().getNombre()
                     : "";
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setContenido(List<Paciente> lista) {
        this.contenido = lista;
        fireTableDataChanged();
    }

    public void addRow(Paciente p) {
        contenido.add(p);
        int idx = contenido.size() - 1;
        fireTableRowsInserted(idx, idx);
    }

    public void removeRow(int row) {
        contenido.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void clear() {
        int n = contenido.size();
        if (n > 0) {
            contenido.clear();
            fireTableRowsDeleted(0, n - 1);
        }
    }

    public Paciente getPacienteAt(int row) {
        return contenido.get(row);
    }
}
