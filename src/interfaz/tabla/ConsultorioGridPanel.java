package interfaz.tabla;

import modelo.Consultorio;
import servicio.ConsultorioService;
import servicio.excepciones.ServicioException;
import interfaz.ConsultorioForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ConsultorioGridPanel extends JPanel implements ActionListener {

    private final ConsultorioService servicio;
    private final JTable tabla;
    private final ConsultorioTableModel model;
    private final JButton btnNuevo;
    private final JButton btnRefrescar;
    private final JButton btnEliminar;

    public ConsultorioGridPanel(ConsultorioService servicio) {
        this.servicio = servicio;
        setLayout(new BorderLayout());

        model = new ConsultorioTableModel();
        tabla = new JTable(model);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnNuevo = new JButton("Nuevo");
        btnNuevo.addActionListener(e -> {
            ConsultorioForm form = new ConsultorioForm(servicio);
            form.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    cargarDatos();
                }
            });
            form.setVisible(true);
        });
        botPanel.add(btnNuevo);

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(this);
        botPanel.add(btnRefrescar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        botPanel.add(btnEliminar);

        add(botPanel, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        try {
            List<Consultorio> lista = servicio.obtenerTodos();
            model.setContenido(lista);
        } catch (ServicioException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "No se pudieron cargar los consultorios:\n" + ex.getMessage(),
                "Error al cargar datos",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un consultorio para eliminar.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        int modeloRow = tabla.convertRowIndexToModel(fila);
        Consultorio c = model.getConsultorioAt(modeloRow);

        int resp = JOptionPane.showConfirmDialog(
            this,
            "¿Eliminar consultorio \"" + c.getNombre() + "\"?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );
        if (resp != JOptionPane.YES_OPTION) return;

        try {
            servicio.eliminar(c.getId());
            model.removeRow(modeloRow);
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo eliminar el consultorio:\n" + ex.getMessage(),
                "Error de Servicio",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefrescar) {
            cargarDatos();
        }
    }
}
