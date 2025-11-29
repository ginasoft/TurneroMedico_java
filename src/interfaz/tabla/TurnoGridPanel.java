package interfaz.tabla;

import servicio.TurnoService;
import servicio.excepciones.ServicioException;
import servicio.ConsultorioService;
import servicio.MedicoService;        
import servicio.PacienteService; 
import modelo.Turno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;  
import java.awt.event.WindowEvent;
import java.util.List;

import interfaz.TurnoForm;

public class TurnoGridPanel extends JPanel implements ActionListener {

    private final TurnoService servicio;
    private final MedicoService medicoServicio;     
    private final PacienteService pacienteServicio; 
    private final ConsultorioService consultorioServicio;
    private final JTable tabla;
    private final TurnoTableModel model;
    private final JButton btnNuevo; 
    private final JButton btnRefrescar;
    private final JButton btnEliminar;   

    public TurnoGridPanel(TurnoService servicio, MedicoService medicoServicio, PacienteService pacienteServicio, ConsultorioService consultorioServicio) {
        this.servicio = servicio;
        this.medicoServicio   = medicoServicio;     
        this.pacienteServicio = pacienteServicio;   
        this.consultorioServicio = consultorioServicio;  
        setLayout(new BorderLayout());

        model = new TurnoTableModel();
        tabla = new JTable(model);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        btnNuevo = new JButton("Nuevo");
        btnNuevo.addActionListener(e -> {
            TurnoForm form = new TurnoForm(servicio, medicoServicio, pacienteServicio, consultorioServicio);
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
            List<Turno> lista = servicio.obtenerTodos();
            model.setContenido(lista);
        } catch (ServicioException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "No se pudieron cargar los turnos:\n" + ex.getMessage(),
                "Error al cargar datos",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un turno para eliminar.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modeloRow = tabla.convertRowIndexToModel(fila);
        Turno t = model.getTurnoAt(modeloRow);

        if (JOptionPane.showConfirmDialog(this,
                "¿Eliminar turno de " +
                t.getMedico().getNombre() +
                " con " + t.getPaciente().getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            servicio.eliminar(t.getId());
            model.removeRow(modeloRow);
        } catch (ServicioException ex) {
            JOptionPane.showMessageDialog(this,
                "No se pudo eliminar el turno:\n" + ex.getMessage(),
                "Error de Servicio",
                JOptionPane.ERROR_MESSAGE);
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
