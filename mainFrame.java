package view;

import controller.PrestamoController;
import dao.EstudianteDAO;
import dao.LibroDAO;
import dao.PrestamoDAO;
import model.Estudiante;
import model.Libro;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {
    private Usuario usuarioActual;
    private JTabbedPane tabbedPane;
    private JTable tblLibros, tblEstudiantes, tblPrestamos;

    public MainFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Sistema Biblioteca - Usuario: " + usuario.getNombre() + " (" + usuario.getRol() + ")");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        // 1. Libros
        JPanel panelLibros = new JPanel(new BorderLayout());
        tblLibros = new JTable();
        panelLibros.add(new JScrollPane(tblLibros), BorderLayout.CENTER);
        
        if ("BIBLIOTECARIO".equalsIgnoreCase(usuario.getRol())) {
            JPanel panelBarra = new JPanel();
            JButton btnAgregar = new JButton("Nuevo Libro");
            JButton btnEliminar = new JButton("Eliminar Libro");

            btnAgregar.addActionListener(e -> {
                String titulo = JOptionPane.showInputDialog(this, "Título del libro:");
                String autor = JOptionPane.showInputDialog(this, "Autor:");
                String isbn = JOptionPane.showInputDialog(this, "ISBN:");
                String editorial = JOptionPane.showInputDialog(this, "Editorial:");
                
                if (titulo != null && autor != null && isbn != null) {
                    LibroDAO dao = new LibroDAO();
                    dao.insertar(new Libro(0, titulo, autor, isbn, editorial, 5, 1));
                    cargarLibros();
                }
            });

            btnEliminar.addActionListener(e -> {
                int fila = tblLibros.getSelectedRow();
                if (fila >= 0) {
                    int id = (int) tblLibros.getValueAt(fila, 0);
                    new LibroDAO().eliminar(id);
                    cargarLibros();
                } else {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar una fila primero.");
                }
            });

            panelBarra.add(btnAgregar);
            panelBarra.add(btnEliminar);
            panelLibros.add(panelBarra, BorderLayout.SOUTH);
        }
        tabbedPane.addTab("Libros", panelLibros);

        // 2. Estudiantes
        JPanel panelEstudiantes = new JPanel(new BorderLayout());
        tblEstudiantes = new JTable();
        panelEstudiantes.add(new JScrollPane(tblEstudiantes), BorderLayout.CENTER);

        if ("BIBLIOTECARIO".equalsIgnoreCase(usuario.getRol())) {
            JPanel panelBarraEst = new JPanel();
            JButton btnAgregarEst = new JButton("Nuevo Estudiante");
            JButton btnEliminarEst = new JButton("Eliminar Estudiante");

            btnAgregarEst.addActionListener(e -> {
                String nombre = JOptionPane.showInputDialog(this, "Nombre estudiante:");
                String rut = JOptionPane.showInputDialog(this, "RUT:");
                String curso = JOptionPane.showInputDialog(this, "Curso:");
                String correo = JOptionPane.showInputDialog(this, "Correo:");

                if (nombre != null && rut != null) {
                    EstudianteDAO dao = new EstudianteDAO();
                    dao.insertar(new Estudiante(0, nombre, rut, curso, correo));
                    cargarEstudiantes();
                }
            });

            btnEliminarEst.addActionListener(e -> {
                int fila = tblEstudiantes.getSelectedRow();
                if (fila >= 0) {
                    int id = (int) tblEstudiantes.getValueAt(fila, 0);
                    new EstudianteDAO().eliminar(id);
                    cargarEstudiantes();
                } else {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar una fila primero.");
                }
            });

            panelBarraEst.add(btnAgregarEst);
            panelBarraEst.add(btnEliminarEst);
            panelEstudiantes.add(panelBarraEst, BorderLayout.SOUTH);
        }
        tabbedPane.addTab("Estudiantes", panelEstudiantes);

        // 3. Préstamos
        JPanel panelPrestamos = new JPanel(new BorderLayout());
        tblPrestamos = new JTable();
        panelPrestamos.add(new JScrollPane(tblPrestamos), BorderLayout.CENTER);

        JPanel panelAccion = new JPanel();
        JTextField txtIdEst = new JTextField(5);
        JTextField txtIdLibro = new JTextField(5);
        JButton btnPrestar = new JButton("Registrar Préstamo");

        panelAccion.add(new JLabel("ID Estudiante:"));
        panelAccion.add(txtIdEst);
        panelAccion.add(new JLabel("ID Libro:"));
        panelAccion.add(txtIdLibro);
        panelAccion.add(btnPrestar);
        panelPrestamos.add(panelAccion, BorderLayout.SOUTH);

        tabbedPane.addTab("Préstamos", panelPrestamos);

        add(tabbedPane);

        btnPrestar.addActionListener(e -> {
            try {
                int idEst = Integer.parseInt(txtIdEst.getText().trim());
                int idLib = Integer.parseInt(txtIdLibro.getText().trim());
                
                PrestamoController controller = new PrestamoController();
                controller.procesarPrestamoAsync(idEst, idLib, () -> {
                    cargarLibros();
                    cargarPrestamos();
                });
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese números válidos para los IDs.");
            }
        });

        cargarLibros();
        cargarEstudiantes();
        cargarPrestamos();
    }

    private void cargarLibros() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Título", "Autor", "ISBN", "Stock"}, 0);
        LibroDAO dao = new LibroDAO();
        for (Libro l : dao.obtenerTodos()) {
            model.addRow(new Object[]{l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn(), l.getStock()});
        }
        tblLibros.setModel(model);
    }

    private void cargarEstudiantes() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nombre", "RUT", "Curso"}, 0);
        EstudianteDAO dao = new EstudianteDAO();
        for (Estudiante e : dao.obtenerTodos()) {
            model.addRow(new Object[]{e.getId(), e.getNombre(), e.getRut(), e.getCurso()});
        }
        tblEstudiantes.setModel(model);
    }

    private void cargarPrestamos() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "ID Estudiante", "ID Libro", "Fecha"}, 0);
        PrestamoDAO dao = new PrestamoDAO();
        dao.obtenerTodos().forEach(p -> 
            model.addRow(new Object[]{p.getId(), p.getIdEstudiante(), p.getIdLibro(), p.getFechaPrestamo()})
        );
        tblPrestamos.setModel(model);
    }
}