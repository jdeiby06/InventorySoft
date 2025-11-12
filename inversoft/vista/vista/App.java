package vista; 

import controlador.ControladorCategoria;
import controlador.ControladorProducto;
import controlador.ControladorUsuario;
import controlador.ControladorInventario;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import vista.LoginDialog;
import modelo.Usuario;


@SuppressWarnings("unused")
public class App {

    public static void main(String[] args) {
        // ==============================================
        // 1. Instanciación de Controladores
        // Es el punto donde el Main 'conecta' la aplicación.
        // ==============================================
        
        System.out.println("--- INICIANDO APLICACIÓN INVENTARIO MVC ---");
        
        ControladorInventario invController = new ControladorInventario();
        ControladorProducto prodController = new ControladorProducto(invController);
        ControladorCategoria catController = new ControladorCategoria();
        ControladorUsuario userController = new ControladorUsuario();

        // 1.b) Pedir credenciales antes de mostrar la interfaz
        Usuario logged = null;
        while (true) {
            logged = LoginDialog.promptLogin(null, userController);
            if (logged != null) break;
            // Si returned null (cancel o credenciales inválidas), preguntar si desea reintentar
            int retry = JOptionPane.showConfirmDialog(null, "Credenciales inválidas o canceladas. ¿Deseas intentarlo de nuevo?", "Login", JOptionPane.YES_NO_OPTION);
            if (retry != JOptionPane.YES_OPTION) {
                System.out.println("No se inició sesión. Saliendo...");
                System.exit(0);
            }
        }

        // 2) Lanzar la Interfaz Gráfica pasando el usuario autenticado
        final Usuario usuarioAutenticado = logged;
        SwingUtilities.invokeLater(() -> {
            new InterfazPrincipal(prodController, catController, userController, invController, usuarioAutenticado);
        });
    }
}