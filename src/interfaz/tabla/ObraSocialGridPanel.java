package interfaz.tabla;

import modelo.ObraSocial;
import servicio.ObraSocialService;
import servicio.excepciones.ServicioException;
import interfaz.ObraSocialForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ObraSocialGridPanel extends JPanel implements ActionListener {
    private final ObraSocialService service;
    private final JTable tabla;
    private final ObraSocialTableModel model;
    private final JButton btnNuevo, btnRefrescar, btnEliminar;

    public ObraSocialGridPanel(ObraSocialService service) {
        this.service = service;
        setLayout(new BorderLayout());

        model = new ObraSocialTableModel();
        tabla = new JTable(model);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botPanel = new JPanel(new FlowLayout());
        btnNuevo      = new JButton("Nuevo");
        btnRefrescar  = new JButton("Refrescar");
        btnEliminar   = new JButton("Eliminar");

        btnNuevo.addActionListener(e -> {
            ObraSocialForm f = new ObraSocialForm(service);
            f.addWindowListener(new WindowAdapter() {
                @Override public void windowClosed(WindowEvent e) {
                    cargarDatos();
                }
            });
            f.setVisible(true);
        });
        btnRefrescar.addActionListener(this);
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        botPanel.add(btnNuevo);
        botPanel.add(btnRefrescar);
        botPanel.add(btnEliminar);
        add(botPanel, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        try {
            List<ObraSocial> lista = service.obtenerTodas();
            model.setContenido(lista);
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(this,
                "No se pudieron cargar obras sociales:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSeleccionado() {
        int row = tabla.getSelectedRow();
        if (row<0) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = tabla.convertRowIndexToModel(row);
        ObraSocial os = model.getObraSocialAt(modelRow);
        if (JOptionPane.showConfirmDialog(this,
            "Eliminar “"+os.getNombre()+"”?", "Confirmar", JOptionPane.YES_NO_OPTION)
            != JOptionPane.YES_OPTION) return;
        try {
            service.eliminar(os.getId());
            model.removeRow(modelRow);
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al eliminar:\n"+ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        cargarDatos();
    }
}
