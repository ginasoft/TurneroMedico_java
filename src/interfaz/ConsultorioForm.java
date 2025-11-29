package interfaz;

import modelo.Consultorio;
import servicio.ConsultorioService;
import servicio.excepciones.ServicioException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConsultorioForm extends JFrame implements ActionListener {
    private JLabel lblNombre, lblUbicacion;
    private JTextField txtNombre, txtUbicacion;
    private JButton btnGuardar, btnLimpiar;
    private final ConsultorioService servicio;

    public ConsultorioForm(ConsultorioService servicio) {
        super("Alta de Consultorio");
        this.servicio = servicio;
        configurarVentana();
        inicializarComponentes();
        ubicarComponentes();
        registrarEventos();
    }

    private void configurarVentana() {
        setLayout(new FlowLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void inicializarComponentes() {
        lblNombre     = new JLabel("Nombre:");
        txtNombre     = new JTextField(20);
        lblUbicacion  = new JLabel("Ubicación:");
        txtUbicacion  = new JTextField(20);
        btnGuardar    = new JButton("Guardar");
        btnLimpiar    = new JButton("Limpiar");
    }

    private void ubicarComponentes() {
        add(lblNombre);
        add(txtNombre);
        add(lblUbicacion);
        add(txtUbicacion);
        add(btnGuardar);
        add(btnLimpiar);
        pack();
        setLocationRelativeTo(null);
    }

    private void registrarEventos() {
        btnGuardar.addActionListener(this);
        btnLimpiar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGuardar) {
            guardarConsultorio();
        } else if (e.getSource() == btnLimpiar) {
            limpiarCampos();
        }
    }

    private void guardarConsultorio() {
        String nombre    = txtNombre.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        if (nombre.isEmpty() || ubicacion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Complete todos los campos.",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Integer id = servicio.guardar(new Consultorio(null, nombre, ubicacion));
            JOptionPane.showMessageDialog(this,
                "Consultorio guardado con ID: " + id,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            dispose(); 
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar consultorio:\n" + ex.getMessage(),
                "Error de Servicio",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtUbicacion.setText("");
    }
}
