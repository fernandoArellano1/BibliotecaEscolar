package controller;

import dao.LibroDAO;
import dao.PrestamoDAO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class PrestamoController {
    private LibroDAO libroDAO;
    private PrestamoDAO prestamoDAO;

    public PrestamoController() {
        this.libroDAO = new LibroDAO();
        this.prestamoDAO = new PrestamoDAO();
    }

    public void procesarPrestamoAsync(int idEstudiante, int idLibro, Runnable onSuccess) {
        new Thread(() -> {
            boolean stockActualizado = libroDAO.descontarStock(idLibro);
            if (stockActualizado) {
                boolean prestamoOk = prestamoDAO.registrarPrestamo(idEstudiante, idLibro);
                SwingUtilities.invokeLater(() -> {
                    if (prestamoOk) {
                        JOptionPane.showMessageDialog(null, "Préstamo registrado exitosamente.");
                        if (onSuccess != null) onSuccess.run();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al registrar el préstamo.");
                    }
                });
            } else {
                SwingUtilities.invokeLater(() -> 
                    JOptionPane.showMessageDialog(null, "El libro seleccionado no tiene stock disponible.")
                );
            }
        }).start();
    }
}