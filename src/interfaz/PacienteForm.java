package interfaz;

import modelo.Paciente;
import modelo.ObraSocial;
import servicio.PacienteService;
import servicio.ObraSocialService;
import servicio.excepciones.ServicioException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PacienteForm extends JFrame implements ActionListener {
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JLabel lblDni;
    private JTextField txtDni;
    private JLabel lblObraSocial;
    private JComboBox<ObraSocial> cbObraSocial;
    private JButton btnGuardar;
    private JButton btnLimpiar;

    private final PacienteService servicio;
    private final ObraSocialService obraSocialService;

    public PacienteForm(PacienteService servicio, ObraSocialService osService) {
        super("Alta/Edición de Paciente");
        this.servicio = servicio;
        this.obraSocialService = osService;
        configurarVentana();
        inicializarComponentes();
        ubicarComponentes();
        registrarEventos();
        cargarObrasSociales(); 
    }

    private void configurarVentana() {
        setLayout(new FlowLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void inicializarComponentes() {
        lblNombre      = new JLabel("Nombre:");
        txtNombre      = new JTextField(20);
        lblDni         = new JLabel("DNI:");
        txtDni         = new JTextField(15);
        lblObraSocial  = new JLabel("Obra Social:");
        cbObraSocial   = new JComboBox<>();
        btnGuardar     = new JButton("Guardar");
        btnLimpiar     = new JButton("Limpiar");
    }

    private void ubicarComponentes() {
        add(lblNombre);       add(txtNombre);
        add(lblDni);          add(txtDni);
        add(lblObraSocial);   add(cbObraSocial);
        add(btnGuardar);      add(btnLimpiar);
        pack();
        setLocationRelativeTo(null);
    }

    private void registrarEventos() {
        btnGuardar.addActionListener(this);
        btnLimpiar.addActionListener(this);
    }

    private void cargarObrasSociales() {
        try {
            List<ObraSocial> lista = obraSocialService.obtenerTodas();
            DefaultComboBoxModel<ObraSocial> model = new DefaultComboBoxModel<>();
            lista.forEach(model::addElement);
            cbObraSocial.setModel(model);
            cbObraSocial.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list,
                                                              Object value,
                                                              int index,
                                                              boolean isSelected,
                                                              boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof ObraSocial) {
                        setText(((ObraSocial) value).getNombre());
                    } else {
                        setText("");
                    }
                    return this;
                }
            });

        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar obras sociales:\n" + ex.getMessage(),
                "Error de Servicio",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGuardar) {
            guardarPaciente();
        } else if (e.getSource() == btnLimpiar) {
            limpiarCampos();
        }
    }

    private void guardarPaciente() {
        String nombre = txtNombre.getText().trim();
        String dni    = txtDni.getText().trim();
        ObraSocial obra = (ObraSocial) cbObraSocial.getSelectedItem();

        if (nombre.isEmpty() || dni.isEmpty() || obra == null) {
            JOptionPane.showMessageDialog(this,
                "Complete todos los campos.",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Paciente paciente = new Paciente(null, nombre, dni, obra);
            Integer id = servicio.guardar(paciente);
            JOptionPane.showMessageDialog(this,
                "Paciente guardado con ID: " + id,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            dispose();
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar paciente:\n" + ex.getMessage(),
                "Error de Servicio",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDni.setText("");
        cbObraSocial.setSelectedIndex(-1);
    }
}
