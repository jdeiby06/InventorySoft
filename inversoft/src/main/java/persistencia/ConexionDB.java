package persistencia;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase utilitaria para manejar la conexión JDBC usando un archivo
 * de propiedades ubicado en `src/main/resources/config.properties`.
 *
 * Ejemplo de config.properties:
 * url=jdbc:mysql://localhost:3306/inventorysoft
 * user=root
 * password=1234
 * driver=com.mysql.cj.jdbc.Driver
 */
public class ConexionDB {

    private static final Properties PROPS = new Properties();

    static {
        // Cargamos `config.properties` desde resources
        try (InputStream in = ConexionDB.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                PROPS.load(in);
            } else {
                System.err.println("⚠️ No se encontró el archivo config.properties en el classpath.");
            }
        } catch (IOException e) {
            System.err.println("⚠️ Error leyendo config.properties: " + e.getMessage());
        }
    }

    /**
     * Obtiene una conexión JDBC usando las propiedades cargadas.
     * @return Connection abierta (el caller debe cerrarla)
     * @throws SQLException si falla la conexión
     */
    public static Connection getConnection() throws SQLException {
        String driver = PROPS.getProperty("driver", "").trim();
        String url = PROPS.getProperty("url", "").trim();
        String user = PROPS.getProperty("user", "").trim();
        String password = PROPS.getProperty("password", "").trim();

        if (url.isEmpty()) {
            throw new SQLException("❌ Propiedad 'url' vacía o no definida en config.properties");
        }

        if (!driver.isEmpty()) {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                System.err.println("⚠️ Driver JDBC no encontrado: " + driver);
            }
        }

        if (user.isEmpty()) {
            // Conexión sin usuario (SQLite, por ejemplo)
            return DriverManager.getConnection(url);
        }

        return DriverManager.getConnection(url, user, password);
    }

    
    public static void testConnection() {
        try (Connection c = getConnection()) {
            System.out.println("✅ Conexión exitosa: " + (c != null && !c.isClosed()));
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
