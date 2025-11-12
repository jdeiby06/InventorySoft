package controlador;

import modelo.HistorialInventario;
import vista.VistaHistorialInventario;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import persistencia.HistorialDAO;

public class ControladorInventario {

    private final VistaHistorialInventario vistaHistorial = new VistaHistorialInventario();
    private final HistorialDAO dao;

    public ControladorInventario() {
        this.dao = new HistorialDAO();
        // no mantenemos lista en memoria; la DB es la fuente de verdad
    }

    public void obtenerYMostrarTodoElHistorial() {
        List<HistorialInventario> historial = dao.obtenerTodos();
        vistaHistorial.mostrarHistorial(historial);
    }

    public List<HistorialInventario> obtenerHistorial() {
        return dao.obtenerTodos();
    }

    /** Registrar un movimiento con detalles: tipo (Venta, Ajuste, Compra), cantidad, usuario y fecha actual. */
    public void registrarMovimiento(int idProducto, String productoNombre, String tipoMovimiento, int cantidad, String usuario) {
        String fecha = java.time.LocalDateTime.now().toString();
        HistorialInventario nuevoRegistro = new HistorialInventario(0, idProducto, productoNombre, tipoMovimiento, cantidad, fecha, usuario);
        try {
            dao.insertMovimiento(nuevoRegistro);
            System.out.println("✅ Movimiento registrado en DB: " + tipoMovimiento + " / Producto: " + productoNombre + " / Cant: " + cantidad + " / Usuario: " + usuario);
        } catch (Exception e) {
            System.err.println("Error registrando movimiento en DB: " + e.getMessage());
        }
    }

    // Mantener compatibilidad: registrarMovimiento antiguo que sólo guarda un int fechaInventario
    public void registrarMovimiento(int idProducto, int fechaInventario) {
        // crear un registro con los pocos datos
        HistorialInventario h = new HistorialInventario(0, idProducto, fechaInventario);
        try {
            dao.insertMovimiento(h);
            System.out.println("✅ Movimiento de inventario para Producto ID " + idProducto + " registrado en DB.");
        } catch (Exception e) {
            System.err.println("Error registrando movimiento antiguo en DB: " + e.getMessage());
        }
    }
    
    public void obtenerYMostrarHistorialPorProducto(int idProducto) {
        List<HistorialInventario> registrosProducto = dao.obtenerTodos().stream()
            .filter(h -> h.getIdProducto() == idProducto)
            .collect(Collectors.toList());
        
        System.out.println("\n--- Historial para Producto ID: " + idProducto + " ---");
        if (registrosProducto.isEmpty()) {
            System.out.println("No se encontraron registros.");
        } else {
            vistaHistorial.mostrarHistorial(registrosProducto);
        }
    }
}
