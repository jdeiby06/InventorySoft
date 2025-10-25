package controlador;

import modelo.Producto;
import vista.VistaProducto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorProducto{

    private final List<Producto> productos = new ArrayList<>();
    private final VistaProducto vistaProducto = new VistaProducto();
    private int nextId = 1;

    public ControladorProducto() {
        // Datos de ejemplo
        // Producto: id, nombre, categoria, precio, cantidad, descripcion, stock_actual, stock_minimo
        productos.add(new Producto(nextId++, "Resma Carta", "Oficina", 12000, 10, "Hojas de papel", 10, 20));
        productos.add(new Producto(nextId++, "Cuadeno", "Escuela", 2500, 50, "Cuadeno cocido de 100 hjs.", 50, 20));
        productos.add(new Producto(nextId++, "Lapicero", "Oficina", 1400, 5, "Lapicero tinta negra.", 5, 10));
    }

    public void obtenerYMostrarTodosLosProductos() {
        vistaProducto.mostrarInformacionProductos(productos);
    }

    public void obtenerYMostrarProductosBajoStock() {
        List<Producto> bajoStock = productos.stream()
            .filter(p -> p.getStockActual() <= p.getStockMinimo())
            .collect(Collectors.toList());
            
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
                return;
            }
        }
        System.out.println("❌ ERROR: Producto con ID " + id + " no encontrado para incrementar stock.");
    }
}