package persistencia;

/**
 * Clase pequeña para probar la conexión a la base de datos desde Maven/IDE.
 */
public class TestConexionDB {
    public static void main(String[] args) {
        System.out.println("Probando conexión a la base de datos...");
        ConexionDB.testConnection();
    }
}
