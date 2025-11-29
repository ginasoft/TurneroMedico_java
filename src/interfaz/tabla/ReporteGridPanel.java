package interfaz.tabla;

import servicio.ReporteService;
import servicio.MedicoService;
import servicio.excepciones.ServicioException;
import modelo.Medico;
import modelo.Turno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;  
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReporteGridPanel extends JPanel implements ActionListener {

    private final ReporteService reporteService;
    private final MedicoService medicoService;

    private final JComboBox<Medico> comboMedicos;
    private final JTextField txtFechaInicio;
    private final JTextField txtFechaFin;
    private final JButton btnGenerar;

    private final JTable tabla;
    private final ReporteTableModel model;

    private final JLabel lblTotalTurnos;
    private final JLabel lblTotalRecaudado;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ReporteGridPanel(ReporteService reporteService, MedicoService medicoService) {
        this.reporteService = reporteService;
        this.medicoService   = medicoService;
        setLayout(new BorderLayout());

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.add(new JLabel("Médico:"));
        comboMedicos = new JComboBox<>();
        panelFiltros.add(comboMedicos);

        panelFiltros.add(new JLabel("Desde (YYYY-MM-DD):"));
        txtFechaInicio = new JTextField(10);
        panelFiltros.add(txtFechaInicio);

        panelFiltros.add(new JLabel("Hasta (YYYY-MM-DD):"));
        txtFechaFin = new JTextField(10);
        panelFiltros.add(txtFechaFin);

        btnGenerar = new JButton("Generar Reporte");
        btnGenerar.addActionListener(this);
        panelFiltros.add(btnGenerar);

        add(panelFiltros, BorderLayout.NORTH);

        model = new ReporteTableModel();
        tabla = new JTable(model);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotalTurnos    = new JLabel("Total Turnos: 0");
        lblTotalRecaudado = new JLabel("Total Recaudado: 0.00");
        panelResumen.add(lblTotalTurnos);
        panelResumen.add(lblTotalRecaudado);
        add(panelResumen, BorderLayout.SOUTH);

        cargarMedicos();
    }

    private void cargarMedicos() {
        try {
            List<Medico> medicos = medicoService.obtenerTodos();
            DefaultComboBoxModel<Medico> m = new DefaultComboBoxModel<>();
            for (Medico md : medicos) {
                m.addElement(md);
            }
            comboMedicos.setModel(m);

            comboMedicos.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Medico) {
                        setText(((Medico) value).getNombre());
                    } else {
                        setText("");
                    }
                    return this;
                }
            });

        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(
                this,
                "No se pudieron cargar los médicos:\n" + ex.getMessage(),
                "Error al cargar médicos",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGenerar) {
            generarReporte();
        }
    }
    private void generarReporte() {
        Medico medico = (Medico) comboMedicos.getSelectedItem();
        if (medico == null) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un médico.",
                "Error de Validación",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        LocalDate desde, hasta;
        try {
            desde = LocalDate.parse(txtFechaInicio.getText().trim(), fmt);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Fecha de inicio inválida.",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        try {
            hasta = LocalDate.parse(txtFechaFin.getText().trim(), fmt);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Fecha de fin inválida.",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        try {
            List<Turno> turnos = reporteService.obtenerTurnosPorMedicoYRango(medico, desde, hasta);
            model.setContenido(turnos);

            lblTotalTurnos.setText("Total Turnos: " + turnos.size());
            double total = turnos.stream()
                .mapToDouble(t -> t.getMedico().getValorConsulta())
                .sum();
            lblTotalRecaudado.setText(String.format("Total Recaudado: %.2f", total));
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error al generar el reporte:\n" + ex.getMessage(),
                "Error de Servicio",
                JOptionPane.ERROR_MESSAGE
            );
        }
     
    }
}
