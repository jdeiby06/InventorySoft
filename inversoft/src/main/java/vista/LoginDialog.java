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

    /**
     * Muestra un diálogo para recuperación de contraseña.
     * El usuario ingresa su email y se muestra la contraseña (o un mensaje si no existe).
     */
    private static void mostrarDialogoRecuperacion(JDialog parentDialog, ControladorUsuario ctrlUsuario) {
        JDialog dialogo = new JDialog(parentDialog, "Recuperar Contraseña", true);
        dialogo.setSize(500, 300);
        dialogo.setLocationRelativeTo(parentDialog);
        dialogo.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240));

        JLabel lblTitulo = new JLabel("📧 Recuperar Contraseña");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(15));

        JLabel lblEmail = new JLabel("Ingresa tu correo electrónico:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblEmail);
        panel.add(Box.createVerticalStrut(5));

        JTextField txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(300, 35));
        txtEmail.setMaximumSize(new Dimension(400, 35));
        panel.add(txtEmail);
        panel.add(Box.createVerticalStrut(15));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setBackground(new Color(240, 240, 240));

        JButton btnRecuperar = new JButton("🔑 Recuperar");
        btnRecuperar.setBackground(new Color(39, 174, 96));
        btnRecuperar.setForeground(Color.WHITE);
        btnRecuperar.setFont(new Font("Arial", Font.BOLD, 12));
        btnRecuperar.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Por favor ingresa tu correo", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Usuario usuario = null;
            for (Usuario u : ctrlUsuario.obtenerUsuarios()) {
                if (u.getCorreo().equalsIgnoreCase(email)) {
                    usuario = u;
                    break;
                }
            }
            if (usuario != null) {
                JOptionPane.showMessageDialog(dialogo, 
                    "Tu contraseña es: " + usuario.getContrasena(),
                    "Contraseña Recuperada",
                    JOptionPane.INFORMATION_MESSAGE);
                dialogo.dispose();
            } else {
                JOptionPane.showMessageDialog(dialogo, 
                    "No se encontró un usuario con ese correo.",
                    "Usuario No Encontrado",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(244, 67, 54));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.addActionListener(e -> dialogo.dispose());

        panelBotones.add(btnRecuperar);
        panelBotones.add(btnCancelar);
        panel.add(panelBotones);

        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    public static Usuario promptLogin(Frame parent, ControladorUsuario ctrlUsuario, String rolSeleccionado) {
        volverAtras = false;
        
        // Crear diálogo personalizado con tamaño aumentado
        JDialog dialog = new JDialog(parent, "Iniciar Sesión", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(700, 650);
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

        // Panel central con campos de formulario - aumentado con más espacios
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(40, 80, 50, 80));
        panelCentral.setOpaque(false);

        // Campo Email
        JLabel lblEmail = new JLabel("📧 Email:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 14));
        lblEmail.setForeground(Color.WHITE);
        JTextField txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setPreferredSize(new Dimension(400, 45));
        txtEmail.setMaximumSize(new Dimension(400, 45));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        // Campo Contraseña
        JLabel lblPass = new JLabel("🔑 Contraseña:");
        lblPass.setFont(new Font("Arial", Font.BOLD, 14));
        lblPass.setForeground(Color.WHITE);
        JPasswordField txtPass = new JPasswordField();
        txtPass.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPass.setPreferredSize(new Dimension(400, 45));
        txtPass.setMaximumSize(new Dimension(400, 45));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        panelCentral.add(lblEmail);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(txtEmail);
        panelCentral.add(Box.createVerticalStrut(25));
        panelCentral.add(lblPass);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(txtPass);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        // Panel con botones principales (Entrar y Volver)
        JPanel panelBotonesPrincipal = new JPanel();
        panelBotonesPrincipal.setLayout(new BorderLayout());
        panelBotonesPrincipal.setBackground(new Color(41, 128, 185));

        // Panel inferior con botones - aumentado con más espacios
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(41, 128, 185));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 5));

        // Array para almacenar el resultado
        final Usuario[] resultado = {null};

        // Botón Entrar
        JButton btnEntrar = new JButton("✅ ENTRAR");
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 15));
        btnEntrar.setBackground(new Color(39, 174, 96));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setPreferredSize(new Dimension(170, 55));
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
        btnVolver.setFont(new Font("Arial", Font.BOLD, 15));
        btnVolver.setBackground(new Color(149, 165, 166));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setPreferredSize(new Dimension(170, 55));
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
        panelBotonesPrincipal.add(panelBotones, BorderLayout.CENTER);

        // Panel con botón "Olvidó contraseña?" en la parte inferior
        JPanel panelOlvido = new JPanel();
        panelOlvido.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelOlvido.setBackground(new Color(41, 128, 185));
        panelOlvido.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        
        JButton btnOlvido = new JButton("❓ ¿Olvidó su contraseña?");
        btnOlvido.setFont(new Font("Arial", Font.PLAIN, 10));
        btnOlvido.setContentAreaFilled(false);
        btnOlvido.setBorderPainted(false);
        btnOlvido.setForeground(new Color(200, 220, 255));
        btnOlvido.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOlvido.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnOlvido.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnOlvido.setForeground(new Color(200, 220, 255));
            }
        });
        btnOlvido.addActionListener(e -> {
            mostrarDialogoRecuperacion(dialog, ctrlUsuario);
        });
        panelOlvido.add(btnOlvido);
        panelBotonesPrincipal.add(panelOlvido, BorderLayout.SOUTH);

        panelPrincipal.add(panelBotonesPrincipal, BorderLayout.SOUTH);

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
