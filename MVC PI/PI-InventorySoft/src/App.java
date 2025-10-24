

import controlador.controladorCategoria;
import controlador.controladorInventario;
import controlador.controladorProducto;
import controlador.controladorUsuario;

.

public class App {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  INICIANDO APLICACIÓN DE GESTIÓN DE INVENTARIO (MVC)");
        System.out.println("==================================================");

        // 1. Inicialización de Controladores (y sus respectivas Vistas/DAO)
        CategoriaController catController = new CategoriaController();
        ProductoController prodController = new ProductoController();
        UsuarioController userController = new UsuarioController();
        HistorialInventarioController histController = new HistorialInventarioController();

        // ------------------------------------------------------------------
        //                       DEMOSTRACIÓN DE USUARIO
        // ------------------------------------------------------------------
        System.out.println("\n\n--- DEMOSTRACIÓN DE GESTIÓN DE USUARIOS ---");
        
        // 2. Simular una Creación
        userController.crearUsuario("Juan Pérez", "juan.perez@corp.com", "securePWD", "EMPLEADO");
        
        // 3. Simular Listado
        System.out.println("\nLISTADO COMPLETO DE USUARIOS:");
        userController.obtenerYMostrarTodosLosUsuarios();
        
        // 4. Simular Búsqueda
        System.out.println("\nBÚSQUEDA DE USUARIO POR ID 1:");
        userController.buscarYMostrarPorId(1);

        // ------------------------------------------------------------------
        //                       DEMOSTRACIÓN DE PRODUCTO
        // ------------------------------------------------------------------
        System.out.println("\n\n--- DEMOSTRACIÓN DE GESTIÓN DE PRODUCTOS ---");
        
        // 5. Simular Listado de Productos (incluye productos con bajo stock)
        prodController.obtenerYMostrarTodosLosProductos();

        // 6. Simular Operación de Negocio (Bajo Stock)
        System.out.println("\nPRODUCTOS QUE NECESITAN REPOSICIÓN:");
        prodController.obtenerYMostrarProductosBajoStock();
        
        // 7. Simular Actualización de Stock (incremento)
        prodController.incrementarStock(3, 50); // Incrementa el stock del ID 3
        
        // 8. Verificar que ya no está en bajo stock (si era el caso)
        System.out.println("\nVERIFICACIÓN TRAS INCREMENTO DE STOCK:");
        prodController.obtenerYMostrarProductosBajoStock();


        // ------------------------------------------------------------------
        //                   DEMOSTRACIÓN DE CATEGORÍA
        // ------------------------------------------------------------------
        System.out.println("\n\n--- DEMOSTRACIÓN DE GESTIÓN DE CATEGORÍAS ---");
        catController.obtenerYMostrarTodasLasCategorias();
        
        // ------------------------------------------------------------------
        //                   DEMOSTRACIÓN DE HISTORIAL
        // ------------------------------------------------------------------
        System.out.println("\n\n--- DEMOSTRACIÓN DE HISTORIAL DE INVENTARIO ---");
        histController.registrarMovimiento(101, 20241024);
        histController.obtenerYMostrarHistorialPorProducto(101);

        System.out.println("\n==================================================");
        System.out.println("    APLICACIÓN FINALIZADA. (Revisa la consola)");
        System.out.println("==================================================");
    }
}