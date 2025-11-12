package controlador; 

import modelo.Categoria; 
import vista.VistaCategoria; 
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControladorCategoria {

    private final List<Categoria> categorias = new ArrayList<>();
    private final VistaCategoria vistaCategoria = new VistaCategoria();
    private int nextId = 1;

    public ControladorCategoria() {
        // Inicialización de datos usando el constructor con argumentos
        categorias.add(new Categoria(nextId++, "Oficina", "Material para oficinas."));
        categorias.add(new Categoria(nextId++, "Libros", "Material de lectura."));
    }

    public void obtenerYMostrarTodasLasCategorias() {
        // CORREGIDO: Se usa la instancia (vistaCategoria) y la variable correcta (categorias)
        vistaCategoria.mostrarInformacionCategorias(categorias); 
    }
    public List<Categoria> obtenerCategorias() {
        return new ArrayList<>(categorias);
    }
    public void crearCategoria(String nombre, String descripcion) {
        Categoria nueva = new Categoria(nextId++, nombre, descripcion);
        categorias.add(nueva);
        System.out.println("✅ Categoría '" + nombre + "' creada exitosamente.");
    }

    public void buscarYMostrarPorId(int id) {
        Optional<Categoria> categoriaEncontrada = categorias.stream()
            .filter(c -> c.getId() == id)
            .findFirst();

        if (categoriaEncontrada.isPresent()) {
            vistaCategoria.mostrarInformacionCategorias(List.of(categoriaEncontrada.get()));
        } else {
            System.out.println("❌ ERROR: Categoría con ID " + id + " no encontrada.");
        }
    }
}
    
    
