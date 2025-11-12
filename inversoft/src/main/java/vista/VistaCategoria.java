package vista;

import modelo.Categoria;
import java.util.List;

public class VistaCategoria {

    public void mostrarInformacionCategorias(List<Categoria> categorias) {
        System.out.println("\n======================================");
        System.out.println("              CATEGORÍAS               ");
        System.out.println("======================================");
        
        categorias.forEach(c -> {
            System.out.println("| ID: " + c.getId() + " - " + c.getNombre());
            System.out.println("| Descripción: " + c.getDescripcion());
            System.out.println("--------------------------------------");
        });
    }
}
