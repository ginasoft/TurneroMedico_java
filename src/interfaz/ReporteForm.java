package interfaz;

import servicio.ReporteService;
import servicio.MedicoService;
import interfaz.tabla.ReporteGridPanel;

import javax.swing.*;

public class ReporteForm extends JFrame {

    public ReporteForm(ReporteService reporteService,
                       MedicoService medicoService) {
        super("Reporte de Recaudación");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(new ReporteGridPanel(reporteService, medicoService));
        pack();
        setLocationRelativeTo(null);
    }
}
