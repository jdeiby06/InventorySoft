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
        // Datos de ejemplo: id, nombre, correo, contrasena, rol
        usuarios.add(new Usuario(nextId++, "Admin", "admin@app.com", "pass123", "ADMIN"));
        usuarios.add(new Usuario(nextId++, "Andrea", "vendedor@app.com", "pass123", "VENDEDOR"));
        // Usuario gerente solicitado por el usuario
        usuarios.add(new Usuario(nextId++, "Jeffer", "Jeffer@gmail.com", "adm123", "GERENTE"));
         usuarios.add(new Usuario(nextId++, "Daniela", "Vendedor@app.com", "adm123", "EMPLEADO"));
    }

    public void obtenerYMostrarTodosLosUsuarios() {
        vistaUsuario.mostrarInformacionUsuarios(usuarios);
    }

    public List<Usuario> obtenerUsuarios() {
        return new ArrayList<>(usuarios);
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

    public Usuario autenticar(String email, String pass) {
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(email) && u.getContrasena().equals(pass)) {
                return u;
            }
        }
        return null;
    }
}