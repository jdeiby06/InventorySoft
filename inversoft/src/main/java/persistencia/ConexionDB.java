package persistencia;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase utilitaria para manejar la conexión JDBC usando un archivo
 * de propiedades `src/main/resources/db.properties`.
 *
 * Rellena `db.properties` con url, user y password. Si tu driver no se
 * auto-registra, añade la propiedad `driver=com.xyz.Driver` y el jar en
 * el classpath o la dependencia en pom.xml.
 */
public class ConexionDB {

    private static final Properties PROPS = new Properties();

    static {
        // Cargamos `config.properties` desde resources (antes db.properties)
        try (InputStream in = ConexionDB.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                PROPS.load(in);
            } else {
                System.err.println("Advertencia: no se encontró config.properties en classpath");
            }
        } catch (IOException e) {
            System.err.println("Error leyendo db.properties: " + e.getMessage());
        }
    }

    /**
     * Obtiene una conexión JDBC usando las propiedades cargadas.
     * @return Connection abierta (caller debe cerrarla)
     * @throws SQLException si falla la conexión
     */
    public static Connection getConnection() throws SQLException {
        String driver = PROPS.getProperty("driver", "").trim();
        String url = PROPS.getProperty("url", "").trim();
        String user = PROPS.getProperty("user", "").trim();
        String password = PROPS.getProperty("password", "").trim();

        if (driver.length() > 0) {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                System.err.println("Driver JDBC no encontrado: " + driver + " — asegúrate de tener la dependencia/jar en classpath");
                // seguimos y dejamos que DriverManager lance si no hay driver
            }
        }

        if (url.isEmpty()) {
            throw new SQLException("Propiedad 'url' vacía en db.properties");
        }

        if (user.isEmpty()) {
            // Si no hay usuario, asumimos conexión sin usuario (p.ej. SQLite)
            return DriverManager.getConnection(url);
        }

        return DriverManager.getConnection(url, user, password);
    }

    /** Método de prueba rápido que intenta abrir y cerrar una conexión. */
    public static void testConnection() {
        try (Connection c = getConnection()) {
            System.out.println("Conexión exitosa: " + (c != null && !c.isClosed()));
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
