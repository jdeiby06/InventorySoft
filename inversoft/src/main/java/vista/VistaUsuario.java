package vista;

import modelo.Usuario;
import java.util.List;

public class VistaUsuario {

    public void mostrarInformacionUsuarios(List<Usuario> usuarios) {
        System.out.println("\n======================================");
        System.out.println("              USUARIOS                 ");
        System.out.println("======================================");
        
        usuarios.forEach(u -> {
            System.out.println("| ID: " + u.getId() + " - " + u.getNombre() + " (" + u.getRol() + ")");
            System.out.println("| Email: " + u.getCorreo());
            System.out.println("--------------------------------------");
        });
    }
}
