package controlador;

import modelo.Producto;
import vista.vistaProducto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class controladorProducto{

    private final List<Producto> productos = new ArrayList<>();
    private final VistaProducto vistaProducto = new VistaProducto();
    private int nextId = 1;

    public controladorProducto() {
        // Datos de ejemplo
        // Producto: id, nombre, categoria, precio, cantidad, descripcion, stock_actual, stock_minimo
        productos.add(new Producto(nextId++, "Laptop Gamer X", "Electrónica", 1500.00, 10, "Potente laptop.", 10, 20));
        productos.add(new Producto(nextId++, "Camiseta Algodón", "Ropa", 25.00, 50, "Camiseta básica.", 50, 20));
        productos.add(new Producto(nextId++, "Mouse Óptico", "Electrónica", 12.00, 5, "Mouse ergonómico.", 5, 10));
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
            System.out.println("🎉 ¡No hay productos bajo stock! 🎉");
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