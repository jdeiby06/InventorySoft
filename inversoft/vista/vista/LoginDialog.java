package vista;

import controlador.ControladorUsuario;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class LoginDialog {

    public static Usuario promptLogin(Frame parent, ControladorUsuario ctrlUsuario, String rolSeleccionado) {
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Mostrar rol seleccionado
        JLabel lblRol = new JLabel("Rol:");
        JLabel lblRolValue = new JLabel(rolSeleccionado);
        lblRolValue.setFont(new Font("Arial", Font.BOLD, 12));
        lblRolValue.setForeground(new Color(0, 102, 204));
        
        JTextField txtEmail = new JTextField();
        JPasswordField txtPass = new JPasswordField();

        panel.add(new JLabel("Rol:"));
        panel.add(lblRolValue);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPass);

        int option = JOptionPane.showOptionDialog(parent, panel, "Iniciar Sesión - " + rolSeleccionado,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, new Object[]{"Entrar", "Cancelar"}, "Entrar");

        if (option != JOptionPane.OK_OPTION) {
            return null;
        }

        String email = txtEmail.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Ingresa email y contraseña", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Usuario u = ctrlUsuario.autenticar(email, pass);
        if (u == null) {
            JOptionPane.showMessageDialog(parent, "Credenciales inválidas", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return u;
    }

}

