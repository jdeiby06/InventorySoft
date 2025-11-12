package controlador;

import modelo.HistorialInventario;
import vista.VistaHistorialInventario;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorInventario {

    private final List<HistorialInventario> historial = new ArrayList<>();
    private final VistaHistorialInventario vistaHistorial = new VistaHistorialInventario();
    private int nextId = 1;

    public ControladorInventario() {
        // Datos de ejemplo: id, idProducto, fecha (ej: 20240325)
        historial.add(new HistorialInventario(nextId++, 101, 20240320));
        historial.add(new HistorialInventario(nextId++, 102, 20240320));
        historial.add(new HistorialInventario(nextId++, 101, 20240325));
    }

    public void obtenerYMostrarTodoElHistorial() {
        vistaHistorial.mostrarHistorial(historial);
    }

    public List<HistorialInventario> obtenerHistorial() {
        return new ArrayList<>(historial);
    }

    public void registrarMovimiento(int idProducto, int fechaInventario) {
        HistorialInventario nuevoRegistro = new HistorialInventario(nextId++, idProducto, fechaInventario);
        historial.add(nuevoRegistro);
        System.out.println("✅ Movimiento de inventario para Producto ID " + idProducto + " registrado.");
    }

    /** Registrar movimiento con detalles (producto nombre, tipo, cantidad, usuario) */
    public void registrarMovimiento(int idProducto, String productoNombre, String tipoMovimiento, int cantidad, String usuario) {
        String fecha = java.time.LocalDateTime.now().toString();
        HistorialInventario nuevoRegistro = new HistorialInventario(nextId++, idProducto, productoNombre, tipoMovimiento, cantidad, fecha, usuario);
        historial.add(nuevoRegistro);
        System.out.println("✅ Movimiento registrado: " + tipoMovimiento + " / Producto: " + productoNombre + " / Cant: " + cantidad + " / Usuario: " + usuario);
    }
    
    public void obtenerYMostrarHistorialPorProducto(int idProducto) {
        List<HistorialInventario> registrosProducto = historial.stream()
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