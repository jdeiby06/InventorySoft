package controlador;

import modelo.HistorialInventario;
import modelo.Producto;
import java.util.List;

public class TestHistorial {
    public static void main(String[] args) {
        ControladorProducto cp = new ControladorProducto();
        // Mostrar productos
        System.out.println("Productos iniciales:");
        for (Producto p : cp.obtenerProductos()) {
            System.out.println(p.getId() + ": " + p.getNombre() + " stock=" + p.getStockActual());
        }

        // Vender 2 unidades del primer producto
        int idVenta = cp.obtenerProductos().get(0).getId();
        cp.venderProducto(idVenta, 2, "pruebaUsuario");

        // Incrementar stock del segundo producto
        int idInc = cp.obtenerProductos().get(1).getId();
        cp.incrementarStock(idInc, 5);

        // Mostrar historial registrado dentro del controlador de producto
        ControladorInventario ci = cp.getControladorInventario();
        List<HistorialInventario> historial = ci.obtenerHistorial();
        System.out.println("\nHistorial registrado:");
        for (HistorialInventario h : historial) {
            System.out.println("ID:" + h.getId() + " ProdID:" + h.getIdProducto() + " Tipo:" + h.getTipoMovimiento() + " Prod:" + h.getProducto() + " Cant:" + h.getCantidad() + " Fecha:" + h.getFecha() + " Usuario:" + h.getUsuario());
        }
    }
}
