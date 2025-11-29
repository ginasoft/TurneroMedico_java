package interfaz.tabla;

import servicio.TurnoService;
import servicio.excepciones.ServicioException;
import modelo.Turno;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarioPanel extends JPanel implements ActionListener {
    private final TurnoService turnoService;
    private final JComboBox<String> cmbMes;
    private final JComboBox<Integer> cmbAno;
    private final JButton btnCargar;
    private final JTable tblCalendar;
    private CalendarTableModel model;

    public CalendarioPanel(TurnoService turnoService) {
        this.turnoService = turnoService;
        setLayout(new BorderLayout());

        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmbMes = new JComboBox<>();
        for (int m = 1; m <= 12; m++) {
            String nombre = YearMonth.of(2000, m)
                .getMonth()
                .getDisplayName(TextStyle.FULL, Locale.getDefault());
            cmbMes.addItem(nombre);
        }
        cmbAno = new JComboBox<>();
        int añoActual = LocalDate.now().getYear();
        for (int a = añoActual - 5; a <= añoActual + 5; a++) {
            cmbAno.addItem(a);
        }
        cmbMes.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        cmbAno.setSelectedItem(añoActual);
        btnCargar = new JButton("Mostrar Mes");
        btnCargar.addActionListener(this);

        pnlControls.add(new JLabel("Mes:"));
        pnlControls.add(cmbMes);
        pnlControls.add(new JLabel("Año:"));
        pnlControls.add(cmbAno);
        pnlControls.add(btnCargar);
        add(pnlControls, BorderLayout.NORTH);

        model = new CalendarTableModel();
        tblCalendar = new JTable(model);
        tblCalendar.setRowHeight(80);
        add(new JScrollPane(tblCalendar), BorderLayout.CENTER);

        cargarCalendario();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCargar) {
            cargarCalendario();
        }
    }

    private void cargarCalendario() {
        int mes = cmbMes.getSelectedIndex() + 1;
        int año = (Integer) cmbAno.getSelectedItem();
        YearMonth ym = YearMonth.of(año, mes);
        LocalDate inicio = ym.atDay(1);
        LocalDate fin    = ym.atEndOfMonth();
        try {
            List<Turno> turnos = turnoService.obtenerTodos(); 
            Map<LocalDate, List<Turno>> mapa = turnos.stream()
                .filter(t -> {
                    LocalDate d = t.getFechaHora().toLocalDate();
                    return !d.isBefore(inicio) && !d.isAfter(fin);
                })
                .collect(Collectors.groupingBy(t -> t.getFechaHora().toLocalDate()));

            model.updateMonth(ym, mapa);
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error al cargar turnos:\n" + ex.getMessage(),
                "Error de Servicio",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static class CalendarTableModel extends AbstractTableModel {
        private final String[] dias = { "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom" };
        private YearMonth mes;
        private Object[][] datos;

        public void updateMonth(YearMonth ym, Map<LocalDate, List<Turno>> mapa) {
            this.mes = ym;
            int firstDow = ym.atDay(1).getDayOfWeek().getValue(); 
            int days = ym.lengthOfMonth();
            int rows = (firstDow - 1 + days + 6) / 7;
            datos = new Object[rows][7];
            int day = 1;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < 7; c++) {
                    int idx = r * 7 + c;
                    int d = idx - (firstDow - 2);
                    if (d >= 1 && d <= days) {
                        LocalDate fecha = ym.atDay(d);
                        List<Turno> lista = mapa.getOrDefault(fecha, Collections.emptyList());
                        datos[r][c] = "<html><b>" + d + "</b><br/>" + lista.size() + " turno(s)</html>";
                    } else {
                        datos[r][c] = "";
                    }
                }
            }
            fireTableStructureChanged();
        }

        @Override public int getRowCount()    { return datos != null ? datos.length : 0; }
        @Override public int getColumnCount() { return dias.length; }
        @Override public String getColumnName(int c) { return dias[c]; }
        @Override public Object getValueAt(int r, int c) {
            return datos != null ? datos[r][c] : "";
        }
    }
}
