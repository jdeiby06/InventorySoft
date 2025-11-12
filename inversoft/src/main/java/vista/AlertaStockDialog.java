package vista;

import controlador.ControladorProducto;
import controlador.ControladorInventario;
import modelo.Producto;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Diálogo que muestra una alerta visual de productos bajo stock y permite reponer.
 */
public class AlertaStockDialog extends JDialog {

    private final List<Producto> productos;
    private final ControladorProducto controladorProducto;
    private final ControladorInventario controladorInventario;
    private final Usuario usuarioAutenticado;

    public AlertaStockDialog(Frame parent, List<Producto> productos, ControladorProducto controladorProducto, ControladorInventario controladorInventario, Usuario usuarioAutenticado) {
        super(parent, "!! ALERTA DE STOCK !!", true);
        this.productos = productos;
        this.controladorProducto = controladorProducto;
        this.controladorInventario = controladorInventario;
        this.usuarioAutenticado = usuarioAutenticado;
        inicializar();
        // Registrar en historial una alerta por cada producto bajo stock
        try {
            String usuarioNombre = (usuarioAutenticado != null) ? usuarioAutenticado.getNombre() : "sistema";
            for (Producto p : productos) {
                // registrar movimiento tipo 'AlertaStock' con cantidad 0 y usuario autenticado
                controladorInventario.registrarMovimiento(p.getId(), p.getNombre(), "AlertaStock", 0, usuarioNombre);
            }
        } catch (Exception e) {
            System.err.println("Error registrando alertas en historial: " + e.getMessage());
        }
    }

    private void inicializar() {
        setSize(600, 420);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Cabecera roja con icono de alerta
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(200, 30, 30));
        header.setBorder(new EmptyBorder(12, 12, 12, 12));
        JLabel lblIcon = new JLabel("⚠️");
        lblIcon.setFont(new Font("Arial", Font.PLAIN, 28));
        lblIcon.setForeground(Color.WHITE);
        header.add(lblIcon, BorderLayout.WEST);
        JLabel lblTitle = new JLabel("ALERTA DE STOCK - Productos por debajo del mínimo");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(lblTitle, BorderLayout.CENTER);
        panel.add(header, BorderLayout.NORTH);

        // Lista de productos en alerta
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Producto p : productos) {
            String linea = String.format("ID:%d  %s — Stock:%d (Min:%d)", p.getId(), p.getNombre(), p.getStockActual(), p.getStockMinimo());
            listModel.addElement(linea);
        }
        JList<String> list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(list);
        panel.add(scroll, BorderLayout.CENTER);

        // Panel inferior con acciones
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnReponer = new JButton("🔁 Reponer seleccionado");
        btnReponer.setBackground(new Color(76, 175, 80));
        btnReponer.setForeground(Color.WHITE);
        btnReponer.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto para reponer", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Producto seleccionado = productos.get(idx);
            String s = JOptionPane.showInputDialog(this, "Cantidad a reponer para '" + seleccionado.getNombre() + "':", "10");
            if (s == null) return;
            try {
                int cantidad = Integer.parseInt(s.trim());
                if (cantidad <= 0) throw new NumberFormatException();
                controladorProducto.incrementarStock(seleccionado.getId(), cantidad);
                JOptionPane.showMessageDialog(this, "Se repuso " + cantidad + " unidades.", "Repuesto", JOptionPane.INFORMATION_MESSAGE);
                // actualizar texto en la lista
                seleccionado.setStockActual(seleccionado.getStockActual() + cantidad);
                listModel.set(idx, String.format("ID:%d  %s — Stock:%d (Min:%d)", seleccionado.getId(), seleccionado.getNombre(), seleccionado.getStockActual(), seleccionado.getStockMinimo()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingresa una cantidad válida", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(149, 165, 166));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.addActionListener(e -> dispose());

        actions.add(btnReponer);
        actions.add(btnCerrar);

        panel.add(actions, BorderLayout.SOUTH);

        add(panel);
    }

    public void mostrar() {
        setVisible(true);
    }
}
