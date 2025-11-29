package interfaz.tabla;

import servicio.ReporteService;
import servicio.excepciones.ServicioException;
import modelo.MedicoRecaudacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ReporteRecaudacionGridPanel extends JPanel implements ActionListener {
    private final ReporteService reporteService;
    private final JTextField txtDesde;
    private final JTextField txtHasta;
    private final JButton btnGenerar;
    private final JTable tabla;
    private final ReporteRecaudacionTableModel model;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ReporteRecaudacionGridPanel(ReporteService reporteService) {
        this.reporteService = reporteService;
        setLayout(new BorderLayout());

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.add(new JLabel("Desde (YYYY-MM-DD):"));
        txtDesde = new JTextField(10);
        panelFiltros.add(txtDesde);
        panelFiltros.add(new JLabel("Hasta (YYYY-MM-DD):"));
        txtHasta = new JTextField(10);
        panelFiltros.add(txtHasta);
        btnGenerar = new JButton("Generar Recaudación");
        btnGenerar.addActionListener(this);
        panelFiltros.add(btnGenerar);
        add(panelFiltros, BorderLayout.NORTH);

        model = new ReporteRecaudacionTableModel();
        tabla = new JTable(model);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGenerar) {
            generarRecaudacion();
        }
    }

    private void generarRecaudacion() {
        LocalDate desde, hasta;
        try {
            desde = LocalDate.parse(txtDesde.getText().trim(), fmt);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                this, "Fecha 'Desde' inválida.", "Error de Validación", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        try {
            hasta = LocalDate.parse(txtHasta.getText().trim(), fmt);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                this, "Fecha 'Hasta' inválida.", "Error de Validación", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            List<MedicoRecaudacion> datos =
                reporteService.listarRecaudacionPorMedicoEntreFechas(desde, hasta);
            model.setContenido(datos);
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(
                this, "Error al generar recaudación:\n" + ex.getMessage(),
                "Error de Servicio", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
