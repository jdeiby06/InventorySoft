package vista;

import modelo.Usuario;
import java.util.List;

public class VistaUsuario {

    public void mostrarInformacionUsuarios(List<Usuario> usuarios) {
        System.out.println("\n======================================");
        System.out.println("            LISTADO DE USUARIOS         ");
        System.out.println("======================================");
        
        usuarios.forEach(u -> {
            System.out.println("| ID: " + u.getId() + " - " + u.getNombre() + " (Rol: " + u.getRol() + ")");
            System.out.println("| Correo: " + u.getCorreo());
            // ADVERTENCIA: NUNCA mostrar la contraseña en producción
            System.out.println("| Contraseña: " + u.getContrasena() + " (Sólo para demo)");
            System.out.println("--------------------------------------");
        });
    }
}