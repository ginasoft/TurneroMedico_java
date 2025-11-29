package interfaz;

import modelo.Medico;
import modelo.ObraSocial;
import servicio.MedicoService;
import servicio.ObraSocialService;
import servicio.excepciones.ServicioException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MedicoForm extends JFrame implements ActionListener {
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JLabel lblValorConsulta;
    private JTextField txtValorConsulta;
    private JLabel lblObraSocial;
    private JComboBox<ObraSocial> cbObraSocial;
    private JButton btnGuardar;
    private JButton btnLimpiar;

    private final MedicoService servicio;
    private final ObraSocialService obraSocialService;

    public MedicoForm(MedicoService servicio, ObraSocialService osService) {
        super("Alta/Edición de Médico");
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
        lblNombre        = new JLabel("Nombre:");
        txtNombre        = new JTextField(20);
        lblValorConsulta = new JLabel("Valor Consulta:");
        txtValorConsulta = new JTextField(10);
        lblObraSocial    = new JLabel("Obra Social:");
        cbObraSocial     = new JComboBox<>();
        btnGuardar       = new JButton("Guardar");
        btnLimpiar       = new JButton("Limpiar");
    }

    private void ubicarComponentes() {
        add(lblNombre);        add(txtNombre);
        add(lblValorConsulta); add(txtValorConsulta);
        add(lblObraSocial);    add(cbObraSocial);
        add(btnGuardar);       add(btnLimpiar);
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
            guardarMedico();
        } else if (e.getSource() == btnLimpiar) {
            limpiarCampos();
        }
    }

    private void guardarMedico() {
        String nombre = txtNombre.getText().trim();
        String valorStr = txtValorConsulta.getText().trim();
        ObraSocial obra = (ObraSocial) cbObraSocial.getSelectedItem();

        if (nombre.isEmpty() || valorStr.isEmpty() || obra == null) {
            JOptionPane.showMessageDialog(this,
                "Complete todos los campos.",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Valor de consulta inválido.",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Medico medico = new Medico(null, nombre, valor, obra);
            Integer id = servicio.guardar(medico);
            JOptionPane.showMessageDialog(this,
                "Médico guardado con ID: " + id,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            dispose();
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar médico:\n" + ex.getMessage(),
                "Error de Servicio",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtValorConsulta.setText("");
        cbObraSocial.setSelectedIndex(-1);
    }
}
