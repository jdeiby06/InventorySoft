package vista;

import modelo.HistorialInventario;
import java.util.List;

public class vistaHistorialInventario {

    public void mostrarHistorial(List<HistorialInventario> historial) {
        System.out.println("\n================================================");
        System.out.println("          REGISTROS DE INVENTARIO HISTÓRICO     ");
        System.out.println("================================================");
        
        historial.forEach(h -> {
            System.out.println("| ID Registro: " + h.getId());
            System.out.println("| ID Producto: " + h.getIdProducto());
            System.out.println("| Fecha Inventario (INT): " + h.getFechaInventario());
            System.out.println("------------------------------------------------");
        });
    }
}