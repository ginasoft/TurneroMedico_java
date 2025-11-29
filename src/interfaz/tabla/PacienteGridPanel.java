package interfaz.tabla;

import servicio.ObraSocialService;
import servicio.PacienteService;
import servicio.excepciones.ServicioException;
import modelo.Paciente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;   
import java.awt.event.WindowEvent;   
import java.util.List;

import interfaz.PacienteForm; 

public class PacienteGridPanel extends JPanel implements ActionListener {

    private final PacienteService servicio;
    private final JTable tabla;
    private final PacienteTableModel model;
    private final ObraSocialService osService;
    private final JButton btnNuevo;   
    private final JButton btnRefrescar;
    private final JButton btnEliminar;    

    public PacienteGridPanel(PacienteService servicio, ObraSocialService osService) {
        this.servicio = servicio;
		this.osService = osService;
              		
        setLayout(new BorderLayout());

        model = new PacienteTableModel();
        tabla = new JTable(model);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        btnNuevo = new JButton("Nuevo");
        btnNuevo.addActionListener(e -> {
            PacienteForm form = new PacienteForm(servicio, osService);
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
            List<Paciente> lista = servicio.obtenerTodos();
            model.setContenido(lista);
        } catch (ServicioException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "No se pudieron cargar los pacientes:\n" + ex.getMessage(),
                "Error al cargar datos",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un paciente para eliminar.",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        int modeloRow = tabla.convertRowIndexToModel(fila);
        Paciente p = model.getPacienteAt(modeloRow);  
        int resp = JOptionPane.showConfirmDialog(this,
            "¿Eliminar al paciente “" + p.getNombre() + "”?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );
        if (resp != JOptionPane.YES_OPTION) return;

        try {
            servicio.eliminar(p.getId());
            model.removeRow(modeloRow); 
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(this,
                "No se pudo eliminar:\n" + ex.getMessage(),
                "Error de Servicio",
                JOptionPane.ERROR_MESSAGE
            );
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
