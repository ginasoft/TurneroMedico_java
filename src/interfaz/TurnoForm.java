package interfaz;

import modelo.Consultorio;
import modelo.Medico;
import modelo.Paciente;
import modelo.Turno;
import servicio.ConsultorioService;
import servicio.MedicoService;
import servicio.PacienteService;
import servicio.TurnoService;
import servicio.excepciones.ServicioException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TurnoForm extends JFrame implements ActionListener {
    private JLabel lblMedico;
    private JComboBox<Medico> cbMedico;
    private JLabel lblPaciente;
    private JComboBox<Paciente> cbPaciente;
    private JLabel lblFecha;
    private JTextField txtFecha; // YYYY-MM-DD
    private JLabel lblHora;
    private JTextField txtHora;  // HH:MM
    private JLabel lblConsultorio;
    private JComboBox<Consultorio> cbConsultorio;
    private JButton btnGuardar;
    private JButton btnLimpiar;

    private final TurnoService servicio;
    private final MedicoService medicoServicio;
    private final PacienteService pacienteServicio;
    private final ConsultorioService consultorioServicio; 

    public TurnoForm(TurnoService servicio,
                     MedicoService medicoServicio,
                     PacienteService pacienteServicio,
                     ConsultorioService consultorioServicio) {
        super("Alta de Turno");
        this.servicio           = servicio;
        this.medicoServicio     = medicoServicio;
        this.pacienteServicio   = pacienteServicio;
        this.consultorioServicio = consultorioServicio; 
        configurarVentana();
        inicializarComponentes();
        ubicarComponentes();
        registrarEventos();
        cargarCombos();
    }

    private void configurarVentana() {
        setLayout(new FlowLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void inicializarComponentes() {
        lblMedico       = new JLabel("Médico:");
        cbMedico        = new JComboBox<>();
        lblPaciente     = new JLabel("Paciente:");
        cbPaciente      = new JComboBox<>();
        lblFecha        = new JLabel("Fecha (YYYY-MM-DD):");
        txtFecha        = new JTextField(10);
        lblHora         = new JLabel("Hora (HH:MM):");
        txtHora         = new JTextField(5);
        lblConsultorio  = new JLabel("Consultorio:");
        cbConsultorio   = new JComboBox<>();
        btnGuardar      = new JButton("Guardar");
        btnLimpiar      = new JButton("Limpiar");
    }

    private void ubicarComponentes() {
        add(lblMedico);       add(cbMedico);
        add(lblPaciente);     add(cbPaciente);
        add(lblFecha);        add(txtFecha);
        add(lblHora);         add(txtHora);
        add(lblConsultorio);  add(cbConsultorio); 
        add(btnGuardar);      add(btnLimpiar);
        pack();
        setLocationRelativeTo(null);
    }

    private void registrarEventos() {
        btnGuardar.addActionListener(this);
        btnLimpiar.addActionListener(this);
    }

    private void cargarCombos() {
        try {
            List<Medico> medicos = medicoServicio.obtenerTodos();
            DefaultComboBoxModel<Medico> mModel = new DefaultComboBoxModel<>();
            medicos.forEach(mModel::addElement);
            cbMedico.setModel(mModel);
            cbMedico.setRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Medico) {
                        Medico m = (Medico) value;
                        setText(m.getNombre() + " ($" + m.getValorConsulta() + ")");
                    } else {
                        setText("");
                    }
                    return this;
                }
            });

            List<Paciente> pacientes = pacienteServicio.obtenerTodos();
            DefaultComboBoxModel<Paciente> pModel = new DefaultComboBoxModel<>();
            pacientes.forEach(pModel::addElement);
            cbPaciente.setModel(pModel);
            cbPaciente.setRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(JList<?> list, Object value,int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Paciente) {
                        Paciente p = (Paciente) value;
                        setText(p.getNombre() + " (DNI: " + p.getDni() + ")");
                    } else {
                        setText("");
                    }
                    return this;
                }
            });

            List<Consultorio> consultorios = consultorioServicio.obtenerTodos();
            DefaultComboBoxModel<Consultorio> cModel = new DefaultComboBoxModel<>();
            consultorios.forEach(cModel::addElement);
            cbConsultorio.setModel(cModel);
            cbConsultorio.setRenderer(new DefaultListCellRenderer() {
                public Component getListCellRendererComponent(JList<?> list, Object value,int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Consultorio) {
                        setText(((Consultorio) value).getNombre());
                    } else {
                        setText("");
                    }
                    return this;
                }
            });

        } catch (ServicioException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error al guardar turno:\n" + ex.getCause().getMessage(),
                "Error de Servicio",
                JOptionPane.ERROR_MESSAGE);

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGuardar) {
            guardarTurno();
        } else if (e.getSource() == btnLimpiar) {
            limpiarCampos();
        }
    }

    private void guardarTurno() {
        Medico medico       = (Medico) cbMedico.getSelectedItem();
        Paciente paciente   = (Paciente) cbPaciente.getSelectedItem();
        Consultorio consultorio = (Consultorio) cbConsultorio.getSelectedItem(); 
        String fechaStr     = txtFecha.getText().trim();
        String horaStr      = txtHora.getText().trim();

        if (medico == null || paciente == null || consultorio == null ||
            fechaStr.isEmpty() || horaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Complete todos los campos.",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                "Formato de fecha inválido.",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalTime hora;
        try {
            hora = LocalTime.parse(horaStr);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                "Formato de hora inválido.",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

        try {
            Turno turno = new Turno(null, medico, paciente, fechaHora, consultorio);
            Integer id = servicio.guardar(turno);
            JOptionPane.showMessageDialog(this,
                "Turno guardado con ID: " + id,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            dispose();
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar turno:\n" + ex.getMessage(),
                "Error de Servicio",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        cbMedico.setSelectedIndex(-1);
        cbPaciente.setSelectedIndex(-1);
        cbConsultorio.setSelectedIndex(-1);
        txtFecha.setText("");
        txtHora.setText("");
    }
}
