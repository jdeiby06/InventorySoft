package vista; 

import controlador.ControladorCategoria;
import controlador.ControladorProducto;
import controlador.ControladorUsuario;
import controlador.ControladorInventario;


public class App {

    public static void main(String[] args) {
        // ==============================================
        // 1. Instanciación de Controladores
        // Es el punto donde el Main 'conecta' la aplicación.
        // ==============================================
        
        System.out.println("--- INICIANDO APLICACIÓN INVENTARIO MVC ---");
        
        ControladorProducto prodController = new ControladorProducto();
        ControladorCategoria catController = new ControladorCategoria();
        ControladorUsuario userController = new ControladorUsuario();
        ControladorInventario invController = new ControladorInventario();

        // ==============================================
        // 2. Ejecución de Lógica y Presentación (Demo)
        // ==============================================

        // --- Demo Productos ---
        System.out.println("\n\n################################################");
        System.out.println("### DEMO: GESTIÓN DE PRODUCTOS ###");
        
        // 1. Mostrar todos los productos iniciales
        prodController.obtenerYMostrarTodosLosProductos();

        // 2. Simular incremento de stock (ej. Producto ID 1)
        System.out.println("\n>>> SIMULANDO ENTRADA DE STOCK para Producto ID 1...");
        prodController.incrementarStock(1, 50);

        // 3. Mostrar productos con stock actualizado
        prodController.obtenerYMostrarTodosLosProductos();
        
        // --- Demo Categorías ---
        System.out.println("\n\n################################################");
        System.out.println("### DEMO: GESTIÓN DE CATEGORÍAS ###");
        
        // 1. Mostrar todas las categorías
        catController.obtenerYMostrarTodasLasCategorias();
        
        // 2. Crear una nueva categoría
        catController.crearCategoria("Abarrotes", "Comida seca y enlatada.");
        
        // 3. Mostrar categorías después de la creación
        catController.obtenerYMostrarTodasLasCategorias();
        
        // --- Demo Inventario ---
        System.out.println("\n\n################################################");
        System.out.println("### DEMO: REGISTRO DE INVENTARIO HISTÓRICO ###");
        
        // 1. Mostrar historial completo
        invController.obtenerYMostrarTodoElHistorial();
        
        // --- Demo Usuarios ---
        System.out.println("\n\n################################################");
        System.out.println("### DEMO: GESTIÓN DE USUARIOS ###");

        // 1. Mostrar todos los usuarios
        userController.obtenerYMostrarTodosLosUsuarios();
        
    }
}