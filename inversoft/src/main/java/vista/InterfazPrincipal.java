package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import controlador.ControladorCategoria;
import controlador.ControladorInventario;
import controlador.ControladorProducto;
import controlador.ControladorUsuario;
import modelo.Categoria;
import modelo.Producto;
import modelo.Usuario;
import modelo.HistorialInventario;
import util.ImageUtils;

public class InterfazPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JPanel panelPrincipal;
    private JTabbedPane tabbedPane;
    
    // Controladores
    private ControladorProducto controladorProducto;
    private ControladorCategoria controladorCategoria;
    private ControladorUsuario controladorUsuario;
    private ControladorInventario controladorInventario;
    // Usuario autenticado
    private modelo.Usuario usuarioAutenticado;
    
    // Componentes para Productos
    private JTable tablaProductos;
    private DefaultTableModel modeloTablaProductos;
    
    // Componentes para Categorías
    private JTable tablaCategorias;
    private DefaultTableModel modeloTablaCategorias;
    
    // Componentes para Usuarios
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTablaUsuarios;
    
    // Componentes para Historial
    private JTable tablaHistorial;
    private DefaultTableModel modeloTablaHistorial;

    public InterfazPrincipal(
            ControladorProducto controladorProducto,
            ControladorCategoria controladorCategoria,
            ControladorUsuario controladorUsuario,
            ControladorInventario controladorInventario,
            modelo.Usuario usuarioAutenticado
    ) {
        this.controladorProducto = controladorProducto;
        this.controladorCategoria = controladorCategoria;
        this.controladorUsuario = controladorUsuario;
        this.controladorInventario = controladorInventario;
        this.usuarioAutenticado = usuarioAutenticado;

        inicializarInterfaz();
    }

    private void inicializarInterfaz() {
        setTitle("Sistema de Gestión de Inventario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Panel principal
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBackground(new Color(240, 240, 240));
        
        // Crear menú
        crearMenuBar();
        
        // Crear pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
        tabbedPane.setBackground(new Color(255, 255, 255));
        
        // Agregar pestañas
        boolean esEmpleado = usuarioAutenticado != null && "Empleado".equalsIgnoreCase(usuarioAutenticado.getRol());
        // Si el usuario es 'Empleado' mostrar Productos, Categorías e Historial (sin Usuarios)
        tabbedPane.addTab("Productos", crearPestañaProductos());
        tabbedPane.addTab("Categorías", crearPestañaCategorias());
        tabbedPane.addTab("Historial", crearPestañaHistorial());
        if (!esEmpleado) {
            tabbedPane.addTab("Usuarios", crearPestañaUsuarios());
        }
        
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);
        
        add(panelPrincipal);
        setVisible(true);
    }

    private void crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(50, 50, 50));
        menuBar.setForeground(Color.WHITE);
        
        // Logo a la izquierda
        ImageIcon logo = ImageUtils.loadAndScaleImage("inventorysoft_logo.png", 50, 50);
        JLabel lblLogo = new JLabel(logo);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menuBar.add(lblLogo);
        menuBar.add(Box.createHorizontalStrut(15));
        
        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setForeground(Color.BLACK);
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);
        
        // Menú Editar
        JMenu menuEditar = new JMenu("Editar");
        menuEditar.setForeground(Color.BLACK);
        JMenuItem itemActualizar = new JMenuItem("Actualizar");
        itemActualizar.addActionListener(e -> actualizarTodas());
        menuEditar.add(itemActualizar);
        
        menuBar.add(menuArchivo);
        menuBar.add(menuEditar);
        // Mostrar usuario autenticado a la derecha
        if (usuarioAutenticado != null) {
            JLabel lblUsuario = new JLabel(usuarioAutenticado.getNombre() + " <" + usuarioAutenticado.getCorreo() + ">");
            lblUsuario.setForeground(Color.WHITE);
            menuBar.add(Box.createHorizontalGlue());
            menuBar.add(lblUsuario);
        }
        
        setJMenuBar(menuBar);
    }

    private JPanel crearPestañaProductos() {
        // Panel con fondo de imagen InventorySoft
        JPanel panel = new JPanel() {
            private ImageIcon bgImage = null;
            private Image scaledImage = null;
            private int lastWidth = -1;
            private int lastHeight = -1;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage == null) {
                    bgImage = ImageUtils.loadImage("inventorysoft_logo.png");
                }
                if (bgImage != null) {
                    // Cachear la imagen escalada para evitar escalarla cada vez
                    if (scaledImage == null || lastWidth != getWidth() || lastHeight != getHeight()) {
                        Image tempImg = bgImage.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                        scaledImage = tempImg;
                        lastWidth = getWidth();
                        lastHeight = getHeight();
                    }
                    // Dibujar imagen de fondo centrada y semi-transparente
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f)); // 8% opacidad
                    g2d.drawImage(scaledImage, 0, 0, this);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // restaurar
                }
            }
        };
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(245, 245, 245));
        panel.setOpaque(true);
        
        // Panel de botones
        JPanel panelBotones = crearPanelBotonesProductos();
        panel.add(panelBotones, BorderLayout.NORTH);
        
        // Tabla de productos
        modeloTablaProductos = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Categoría", "Precio", "Stock Actual", "Stock Mínimo", "Estado"}, 0
        ) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaProductos = new JTable(modeloTablaProductos);
        tablaProductos.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaProductos.setRowHeight(25);
        tablaProductos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaProductos.getTableHeader().setBackground(new Color(70, 130, 180));
        tablaProductos.getTableHeader().setForeground(Color.WHITE);
        tablaProductos.setOpaque(true);
        tablaProductos.setBackground(new Color(255, 255, 255, 220)); // blanco semi-transparente
        
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        cargarProductos();
        
        return panel;
    }

    private JPanel crearPanelBotonesProductos() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        
        boolean esEmpleado = usuarioAutenticado != null && "Empleado".equalsIgnoreCase(usuarioAutenticado.getRol());

        JButton btnAgregarProducto = new JButton("➕ Agregar Producto");
        btnAgregarProducto.setFont(new Font("Arial", Font.BOLD, 11));
        btnAgregarProducto.setBackground(new Color(76, 175, 80));
        btnAgregarProducto.setForeground(Color.WHITE);
        btnAgregarProducto.addActionListener(e -> mostrarDialogoProducto(null));
        
        JButton btnEditarProducto = new JButton("✏️ Editar Producto");
        btnEditarProducto.setFont(new Font("Arial", Font.BOLD, 11));
        btnEditarProducto.setBackground(new Color(255, 193, 7));
        btnEditarProducto.setForeground(Color.WHITE);
        btnEditarProducto.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto en la tabla para editar", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int id = (int) modeloTablaProductos.getValueAt(fila, 0);
            Producto seleccionado = null;
            for (Producto p : controladorProducto.obtenerProductos()) {
                if (p.getId() == id) { seleccionado = p; break; }
            }
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el producto seleccionado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mostrarDialogoProducto(seleccionado);
        });
        
            JButton btnEliminarProducto = new JButton("🗑️ Eliminar Producto");
            btnEliminarProducto.setFont(new Font("Arial", Font.BOLD, 11));
            btnEliminarProducto.setBackground(new Color(244, 67, 54));
            btnEliminarProducto.setForeground(Color.WHITE);
            btnEliminarProducto.addActionListener(e -> {
                int fila = tablaProductos.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(this, "Selecciona un producto en la tabla para eliminar", "Información", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                int id = (int) modeloTablaProductos.getValueAt(fila, 0);
                String nombreProducto = (String) modeloTablaProductos.getValueAt(fila, 1);
            
                int confirmacion = JOptionPane.showConfirmDialog(
                    this, 
                    "¿Estás seguro de que deseas eliminar el producto '" + nombreProducto + "'?\nEsta acción no se puede deshacer.",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
            
                if (confirmacion == JOptionPane.YES_OPTION) {
                    boolean eliminado = controladorProducto.eliminarProducto(id);
                    if (eliminado) {
                        JOptionPane.showMessageDialog(this, "Producto eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarProductos();
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo eliminar el producto", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        
        JButton btnActualizarStock = new JButton("📦 Actualizar Stock");
        btnActualizarStock.setFont(new Font("Arial", Font.BOLD, 11));
        btnActualizarStock.setBackground(new Color(33, 150, 243));
        btnActualizarStock.setForeground(Color.WHITE);
        btnActualizarStock.addActionListener(e -> mostrarDialogoActualizarStock());

    JButton btnVender = new JButton("💸 Vender");
    btnVender.setFont(new Font("Arial", Font.BOLD, 11));
    btnVender.setBackground(new Color(233, 30, 99));
    btnVender.setForeground(Color.WHITE);
    btnVender.addActionListener(e -> mostrarDialogoVender());
        
        JButton btnProductosBajoStock = new JButton("⚠️ Bajo Stock");
        btnProductosBajoStock.setFont(new Font("Arial", Font.BOLD, 11));
        btnProductosBajoStock.setBackground(new Color(255, 152, 0));
        btnProductosBajoStock.setForeground(Color.WHITE);
        btnProductosBajoStock.addActionListener(e -> cargarProductosBajoStock());
        
        JButton btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 11));
        btnActualizar.setBackground(new Color(156, 39, 176));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> cargarProductos());
        
        panel.add(btnAgregarProducto);
        panel.add(btnEditarProducto);
        // Permisos: Empleado puede ver Bajo Stock pero no Eliminar ni Actualizar Stock
        if (!esEmpleado) {
            panel.add(btnEliminarProducto);
            panel.add(btnActualizarStock);
        }
        panel.add(btnProductosBajoStock);
        panel.add(btnVender);
        panel.add(btnActualizar);
        
        return panel;
    }

    private JPanel crearPestañaCategorias() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(245, 245, 245));
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        JButton btnAgregarCategoria = new JButton("➕ Agregar Categoría");
        btnAgregarCategoria.setFont(new Font("Arial", Font.BOLD, 11));
        btnAgregarCategoria.setBackground(new Color(76, 175, 80));
        btnAgregarCategoria.setForeground(Color.WHITE);
        btnAgregarCategoria.addActionListener(e -> mostrarDialogoCategoria());
        
        JButton btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 11));
        btnActualizar.setBackground(new Color(156, 39, 176));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> cargarCategorias());
        
        panelBotones.add(btnAgregarCategoria);
        panelBotones.add(btnActualizar);
        panel.add(panelBotones, BorderLayout.NORTH);
        
        // Tabla de categorías
        modeloTablaCategorias = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Descripción"}, 0
        ) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaCategorias = new JTable(modeloTablaCategorias);
        tablaCategorias.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaCategorias.setRowHeight(25);
        tablaCategorias.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaCategorias.getTableHeader().setBackground(new Color(70, 130, 180));
        tablaCategorias.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tablaCategorias);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        cargarCategorias();
        
        return panel;
    }

    private JPanel crearPestañaUsuarios() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(245, 245, 245));
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        JButton btnAgregarUsuario = new JButton("➕ Agregar Usuario");
        btnAgregarUsuario.setFont(new Font("Arial", Font.BOLD, 11));
        btnAgregarUsuario.setBackground(new Color(76, 175, 80));
        btnAgregarUsuario.setForeground(Color.WHITE);
        btnAgregarUsuario.addActionListener(e -> mostrarDialogoUsuario());
        
        JButton btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 11));
        btnActualizar.setBackground(new Color(156, 39, 176));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> cargarUsuarios());
        
        panelBotones.add(btnAgregarUsuario);
        panelBotones.add(btnActualizar);
        panel.add(panelBotones, BorderLayout.NORTH);
        
        // Tabla de usuarios
        modeloTablaUsuarios = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Email", "Teléfono", "Rol"}, 0
        ) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaUsuarios = new JTable(modeloTablaUsuarios);
        tablaUsuarios.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaUsuarios.getTableHeader().setBackground(new Color(70, 130, 180));
        tablaUsuarios.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        cargarUsuarios();
        
        return panel;
    }

    private JPanel crearPestañaHistorial() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(245, 245, 245));
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(new Color(240, 240, 240));
        
        JButton btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 11));
        btnActualizar.setBackground(new Color(156, 39, 176));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> cargarHistorial());
        
        panelBotones.add(btnActualizar);
        panel.add(panelBotones, BorderLayout.NORTH);
        
        // Tabla de historial
        modeloTablaHistorial = new DefaultTableModel(
            new Object[]{"ID", "Producto", "Tipo Movimiento", "Cantidad", "Fecha", "Usuario"}, 0
        ) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaHistorial = new JTable(modeloTablaHistorial);
        tablaHistorial.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaHistorial.setRowHeight(25);
        tablaHistorial.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaHistorial.getTableHeader().setBackground(new Color(70, 130, 180));
        tablaHistorial.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        cargarHistorial();
        
        return panel;
    }

    // ==================== Métodos de Carga de Datos ====================

    private void cargarProductos() {
        modeloTablaProductos.setRowCount(0);
        List<Producto> productos = controladorProducto.obtenerProductos();
        for (Producto p : productos) {
            String estado = p.getStockActual() <= p.getStockMinimo() ? "⚠️ BAJO STOCK" : "✓ OK";
            modeloTablaProductos.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getCategoria(),
                String.format("$%.2f", p.getPrecio()),
                p.getStockActual(),
                p.getStockMinimo(),
                estado
            });
        }
    }

    private void cargarProductosBajoStock() {
        modeloTablaProductos.setRowCount(0);
        java.util.List<Producto> productos = controladorProducto.obtenerProductosBajoStock();
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos bajo stock", "Información", JOptionPane.INFORMATION_MESSAGE);
            cargarProductos();
            return;
        }
        // Mostrar diálogo de alerta con estilo y acciones
        AlertaStockDialog alerta = new AlertaStockDialog(this, productos, controladorProducto, controladorInventario, usuarioAutenticado);
        alerta.mostrar();
        // después de cerrar la alerta, recargar la tabla general
        cargarProductos();
    }

    private void cargarCategorias() {
        modeloTablaCategorias.setRowCount(0);
        List<Categoria> categorias = controladorCategoria.obtenerCategorias();
        for (Categoria c : categorias) {
            modeloTablaCategorias.addRow(new Object[]{
                c.getId(),
                c.getNombre(),
                c.getDescripcion()
            });
        }
    }

    private void cargarUsuarios() {
        modeloTablaUsuarios.setRowCount(0);
        List<Usuario> usuarios = controladorUsuario.obtenerUsuarios();
        for (Usuario u : usuarios) {
            modeloTablaUsuarios.addRow(new Object[]{
                u.getId(),
                u.getNombre(),
                u.getEmail(),
                u.getTelefono(),
                u.getRol()
            });
        }
    }

    private void cargarHistorial() {
        modeloTablaHistorial.setRowCount(0);
        List<HistorialInventario> historial = controladorInventario.obtenerHistorial();
        for (HistorialInventario h : historial) {
            modeloTablaHistorial.addRow(new Object[]{
                h.getId(),
                h.getProducto(),
                h.getTipoMovimiento(),
                h.getCantidad(),
                h.getFecha(),
                h.getUsuario()
            });
        }
    }

    // ==================== Diálogos ====================

    private void mostrarDialogoProducto(Producto productoExistente) {
        String titulo = (productoExistente == null) ? "Agregar Producto" : "Editar Producto";
        JDialog dialogo = new JDialog(this, titulo, true);
        dialogo.setSize(500, 420);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblCategoria = new JLabel("Categoría:");
        JTextField txtCategoria = new JTextField();

        JLabel lblPrecio = new JLabel("Precio:");
        JTextField txtPrecio = new JTextField();

        JLabel lblStock = new JLabel("Stock Actual:");
        JTextField txtStock = new JTextField();

        JLabel lblStockMinimo = new JLabel("Stock Mínimo:");
        JTextField txtStockMinimo = new JTextField();

        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextArea txtDescripcion = new JTextArea();
        txtDescripcion.setLineWrap(true);

        // Si estamos editando, pre-llenar campos
        if (productoExistente != null) {
            txtNombre.setText(productoExistente.getNombre());
            txtCategoria.setText(productoExistente.getCategoria());
            txtPrecio.setText(String.valueOf(productoExistente.getPrecio()));
            txtStock.setText(String.valueOf(productoExistente.getStockActual()));
            txtStockMinimo.setText(String.valueOf(productoExistente.getStockMinimo()));
            txtDescripcion.setText(productoExistente.getDescripcion());
        }

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(76, 175, 80));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                String categoria = txtCategoria.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                int stock = Integer.parseInt(txtStock.getText().trim());
                int stockMinimo = Integer.parseInt(txtStockMinimo.getText().trim());
                String descripcion = txtDescripcion.getText().trim();

                if (nombre.isEmpty() || categoria.isEmpty()) {
                    JOptionPane.showMessageDialog(dialogo, "Completa todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (productoExistente == null) {
                    controladorProducto.crearProducto(nombre, categoria, precio, stock, descripcion, stockMinimo);
                    JOptionPane.showMessageDialog(dialogo, "Producto creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // actualizar objeto existente y persistir
                    productoExistente.setNombre(nombre);
                    productoExistente.setCategoria(categoria);
                    productoExistente.setPrecio(precio);
                    productoExistente.setCantidad(stock);
                    productoExistente.setStockActual(stock);
                    productoExistente.setStockMinimo(stockMinimo);
                    productoExistente.setDescripcion(descripcion);
                    boolean ok = controladorProducto.editarProducto(productoExistente);
                    if (ok) {
                        JOptionPane.showMessageDialog(dialogo, "Producto actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(dialogo, "Error al actualizar producto", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                cargarProductos();
                dialogo.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "Verifica los datos numéricos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(244, 67, 54));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> dialogo.dispose());

        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblCategoria);
        panel.add(txtCategoria);
        panel.add(lblPrecio);
        panel.add(txtPrecio);
        panel.add(lblStock);
        panel.add(txtStock);
        panel.add(lblStockMinimo);
        panel.add(txtStockMinimo);
        panel.add(lblDescripcion);
        panel.add(new JScrollPane(txtDescripcion));
        panel.add(btnGuardar);
        panel.add(btnCancelar);

        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    private void mostrarDialogoActualizarStock() {
        JDialog dialogo = new JDialog(this, "Actualizar Stock", true);
        dialogo.setSize(400, 250);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel lblProductoId = new JLabel("ID Producto:");
        JTextField txtProductoId = new JTextField();
        
        JLabel lblCantidad = new JLabel("Cantidad a Agregar:");
        JTextField txtCantidad = new JTextField();
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(76, 175, 80));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> {
            try {
                int idProducto = Integer.parseInt(txtProductoId.getText());
                int cantidad = Integer.parseInt(txtCantidad.getText());
                
                controladorProducto.incrementarStock(idProducto, cantidad);
                JOptionPane.showMessageDialog(dialogo, "Stock actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarProductos();
                dialogo.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "Ingresa números válidos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(244, 67, 54));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        panel.add(lblProductoId);
        panel.add(txtProductoId);
        panel.add(lblCantidad);
        panel.add(txtCantidad);
        panel.add(btnActualizar);
        panel.add(btnCancelar);
        
        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    private void mostrarDialogoVender() {
        JDialog dialogo = new JDialog(this, "Vender Producto", true);
        dialogo.setSize(420, 280);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblProductoId = new JLabel("ID Producto:");
        JTextField txtProductoId = new JTextField();

        JLabel lblCantidad = new JLabel("Cantidad a Vender:");
        JTextField txtCantidad = new JTextField();

        JLabel lblUsuario = new JLabel("Usuario:");
        JTextField txtUsuario = new JTextField();

        JButton btnVender = new JButton("Vender");
        btnVender.setBackground(new Color(233, 30, 99));
        btnVender.setForeground(Color.WHITE);
        btnVender.addActionListener(e -> {
            try {
                int idProducto = Integer.parseInt(txtProductoId.getText());
                int cantidad = Integer.parseInt(txtCantidad.getText());
                String usuario = txtUsuario.getText();

                controladorProducto.venderProducto(idProducto, cantidad, usuario);
                JOptionPane.showMessageDialog(dialogo, "Venta registrada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarProductos();
                cargarHistorial();
                dialogo.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "Ingresa números válidos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(244, 67, 54));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> dialogo.dispose());

        panel.add(lblProductoId);
        panel.add(txtProductoId);
        panel.add(lblCantidad);
        panel.add(txtCantidad);
        panel.add(lblUsuario);
        panel.add(txtUsuario);
        panel.add(btnVender);
        panel.add(btnCancelar);

        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    private void mostrarDialogoCategoria() {
        JDialog dialogo = new JDialog(this, "Agregar Categoría", true);
        dialogo.setSize(400, 250);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();
        
        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextArea txtDescripcion = new JTextArea();
        txtDescripcion.setLineWrap(true);
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(76, 175, 80));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String descripcion = txtDescripcion.getText();
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Ingresa un nombre", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            controladorCategoria.crearCategoria(nombre, descripcion);
            JOptionPane.showMessageDialog(dialogo, "Categoría creada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarCategorias();
            dialogo.dispose();
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(244, 67, 54));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblDescripcion);
        panel.add(new JScrollPane(txtDescripcion));
        panel.add(btnGuardar);
        panel.add(btnCancelar);
        
        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    private void mostrarDialogoUsuario() {
        JDialog dialogo = new JDialog(this, "Agregar Usuario", true);
        dialogo.setSize(450, 350);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();
        
        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();
        
        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField();
        
        JLabel lblRol = new JLabel("Rol:");
        JComboBox<String> comboRol = new JComboBox<>(new String[]{"Admin", "Empleado", "Gerente"});
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(76, 175, 80));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String email = txtEmail.getText();
            String telefono = txtTelefono.getText();
            String rol = (String) comboRol.getSelectedItem();
            
            if (nombre.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Completa los campos requeridos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            controladorUsuario.crearUsuario(nombre, email, telefono, rol);
            JOptionPane.showMessageDialog(dialogo, "Usuario creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarUsuarios();
            dialogo.dispose();
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(244, 67, 54));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> dialogo.dispose());
        
        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblTelefono);
        panel.add(txtTelefono);
        panel.add(lblRol);
        panel.add(comboRol);
        panel.add(btnGuardar);
        panel.add(btnCancelar);
        
        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    private void actualizarTodas() {
        cargarProductos();
        cargarCategorias();
        cargarUsuarios();
        cargarHistorial();
    }
}
