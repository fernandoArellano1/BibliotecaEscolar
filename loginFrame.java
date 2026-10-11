package view;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("Acceso - Biblioteca Escolar");
        setSize(360, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 8, 8));

        add(new JLabel("  Correo:"));
        txtCorreo = new JTextField();
        add(txtCorreo);

        add(new JLabel("  Contraseña:"));
        txtContrasena = new JPasswordField();
        add(txtContrasena);

        btnLogin = new JButton("Ingresar");
        add(new JLabel(""));
        add(btnLogin);

        btnLogin.addActionListener(e -> {
            String correo = txtCorreo.getText().trim();
            String pass = new String(txtContrasena.getPassword()).trim();
            
            UsuarioDAO dao = new UsuarioDAO();
            Usuario u = dao.autenticar(correo, pass);

            if (u != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido " + u.getNombre());
                new MainFrame(u).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            }
        });
    }
}