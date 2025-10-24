package controlador;

import com.miaplicacion.modelo.HistorialInventario; // Asegúrate de la ruta de tu modelo
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventario/historial") // URL base específica para el historial
public class HistorialInventarioController {

    // SIMULACIÓN DE DATOS EN MEMORIA
    private final List<HistorialInventario> historial = new ArrayList<>();
    private int nextId = 1;

    public HistorialInventarioController() {
        // Datos de ejemplo: id, idProducto, fecha (ej: 20240325)
        historial.add(new HistorialInventario(nextId++, 101, 20240320));
        historial.add(new HistorialInventario(nextId++, 102, 20240320));
        historial.add(new HistorialInventario(nextId++, 101, 20240325)); // Otro registro para el producto 101
    }
    // FIN SIMULACIÓN DE DATOS

    /**
     * Endpoint: GET /api/inventario/historial
     * Obtiene todos los registros del historial.
     */
    @GetMapping 
    public List<HistorialInventario> obtenerTodoElHistorial() {
        return historial;
    }

    /**
     * Endpoint: GET /api/inventario/historial/{id}
     * Obtiene un registro por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistorialInventario> obtenerPorId(@PathVariable int id) {
        Optional<HistorialInventario> registro = historial.stream()
            .filter(h -> h.getId() == id)
            .findFirst();

        return registro
            .map(r -> new ResponseEntity<>(r, HttpStatus.OK)) // 200 OK
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found
    }

    /**
     * Endpoint: POST /api/inventario/historial
     * Crea un nuevo registro de inventario.
     */
    @PostMapping 
    public ResponseEntity<HistorialInventario> crearRegistro(@RequestBody HistorialInventario nuevoRegistro) {
        // Asignar el siguiente ID y guardar
        nuevoRegistro.setId(nextId++); 
        historial.add(nuevoRegistro);
        
        // Retorna el objeto creado con el código 201 CREATED
        return new ResponseEntity<>(nuevoRegistro, HttpStatus.CREATED);
    }
    
    /**
     * Endpoint: GET /api/inventario/historial/producto/{idProducto}
     * Extensión útil: Obtener historial por ID de Producto.
     */
    @GetMapping("/producto/{idProducto}")
    public List<HistorialInventario> obtenerHistorialPorProducto(@PathVariable int idProducto) {
        // Filtra y devuelve solo los registros del producto específico
        return historial.stream()
            .filter(h -> h.getIdProducto() == idProducto)
            .toList();
    }
    
    // Puedes añadir métodos PUT para actualizar o DELETE para eliminar, siguiendo la lógica del ejemplo anterior.
}