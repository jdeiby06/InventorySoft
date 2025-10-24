package vista;

import modelo.Categoria;
import java.util.List;

public class vistaCategoria {

    public void mostrarInformacionCategorias(List<Categoria> categorias) {
        System.out.println("\n======================================");
        System.out.println("          LISTADO DE CATEGORÍAS         ");
        System.out.println("======================================");
        
        categorias.forEach(c -> {
            System.out.println("| ID: " + c.getId());
            System.out.println("| Nombre: " + c.getNombre());
            System.out.println("| Descripción: " + c.getDescripcion());
            System.out.println("--------------------------------------");
        });
    }
}