package interfaz.tabla;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.Medico;

public class MedicoTableModel extends AbstractTableModel {

    private static final int COLUMNA_NOMBRE         = 0;
    private static final int COLUMNA_VALOR_CONSULTA = 1;
    private static final int COLUMNA_OBRA_SOCIAL    = 2;

    private String[] nombres = { "Nombre", "Valor Consulta", "Obra Social" };
    private Class<?>[] tipos = { String.class, Double.class, String.class };

    private List<Medico> contenido = new ArrayList<>();

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
        Medico m = contenido.get(row);
        switch (col) {
            case COLUMNA_NOMBRE:
                return m.getNombre();
            case COLUMNA_VALOR_CONSULTA:
                return m.getValorConsulta();
            case COLUMNA_OBRA_SOCIAL:
                return m.getObraSocial() != null
                     ? m.getObraSocial().getNombre()
                     : "";
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setContenido(List<Medico> lista) {
        this.contenido = lista;
        fireTableDataChanged();
    }

    public void addRow(Medico m) {
        contenido.add(m);
        int idx = contenido.size() - 1;
        fireTableRowsInserted(idx, idx);
    }

    public Medico getMedicoAt(int row) {
        return contenido.get(row);
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
}
