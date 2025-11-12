package vista;

import modelo.HistorialInventario;
import java.util.List;

public class VistaHistorialInventario {

    public void mostrarHistorial(List<HistorialInventario> historial) {
        System.out.println("\n======================================");
        System.out.println("          HISTORIAL DE INVENTARIO      ");
        System.out.println("======================================");
        
        historial.forEach(h -> {
            System.out.println("| ID: " + h.getId() + " - Producto ID: " + h.getIdProducto());
            System.out.println("| Fecha: " + h.getFechaInventario());
            System.out.println("--------------------------------------");
        });
    }
}
