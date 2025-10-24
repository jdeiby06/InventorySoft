package com.miaplicacion.controladores;

import com.miaplicacion.modelo.Categoria;
import com.miaplicacion.vistas.VistaCategoria;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class controladorCategoria {

    private final List<Categoria> categorias = new ArrayList<>();
    private final VistaCategoria vistaCategoria = new vistaCategoria();
    private int nextId = 1;

    public controladorCategoria() {
        // Inicialización de datos usando el constructor con argumentos
        categorias.add(new Categoria(nextId++, "Electrónica", "Dispositivos digitales."));
        categorias.add(new Categoria(nextId++, "Libros", "Material de lectura."));
    }

    public void obtenerYMostrarTodasLasCategorias() {
        // Controlador obtiene datos y llama a la Vista para presentar
        vistaCategoria.mostrarInformacionCategorias(categorias);
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