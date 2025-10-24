package controlador;

import modelo.Usuario;
import vista.vistaUsuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class controladorUsuario {

    private final List<Usuario> usuarios = new ArrayList<>();
    private final VistaUsuario vistaUsuario = new VistaUsuario();
    private int nextId = 1;

    public controladorUsuario() {
        // Datos de ejemplo: id, nombre, correo, contrasena, rol
        usuarios.add(new Usuario(nextId++, "Admin User", "admin@app.com", "pass123", "ADMIN"));
        usuarios.add(new Usuario(nextId++, "Standard User", "user@app.com", "pass456", "USER"));
    }

    public void obtenerYMostrarTodosLosUsuarios() {
        vistaUsuario.mostrarInformacionUsuarios(usuarios);
    }

    public void crearUsuario(String nombre, String correo, String contrasena, String rol) {
        Usuario nuevoUsuario = new Usuario(nextId++, nombre, correo, contrasena, rol);
        usuarios.add(nuevoUsuario);
        System.out.println("✅ Usuario '" + nombre + "' creado con rol '" + rol + "'.");
    }

    public void buscarYMostrarPorId(int id) {
        Optional<Usuario> usuarioEncontrado = usuarios.stream()
            .filter(u -> u.getId() == id)
            .findFirst();

        if (usuarioEncontrado.isPresent()) {
            vistaUsuario.mostrarInformacionUsuarios(List.of(usuarioEncontrado.get()));
        } else {
            System.out.println("❌ ERROR: Usuario con ID " + id + " no encontrado.");
        }
    }
}