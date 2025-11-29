package interfaz.tabla;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.Turno;

public class TurnoTableModel extends AbstractTableModel {

    private static final int COLUMNA_FECHA   = 0;
    private static final int COLUMNA_MEDICO  = 1;
    private static final int COLUMNA_PACIENTE= 2;

    private String[] nombres = { "Fecha/Hora", "Médico", "Paciente" };
    private Class<?>[] tipos = { String.class, String.class, String.class };

    private List<Turno> contenido = new ArrayList<>();

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
        Turno t = contenido.get(row);
        switch (col) {
            case COLUMNA_FECHA:
                return t.getFechaHora().toString();
            case COLUMNA_MEDICO:
                return t.getMedico().getNombre();
            case COLUMNA_PACIENTE:
                return t.getPaciente().getNombre();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setContenido(List<Turno> lista) {
        this.contenido = lista;
        fireTableDataChanged();
    }
    
    public void removeRow(int row) {
        contenido.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    public Turno getTurnoAt(int row) {
        return contenido.get(row);
    }

    public void clear() {
        int n = contenido.size();
        if (n > 0) {
            contenido.clear();
            fireTableRowsDeleted(0, n - 1);
        }
    }
}
