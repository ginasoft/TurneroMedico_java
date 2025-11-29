package interfaz.tabla;

import servicio.MedicoService;
import servicio.ObraSocialService;
import servicio.excepciones.ServicioException;
import modelo.Medico;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import interfaz.MedicoForm;

public class MedicoGridPanel extends JPanel implements ActionListener {

    private final MedicoService servicio;
    private final ObraSocialService osService;  
    private final JTable tabla;
    private final MedicoTableModel model;
    private final JButton btnRefrescar;
    private final JButton btnEliminar;
    private final JButton btnNuevo;

    public MedicoGridPanel(MedicoService servicio, ObraSocialService osService) {
        this.servicio = servicio;
        this.osService  = osService;  
        setLayout(new BorderLayout());

        model = new MedicoTableModel();
        tabla = new JTable(model);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        btnNuevo = new JButton("Nuevo");
        btnNuevo.addActionListener(e -> {
            MedicoForm form = new MedicoForm(servicio, osService);
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
            List<Medico> lista = servicio.obtenerTodos();
            model.setContenido(lista);
        } catch (ServicioException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "No se pudieron cargar los médicos:\n" + ex.getMessage(),
                "Error al cargar datos",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un médico para eliminar.",
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modeloRow = tabla.convertRowIndexToModel(fila);
        Medico m = model.getMedicoAt(modeloRow);
        int resp = JOptionPane.showConfirmDialog(this,
            "¿Eliminar al médico “" + m.getNombre() + "”?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return;

        try {
            servicio.eliminar(m.getId());
            model.removeRow(modeloRow); 
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(this,
                "No se pudo eliminar:\n" + ex.getMessage(),
                "Error de Servicio", JOptionPane.ERROR_MESSAGE);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnRefrescar) {
            cargarDatos();
        } 
        
    }
}
