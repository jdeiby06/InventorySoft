package persistencia;

import modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public ProductoDAO() {
        // crear tabla si no existe
        String sql = "CREATE TABLE IF NOT EXISTS productos ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nombre VARCHAR(255),"
                + "categoria VARCHAR(255),"
                + "precio DOUBLE,"
                + "cantidad INT,"
                + "descripcion TEXT,"
                + "stock_actual INT,"
                + "stock_minimo INT"
                + ")";
        try (Connection c = ConexionDB.getConnection(); Statement s = c.createStatement()) {
            s.execute(sql);
        } catch (SQLException e) {
            System.err.println("No se pudo crear/verificar tabla productos: " + e.getMessage());
        }
    }

    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, categoria, precio, cantidad, descripcion, stock_actual, stock_minimo FROM productos ORDER BY id ASC";
        try (Connection c = ConexionDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        rs.getString("descripcion"),
                        rs.getInt("stock_actual"),
                        rs.getInt("stock_minimo")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error leyendo productos desde DB: " + e.getMessage());
        }
        return lista;
    }

    public Producto insertar(Producto p) {
        String sql = "INSERT INTO productos (nombre, categoria, precio, cantidad, descripcion, stock_actual, stock_minimo) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = ConexionDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCategoria());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getCantidad());
            ps.setString(5, p.getDescripcion());
            ps.setInt(6, p.getStockActual());
            ps.setInt(7, p.getStockMinimo());
            ps.executeUpdate();
            // Obtener el último ID insertado (SQLite compatible)
            try (Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SELECT last_insert_rowid() as id")) {
                if (rs.next()) {
                    p.setId(rs.getInt("id"));
                }
            }
            return p;
        } catch (SQLException e) {
            System.err.println("Error insertando producto en DB: " + e.getMessage());
            return p;
        }
    }

    public boolean actualizarStock(int id, int nuevoStock, int nuevoCantidad) {
        String sql = "UPDATE productos SET stock_actual = ?, cantidad = ? WHERE id = ?";
        try (Connection c = ConexionDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, nuevoStock);
            ps.setInt(2, nuevoCantidad);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando stock en DB: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarProducto(Producto p) {
        String sql = "UPDATE productos SET nombre=?, categoria=?, precio=?, cantidad=?, descripcion=?, stock_actual=?, stock_minimo=? WHERE id=?";
        try (Connection c = ConexionDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCategoria());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getCantidad());
            ps.setString(5, p.getDescripcion());
            ps.setInt(6, p.getStockActual());
            ps.setInt(7, p.getStockMinimo());
            ps.setInt(8, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando producto en DB: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection c = ConexionDB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando producto en DB: " + e.getMessage());
            return false;
        }
    }
}
