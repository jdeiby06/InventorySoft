package vista;

import modelo.Producto;
import java.util.List;

public class VistaProducto_old {

    public void mostrarInformacionProductos(List<Producto> productos) {
        System.out.println("\n======================================");
        System.out.println("             INVENTARIO DE PRODUCTOS    ");
        System.out.println("======================================");
        
        productos.forEach(p -> {
            String estadoStock = p.getStockActual() <= p.getStockMinimo() ? "!!! BAJO STOCK !!!" : "OK";
            
            System.out.println("| ID: " + p.getId() + " - " + p.getNombre() + " (" + estadoStock + ")");
            System.out.println("| Categoría: " + p.getCategoria());
            System.out.println("| Precio: $" + p.getPrecio());
            System.out.println("| Stock Actual: " + p.getStockActual() + " (Min: " + p.getStockMinimo() + ")");
            System.out.println("| Descripción: " + p.getDescripcion());
            System.out.println("--------------------------------------");
        });
    }
}
