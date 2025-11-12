package vista;

import controlador.ControladorCategoria;
import controlador.ControladorProducto;
import controlador.ControladorUsuario;
import controlador.ControladorInventario;
import modelo.Usuario;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;


public class App {

    public static void main(String[] args) {
        // ==============================================
        // 1. Instanciación de Controladores
        // Es el punto donde el Main 'conecta' la aplicación.
        // ==============================================
        
        System.out.println("--- INICIANDO APLICACIÓN INVENTARIO ---");
        
        ControladorInventario invController = new ControladorInventario();
        ControladorProducto prodController = new ControladorProducto(invController);
        ControladorCategoria catController = new ControladorCategoria();
        ControladorUsuario userController = new ControladorUsuario();

        // ==============================================
        // 2. Lanzar la Interfaz Gráfica
        // ==============================================
        SwingUtilities.invokeLater(() -> {
                // Bucle principal: permite seleccionar rol y volver si es necesario
                boolean volverASeleccionarRol = true;
            
                while (volverASeleccionarRol) {
                    volverASeleccionarRol = false;
                
                    // Primero: Seleccionar rol
                    String rolSeleccionado = RolSelectionDialog.selectRol(null);
                
                    if (rolSeleccionado == null) {
                        System.out.println("Selección de rol cancelada");
                        System.exit(0);
                    }
                
                    System.out.println("Rol seleccionado: " + rolSeleccionado);
                
                    // Segundo: Login con las credenciales (con reintentos)
                    Usuario usuarioAutenticado = null;
                    int intentos = 0;
                    final int MAX_INTENTOS = 3;
                
                    while (usuarioAutenticado == null && intentos < MAX_INTENTOS) {
                        usuarioAutenticado = LoginDialog.promptLogin(null, userController, rolSeleccionado);
                    
                        // Verificar si presionó el botón "Volver"
                        if (usuarioAutenticado != null && usuarioAutenticado.getId() == -999) {
                            System.out.println("Usuario presionó 'Volver': regresando a selección de rol");
                            volverASeleccionarRol = true;
                            usuarioAutenticado = null;
                            break;
                        }
                    
                        if (usuarioAutenticado == null) {
                            intentos++;
                            if (intentos < MAX_INTENTOS) {
                                // Preguntamos si desea reintentar
                                int respuesta = JOptionPane.showConfirmDialog(null, 
                                    "❌ Login cancelado o credenciales inválidas.\n¿Desea intentar de nuevo?\n\n⏳ Intentos restantes: " + (MAX_INTENTOS - intentos),
                                    "Reintentar Login",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                            
                                if (respuesta == JOptionPane.NO_OPTION) {
                                    System.out.println("Sesión cancelada por el usuario");
                                    System.exit(0);
                                }
                            }
                        }
                    }
                
                    // Si regresa a seleccionar rol, continúa el bucle
                    if (volverASeleccionarRol) {
                        continue;
                    }
                
                    // Verificar si se agotaron los intentos
                    if (usuarioAutenticado == null) {
                        JOptionPane.showMessageDialog(null, 
                            "⛔ Se agotaron los intentos de login (" + MAX_INTENTOS + ").\nLa aplicación se cerrará.",
                            "Acceso Denegado",
                            JOptionPane.ERROR_MESSAGE);
                        System.out.println("Acceso denegado: máximo de intentos alcanzado");
                        System.exit(0);
                    }
                
                    // Login exitoso: lanzar la interfaz principal
                    System.out.println("✅ Usuario autenticado: " + usuarioAutenticado.getNombre());
                    new InterfazPrincipal(prodController, catController, userController, invController, usuarioAutenticado);
                }
        });
    }
}
