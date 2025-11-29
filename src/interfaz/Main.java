package interfaz;

import javax.swing.*;
import conexiondb.TableManager;
import conexiondb.DataAccessException;

import dao.MedicoDAO;
import dao.PacienteDAO;
import dao.TurnoDAO;
import dao.ConsultorioDAO;
import dao.ObraSocialDAO;                 
import dao.impl.h2.MedicoDAOH2Impl;
import dao.impl.h2.PacienteDAOH2Impl;
import dao.impl.h2.TurnoDAOH2Impl;
import dao.impl.h2.ConsultorioDAOH2Impl;
import dao.impl.h2.ObraSocialDAOH2Impl; 

import servicio.MedicoService;
import servicio.PacienteService;
import servicio.TurnoService;
import servicio.ConsultorioService;
import servicio.ObraSocialService;      
import servicio.ReporteService;
import servicio.excepciones.ServicioException;

import interfaz.tabla.MedicoGridPanel;
import interfaz.tabla.PacienteGridPanel;
import interfaz.tabla.TurnoGridPanel;
import interfaz.tabla.ReporteGridPanel;
import interfaz.tabla.ConsultorioGridPanel;
import interfaz.tabla.ObraSocialGridPanel;  
import interfaz.tabla.PacienteTurnosPanel;
import interfaz.tabla.ReporteRecaudacionGridPanel;
import interfaz.tabla.CalendarioPanel;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new TableManager().crearTablas();

                MedicoDAO        medicoDAO        = new MedicoDAOH2Impl();
                PacienteDAO      pacienteDAO      = new PacienteDAOH2Impl();
                TurnoDAO         turnoDAO         = new TurnoDAOH2Impl();
                ConsultorioDAO   consultorioDAO   = new ConsultorioDAOH2Impl();
                ObraSocialDAO    obraSocialDAO    = new ObraSocialDAOH2Impl();

                MedicoService     medicoService      = new MedicoService(medicoDAO);
                PacienteService   pacienteService    = new PacienteService(pacienteDAO, turnoDAO);
                TurnoService      turnoService       = new TurnoService(turnoDAO, medicoService, pacienteService);
                ConsultorioService consultorioService = new ConsultorioService(consultorioDAO);
                ObraSocialService obraSocialService  = new ObraSocialService(obraSocialDAO);
                ReporteService    reporteService     = new ReporteService(medicoService, turnoDAO);

                JTabbedPane pestañas = new JTabbedPane();
                pestañas.addTab("Médicos",          new MedicoGridPanel(medicoService, obraSocialService));
                pestañas.addTab("Pacientes",        new PacienteGridPanel(pacienteService, obraSocialService));
                pestañas.addTab("Obras Social",     new ObraSocialGridPanel(obraSocialService));
                pestañas.addTab("Consultorios",     new ConsultorioGridPanel(consultorioService));
                pestañas.addTab("Turnos",           new TurnoGridPanel(turnoService, medicoService, pacienteService, consultorioService));
                pestañas.addTab("Reportes",         new ReporteGridPanel(reporteService, medicoService));
                pestañas.addTab("Mis Turnos",       new PacienteTurnosPanel(pacienteService));
                pestañas.addTab("Recaudación Global", new ReporteRecaudacionGridPanel(reporteService));
                pestañas.addTab("Calendario",       new CalendarioPanel(turnoService));

                JFrame frame = new JFrame("Turnera Médica");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(pestañas);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            } catch (DataAccessException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Error al inicializar la base de datos:\n" + e.getMessage(),
                    "Error de Inicialización",
                    JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            }
        });
    }
}
