package vista;

import controlador.ControladorUsuario;
import modelo.Usuario;
import util.ImageUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo modal mejorado para autenticación por correo y contraseña.
 */
public class LoginDialog {

    private static boolean volverAtras = false;

    public static Usuario promptLogin(Frame parent, ControladorUsuario ctrlUsuario, String rolSeleccionado) {
        volverAtras = false;
        
        // Crear diálogo personalizado
        JDialog dialog = new JDialog(parent, "Iniciar Sesión", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(600, 550);
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);

        // Panel principal con gradiente
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(52, 152, 219),
                        0, getHeight(), new Color(41, 128, 185));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelPrincipal.setLayout(new BorderLayout());

        // Panel superior con logo y título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(41, 128, 185));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));

        // Logo
        ImageIcon logo = ImageUtils.loadAndScaleImage("inventorysoft_logo.png", 100, 100);
        JLabel lblLogo = new JLabel(logo);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTitulo.add(lblLogo);
        panelTitulo.add(Box.createVerticalStrut(10));

        JLabel lblTitulo = new JLabel("🔐 INICIAR SESIÓN");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRolInfo = new JLabel("Rol: " + rolSeleccionado);
        lblRolInfo.setFont(new Font("Arial", Font.BOLD, 14));
        lblRolInfo.setForeground(new Color(200, 220, 255));
        lblRolInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelTitulo.add(lblTitulo);
        panelTitulo.add(Box.createVerticalStrut(5));
        panelTitulo.add(lblRolInfo);
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);

        // Panel central con campos de formulario
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 60, 40, 60));
        panelCentral.setOpaque(false);

        // Campo Email
        JLabel lblEmail = new JLabel("📧 Email:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 13));
        lblEmail.setForeground(Color.WHITE);
        JTextField txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 13));
        txtEmail.setPreferredSize(new Dimension(350, 40));
        txtEmail.setMaximumSize(new Dimension(350, 40));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Campo Contraseña
        JLabel lblPass = new JLabel("🔑 Contraseña:");
        lblPass.setFont(new Font("Arial", Font.BOLD, 13));
        lblPass.setForeground(Color.WHITE);
        JPasswordField txtPass = new JPasswordField();
        txtPass.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPass.setPreferredSize(new Dimension(350, 40));
        txtPass.setMaximumSize(new Dimension(350, 40));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        panelCentral.add(lblEmail);
        panelCentral.add(Box.createVerticalStrut(8));
        panelCentral.add(txtEmail);
        panelCentral.add(Box.createVerticalStrut(20));
        panelCentral.add(lblPass);
        panelCentral.add(Box.createVerticalStrut(8));
        panelCentral.add(txtPass);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(41, 128, 185));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));

        // Array para almacenar el resultado
        final Usuario[] resultado = {null};

        // Botón Entrar
        JButton btnEntrar = new JButton("✅ ENTRAR");
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 13));
        btnEntrar.setBackground(new Color(39, 174, 96));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setPreferredSize(new Dimension(150, 45));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEntrar.setBackground(new Color(27, 148, 77));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEntrar.setBackground(new Color(39, 174, 96));
            }
        });
        btnEntrar.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword());

            if (email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "📋 Por favor ingresa email y contraseña", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Usuario u = ctrlUsuario.autenticar(email, pass);
            if (u == null) {
                JOptionPane.showMessageDialog(dialog, "❌ Credenciales inválidas.\nVerifica tu email y contraseña.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                txtPass.setText("");
                return;
            }
            resultado[0] = u;
            Transition.fadeOutAndDispose(dialog);
        });

        // Botón Volver
        JButton btnVolver = new JButton("⬅️ VOLVER");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 13));
        btnVolver.setBackground(new Color(149, 165, 166));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setPreferredSize(new Dimension(150, 45));
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVolver.setBackground(new Color(127, 140, 141));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVolver.setBackground(new Color(149, 165, 166));
            }
        });
        btnVolver.addActionListener(e -> {
            volverAtras = true;
            Transition.fadeOutAndDispose(dialog);
        });

        panelBotones.add(btnEntrar);
        panelBotones.add(btnVolver);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

    dialog.add(panelPrincipal);
    Transition.fadeIn(dialog);

        // Si presionó volver, retornar null pero con bandera
        if (volverAtras) {
            return new Usuario(-1, "", "", "", "") {
                @Override
                public int getId() {
                    return -999; // Código especial para indicar "volver"
                }
            };
        }

        return resultado[0];
    }
}
