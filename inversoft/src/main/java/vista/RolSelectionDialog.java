package vista;

import util.ImageUtils;
import javax.swing.*;
import java.awt.*;

public class RolSelectionDialog {

    /**
     * Muestra un diálogo para que el usuario seleccione su rol.
     * @return El rol seleccionado: "Admin", "Gerente" o "Empleado", o null si cancela
     */
    public static String selectRol(Frame parent) {
        // Crear un diálogo personalizado grande y llamativo
        JDialog dialog = new JDialog(parent, "Seleccionar Rol de Acceso", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(600, 600);
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);

        // Panel principal con fondo degradado
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fondo degradado
                GradientPaint gradient = new GradientPaint(0, 0, new Color(52, 152, 219),
                        0, getHeight(), new Color(41, 128, 185));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelPrincipal.setLayout(new BorderLayout());

        // Panel superior con logo y título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        panelTitulo.setBackground(new Color(41, 128, 185));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Logo
        ImageIcon logo = ImageUtils.loadAndScaleImage("inventorysoft_logo.png", 120, 120);
        JLabel lblLogo = new JLabel(logo);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTitulo.add(lblLogo);
        panelTitulo.add(Box.createVerticalStrut(10));

        JLabel lblTitulo = new JLabel("🏢 SISTEMA DE GESTIÓN DE INVENTARIO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Selecciona tu rol para continuar");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(200, 220, 255));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelTitulo.add(lblTitulo);
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);

        // Panel central con botones
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        panelCentral.setBackground(new Color(52, 152, 219));
        panelCentral.setOpaque(false);

        // Array para almacenar el resultado
        final String[] result = {null};

        // Botón Admin
        JButton btnAdmin = crearBoton("👨‍💼 ADMINISTRADOR", 
                "Acceso total al sistema", 
                new Color(231, 76, 60),
                new Color(192, 57, 43));
        btnAdmin.addActionListener(e -> {
            result[0] = "Admin";
            Transition.fadeOutAndDispose(dialog);
        });

        // Botón Gerente
        JButton btnGerente = crearBoton("👔 GERENTE", 
                "Gestión y supervisión", 
                new Color(52, 73, 94),
                new Color(44, 62, 80));
        btnGerente.addActionListener(e -> {
            result[0] = "Gerente";
            Transition.fadeOutAndDispose(dialog);
        });

        // Botón Empleado
        JButton btnEmpleado = crearBoton("👷 EMPLEADO", 
                "Operaciones básicas", 
                new Color(39, 174, 96),
                new Color(27, 148, 77));
        btnEmpleado.addActionListener(e -> {
            result[0] = "Empleado";
            Transition.fadeOutAndDispose(dialog);
        });

        panelCentral.add(btnAdmin);
        panelCentral.add(Box.createVerticalStrut(20));
        panelCentral.add(btnGerente);
        panelCentral.add(Box.createVerticalStrut(20));
        panelCentral.add(btnEmpleado);

        // Panel inferior con botón Cancelar
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(41, 128, 185));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setBackground(new Color(149, 165, 166));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setPreferredSize(new Dimension(150, 45));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelar.setBackground(new Color(127, 140, 141));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelar.setBackground(new Color(149, 165, 166));
            }
        });
        btnCancelar.addActionListener(e -> {
            result[0] = null;
            Transition.fadeOutAndDispose(dialog);
        });

        panelInferior.add(btnCancelar);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

    dialog.add(panelPrincipal);
    Transition.fadeIn(dialog);

        return result[0];
    }

    /**
     * Crea un botón personalizado grande y atractivo
     */
    private static JButton crearBoton(String titulo, String descripcion, Color colorBase, Color colorOscuro) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getModel().isArmed() ? colorOscuro : colorBase);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(colorOscuro);
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        
        btn.setLayout(new BorderLayout(10, 10));
        btn.setPreferredSize(new Dimension(450, 80));
        btn.setMaximumSize(new Dimension(450, 80));
        btn.setBackground(colorBase);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(230, 230, 230));

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);
        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(3));
        panelTexto.add(lblDesc);

        btn.add(panelTexto, BorderLayout.CENTER);

        return btn;
    }
}

