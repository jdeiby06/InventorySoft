package controlador;

import modelo.Categoria; // Asume que Categoria está en el paquete modelo
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias") // Ruta base para todos los endpoints
public class CategoriaController {

    // 1. SIMULACIÓN DE DATOS (En lugar de la capa de Servicio/Repository)
    private final List<Categoria> categorias = new ArrayList<>();
    private int nextId = 1;

    public CategoriaController() {
        // Inicialización de datos
        categorias.add(new Categoria(nextId++, "Electrodomésticos", "Grandes y pequeños aparatos."));
        categorias.add(new Categoria(nextId++, "Moda", "Ropa, calzado y accesorios."));
    }
    // --------------------------------------------------------------------

    /**
     * Endpoint: GET /api/categorias
     * Obtiene todas las categorías.
     */
    @GetMapping 
    public List<Categoria> obtenerTodas() {
        return categorias;
    }

    /**
     * Endpoint: GET /api/categorias/{id}
     * Obtiene una categoría por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable int id) {
        Optional<Categoria> categoriaEncontrada = categorias.stream()
            .filter(c -> c.getId() == id)
            .findFirst();

        // Retorna 200 OK si encuentra la categoría, o 404 NOT FOUND si no existe
        return categoriaEncontrada
            .map(c -> new ResponseEntity<>(c, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint: POST /api/categorias
     * Crea una nueva categoría.
     */
    @PostMapping 
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria nuevaCategoria) {
        // Asignar el siguiente ID secuencial
        nuevaCategoria.setId(nextId++); 
        categorias.add(nuevaCategoria);
        
        // Retorna la nueva categoría con el código 201 CREATED
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }
    
    /**
     * Endpoint: PUT /api/categorias/{id}
     * Actualiza una categoría existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable int id, @RequestBody Categoria categoriaActualizada) {
        for (int i = 0; i < categorias.size(); i++) {
            Categoria c = categorias.get(i);
            if (c.getId() == id) {
                // Mantener el ID original
                categoriaActualizada.setId(id); 
                // Reemplazar el objeto en la lista
                categorias.set(i, categoriaActualizada); 
                // Retorna 200 OK
                return new ResponseEntity<>(categoriaActualizada, HttpStatus.OK); 
            }
        }
        // Retorna 404 NOT FOUND si no se encuentra
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }

    /**
     * Endpoint: DELETE /api/categorias/{id}
     * Elimina una categoría.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable int id) {
        boolean fueEliminada = categorias.removeIf(c -> c.getId() == id);
        
        if (fueEliminada) {
            // Retorna 204 NO CONTENT si se eliminó exitosamente
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        } else {
            // Retorna 404 NOT FOUND si no se encontró la categoría
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
    }
}