package controlador;

import modelo.Usuario;
import vista.VistaUsuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControladorUsuario {

    private final List<Usuario> usuarios = new ArrayList<>();
    private final VistaUsuario vistaUsuario = new VistaUsuario();
    private int nextId = 1;

    public ControladorUsuario() {
        usuarios.add(new Usuario(nextId++, "Admin User", "admin@app.com", "pass123", "ADMIN"));
        usuarios.add(new Usuario(nextId++, "Standard User", "user@app.com", "pass456", "USER"));
        // Usuario gerente solicitado por el usuario
        usuarios.add(new Usuario(nextId++, "Jeffer", "Jeffer@gmail.com", "adm123", "GERENTE"));
        // Usuario de prueba solicitado: correo usuario@gmail.com, contraseña pass456, rol Empleado
        usuarios.add(new Usuario(nextId++, "Usuario Empleado", "usuario@gmail.com", "pass456", "Empleado"));
    }

    public void obtenerYMostrarTodosLosUsuarios() {
        vistaUsuario.mostrarInformacionUsuarios(usuarios);
    }

    public List<Usuario> obtenerUsuarios() {
        return new ArrayList<>(usuarios);
    }

    /** Autentica un usuario por correo y contraseña. Devuelve el Usuario si coincide, o null si no. */
    public Usuario autenticar(String correo, String contrasena) {
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correo) && u.getContrasena().equals(contrasena)) {
                return u;
            }
        }
        return null;
    }

    public void crearUsuario(String nombre, String email, String telefono, String rol) {
        Usuario nuevoUsuario = new Usuario(nextId++, nombre, email, "default123", rol);
        nuevoUsuario.setTelefono(telefono);
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
