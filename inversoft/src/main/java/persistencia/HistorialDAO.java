package persistencia;

import modelo.HistorialInventario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistorialDAO {

    public HistorialDAO() {
        // Crear tabla si no existe
        // Primero intentamos limpiar si la estructura es antigua
        try (Connection c = ConexionDB.getConnection(); Statement s = c.createStatement()) {
            // Intentar drops la tabla si existe mal (sin columna id)
            try {
                s.execute("DROP TABLE IF EXISTS historial_inventario");
                System.out.println("Tabla historial_inventario eliminada para reconstruir.");
            } catch (SQLException e) {
                // Ignorar si no se puede dropar
            }
            
            // Crear tabla correctamente
            String sql = "CREATE TABLE IF NOT EXISTS historial_inventario ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "id_producto INT NOT NULL,"
                    + "producto VARCHAR(255),"
                    + "tipo_movimiento VARCHAR(100),"
                    + "cantidad INT,"
                    + "fecha VARCHAR(64),"
                    + "usuario VARCHAR(255)"
                    + ")";
            s.execute(sql);
            System.out.println("Tabla historial_inventario creada/verificada correctamente.");
        } catch (SQLException e) {
            System.err.println("No se pudo crear la tabla historial_inventario: " + e.getMessage());
        }
    }

    public HistorialInventario insertMovimiento(HistorialInventario h) throws SQLException {
        String sql = "INSERT INTO historial_inventario (id_producto, producto, tipo_movimiento, cantidad, fecha, usuario) VALUES (?,?,?,?,?,?)";
        try (Connection c = ConexionDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, h.getIdProducto());
            ps.setString(2, h.getProducto());
            ps.setString(3, h.getTipoMovimiento());
            ps.setInt(4, h.getCantidad());
            ps.setString(5, h.getFecha());
            ps.setString(6, h.getUsuario());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    h.setId(id);
                }
            }
            return h;
        }
    }

    public List<HistorialInventario> obtenerTodos() {
        List<HistorialInventario> lista = new ArrayList<>();
        String sql = "SELECT id, id_producto, producto, tipo_movimiento, cantidad, fecha, usuario FROM historial_inventario ORDER BY id ASC";
        try (Connection c = ConexionDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HistorialInventario h = new HistorialInventario(
                        rs.getInt("id"),
                        rs.getInt("id_producto"),
                        rs.getString("producto"),
                        rs.getString("tipo_movimiento"),
                        rs.getInt("cantidad"),
                        rs.getString("fecha"),
                        rs.getString("usuario")
                );
                lista.add(h);
            }
        } catch (SQLException e) {
            System.err.println("Error al leer historial desde DB: " + e.getMessage());
        }
        return lista;
    }
}
