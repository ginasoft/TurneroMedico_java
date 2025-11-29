package interfaz;

import modelo.ObraSocial;
import servicio.ObraSocialService;
import servicio.excepciones.ServicioException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ObraSocialForm extends JFrame implements ActionListener {
    private final JTextField txtNombre = new JTextField(20);
    private final JButton btnGuardar   = new JButton("Guardar");
    private final JButton btnLimpiar   = new JButton("Limpiar");
    private final ObraSocialService service;

    public ObraSocialForm(ObraSocialService service) {
        super("Alta / Edición de Obra Social");
        this.service = service;
        setLayout(new FlowLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(new JLabel("Nombre:"));
        add(txtNombre);

        add(btnGuardar);
        add(btnLimpiar);

        btnGuardar.addActionListener(this);
        btnLimpiar.addActionListener(this);

        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGuardar) {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Complete todos los campos.",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            try {
                ObraSocial os = new ObraSocial(null, nombre);
                Integer id = service.guardar(os);
                JOptionPane.showMessageDialog(
                    this,
                    "Obra social guardada con ID: " + id,
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
            } catch (ServicioException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar obra social:\n" + ex.getMessage(),
                    "Error de Servicio",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            txtNombre.setText("");
        }
    }
}
