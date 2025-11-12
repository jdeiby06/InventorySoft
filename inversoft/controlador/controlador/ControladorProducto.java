package controlador;

import modelo.Producto;
import vista.VistaProducto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import controlador.ControladorInventario;

public class ControladorProducto{

    private final List<Producto> productos = new ArrayList<>();
    private final VistaProducto vistaProducto = new VistaProducto();
    private int nextId = 1;
    private ControladorInventario controladorInventario;

    public ControladorProducto() {
        this(new ControladorInventario());
    }

    public ControladorProducto(ControladorInventario controladorInventario) {
        this.controladorInventario = controladorInventario;
        // Datos de ejemplo
        // Producto: id, nombre, categoria, precio, cantidad, descripcion, stock_actual, stock_minimo
        productos.add(new Producto(nextId++, "Resma Carta", "Oficina", 12000.0, 10, "Hojas de papel", 10, 20));
        productos.add(new Producto(nextId++, "Cuadeno", "Escuela", 2500.0, 50, "Cuadeno cocido de 100 hjs.", 50, 20));
        productos.add(new Producto(nextId++, "Lapicero", "Oficina", 1400.0, 5, "Lapicero tinta negra.", 5, 10));
    }

    public void obtenerYMostrarTodosLosProductos() {
        vistaProducto.mostrarInformacionProductos(productos);
    }

    public List<Producto> obtenerProductos() {
        return new ArrayList<>(productos);
    }

    public List<Producto> obtenerProductosBajoStock() {
        return productos.stream()
            .filter(p -> p.getStockActual() <= p.getStockMinimo())
            .collect(Collectors.toList());
    }

    public void obtenerYMostrarProductosBajoStock() {
        List<Producto> bajoStock = obtenerProductosBajoStock();
            
        System.out.println("\n--- LISTADO DE PRODUCTOS BAJO STOCK ---");
        if (bajoStock.isEmpty()) {
            System.out.println(" ¡No hay productos bajo stock! ");
        } else {
            vistaProducto.mostrarInformacionProductos(bajoStock);
        }
    }

    public void incrementarStock(int id, int cantidad) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                p.setStockActual(p.getStockActual() + cantidad);
                System.out.println("✅ Stock de " + p.getNombre() + " incrementado en " + cantidad + ". Nuevo stock: " + p.getStockActual());
                if (controladorInventario != null) {
                    controladorInventario.registrarMovimiento(p.getId(), p.getNombre(), "IngresoStock", cantidad, "sistema");
                }
                return;
            }
        }
        System.out.println("❌ ERROR: Producto con ID " + id + " no encontrado para incrementar stock.");
    }

    public void venderProducto(int id, int cantidad, String usuario) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                if (p.getStockActual() < cantidad) {
                    System.out.println("❌ ERROR: Stock insuficiente para vender. Disponible: " + p.getStockActual());
                    return;
                }
                p.setStockActual(p.getStockActual() - cantidad);
                System.out.println("✅ Venta: " + cantidad + " unidades de " + p.getNombre() + ". Stock restante: " + p.getStockActual());
                if (controladorInventario != null) {
                    controladorInventario.registrarMovimiento(p.getId(), p.getNombre(), "Venta", cantidad, usuario == null ? "anónimo" : usuario);
                }
                return;
            }
        }
        System.out.println("❌ ERROR: Producto con ID " + id + " no encontrado para venta.");
    }

    public ControladorInventario getControladorInventario() {
        return controladorInventario;
    }

    public void crearProducto(String nombre, String categoria, double precio, int stock, String descripcion, int stockMinimo) {
        Producto nuevoProducto = new Producto(nextId++, nombre, categoria, precio, stock, descripcion, stock, stockMinimo);
        productos.add(nuevoProducto);
        System.out.println("✅ Producto '" + nombre + "' creado exitosamente.");
    }
}