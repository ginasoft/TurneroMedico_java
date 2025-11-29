package interfaz.tabla;

import servicio.PacienteService;
import servicio.excepciones.ServicioException;
import modelo.Paciente;
import modelo.Turno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PacienteTurnosPanel extends JPanel implements ActionListener {

    private final PacienteService servicio;
    private final JComboBox<Paciente> comboPacientes;
    private final JTextField txtDesde;
    private final JTextField txtHasta;
    private final JButton btnMostrar;
    private final JTable tabla;
    private final TurnoTableModel model;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PacienteTurnosPanel(PacienteService servicio) {
        this.servicio = servicio;
        setLayout(new BorderLayout());

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.add(new JLabel("Paciente:"));
        comboPacientes = new JComboBox<>();
        panelFiltros.add(comboPacientes);

        panelFiltros.add(new JLabel("Desde (YYYY-MM-DD):"));
        txtDesde = new JTextField(10);
        panelFiltros.add(txtDesde);

        panelFiltros.add(new JLabel("Hasta (YYYY-MM-DD):"));
        txtHasta = new JTextField(10);
        panelFiltros.add(txtHasta);

        btnMostrar = new JButton("Mostrar Turnos");
        btnMostrar.addActionListener(this);
        panelFiltros.add(btnMostrar);

        add(panelFiltros, BorderLayout.NORTH);

        model = new TurnoTableModel();
        tabla = new JTable(model);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarPacientes();
    }

    private void cargarPacientes() {
        try {
            List<Paciente> lista = servicio.obtenerTodos();
            DefaultComboBoxModel<Paciente> mModel = new DefaultComboBoxModel<>();
            lista.forEach(mModel::addElement);
            comboPacientes.setModel(mModel);

            comboPacientes.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Paciente) {
                        setText(((Paciente) value).getNombre());
                    } else {
                        setText("");
                    }
                    return this;
                }
            });
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error al cargar pacientes:\n" + ex.getMessage(),
                "Error de Servicio",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnMostrar) {
            mostrarTurnos();
        }
    }

    private void mostrarTurnos() {
        Paciente paciente = (Paciente) comboPacientes.getSelectedItem();
        if (paciente == null) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un paciente.",
                "Error de Validación",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        LocalDate desde, hasta;
        try {
            desde = LocalDate.parse(txtDesde.getText().trim(), fmt);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Fecha 'Desde' inválida.",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        try {
            hasta = LocalDate.parse(txtHasta.getText().trim(), fmt);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Fecha 'Hasta' inválida.",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            List<Turno> turnos = servicio.obtenerTurnosPorPacienteYRango(paciente, desde, hasta);
            model.setContenido(turnos);
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error al obtener turnos:\n" + ex.getMessage(),
                "Error de Servicio",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
