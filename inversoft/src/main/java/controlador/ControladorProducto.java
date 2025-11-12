package controlador;

import modelo.Producto;
import persistencia.ProductoDAO;
import vista.VistaProducto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import controlador.ControladorInventario;

public class ControladorProducto {

    private final List<Producto> productos = new ArrayList<>();
    private final VistaProducto vistaProducto = new VistaProducto();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private int nextId = 1;
    // referencia al controlador de inventario para registrar movimientos
    private ControladorInventario controladorInventario;

    public ControladorInventario getControladorInventario() {
        return controladorInventario;
    }

    /** Constructor por defecto (mantiene compatibilidad): crea su propio ControladorInventario interno */
    public ControladorProducto() {
        this(new ControladorInventario());
    }

    /**
     * Constructor que recibe el controlador de inventario compartido.
     * Usarlo para asegurar que los movimientos se registran en el historial común.
     */
    public ControladorProducto(ControladorInventario controladorInventario) {
        this.controladorInventario = controladorInventario;
        // Cargar desde DB; si no hay registros, crear algunos por defecto
        List<Producto> cargados = productoDAO.obtenerTodos();
        if (cargados.isEmpty()) {
            productos.add(new Producto(nextId++, "Resma Carta", "Oficina", 12000.0, 10, "Hojas de papel", 10, 20));
            productos.add(new Producto(nextId++, "Cuaderno", "Escuela", 2500.0, 50, "Cuaderno cosido de 100 hjs.", 50, 20));
            productos.add(new Producto(nextId++, "Lapicero", "Oficina", 1400.0, 5, "Lapicero tinta negra.", 5, 10));
            // persistir los por defecto
            for (Producto p : productos) {
                productoDAO.insertar(p);
            }
        } else {
            productos.addAll(cargados);
            // fijar nextId al max id + 1
            int maxId = cargados.stream().mapToInt(Producto::getId).max().orElse(0);
            nextId = maxId + 1;
        }
    }

    public void obtenerYMostrarTodosLosProductos() {
        vistaProducto.mostrarInformacionProductos(productos);
    }

    public List<Producto> obtenerProductos() {
        return new ArrayList<>(productos);
    }

    public List<Producto> obtenerProductosBajoStock() {
        return productos.stream()
            .filter(p -> p.getStockActual() <= p.getStockMinimo())
            .collect(Collectors.toList());
    }

    public void obtenerYMostrarProductosBajoStock() {
        List<Producto> bajoStock = obtenerProductosBajoStock();
        System.out.println("\n--- LISTADO DE PRODUCTOS BAJO STOCK ---");
        if (bajoStock.isEmpty()) {
            System.out.println(" ¡No hay productos bajo stock! ");
        } else {
            vistaProducto.mostrarInformacionProductos(bajoStock);
        }
    }

    public void incrementarStock(int id, int cantidad) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                p.setStockActual(p.getStockActual() + cantidad);
                System.out.println("✅ Stock de " + p.getNombre() + " incrementado en " + cantidad + ". Nuevo stock: " + p.getStockActual());
                // actualizar en DB
                productoDAO.actualizarStock(p.getId(), p.getStockActual(), p.getCantidad());
                // registrar movimiento de inventario (tipo: Ingreso/Actualización)
                controladorInventario.registrarMovimiento(p.getId(), p.getNombre(), "IngresoStock", cantidad, "sistema");
                return;
            }
        }
        System.out.println("❌ ERROR: Producto con ID " + id + " no encontrado para incrementar stock.");
    }

    /**
     * Vender un producto (disminuir stock) y registrar el movimiento en el historial.
     * @param id id del producto
     * @param cantidad cantidad a vender
     * @param usuario usuario que realiza la venta (cadena)
     */
    public void venderProducto(int id, int cantidad, String usuario) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                if (p.getStockActual() < cantidad) {
                    System.out.println("❌ ERROR: Stock insuficiente para vender. Disponible: " + p.getStockActual());
                    return;
                }
                p.setStockActual(p.getStockActual() - cantidad);
                System.out.println("✅ Venta: " + cantidad + " unidades de " + p.getNombre() + ". Stock restante: " + p.getStockActual());
                // actualizar DB
                productoDAO.actualizarStock(p.getId(), p.getStockActual(), p.getCantidad());
                controladorInventario.registrarMovimiento(p.getId(), p.getNombre(), "Venta", cantidad, usuario == null ? "anónimo" : usuario);
                return;
            }
        }
        System.out.println("❌ ERROR: Producto con ID " + id + " no encontrado para venta.");
    }

    public void crearProducto(String nombre, String categoria, double precio, int stock, String descripcion, int stockMinimo) {
        Producto nuevoProducto = new Producto(0, nombre, categoria, precio, stock, descripcion, stock, stockMinimo);
        // insertar en DB y obtener id
        productoDAO.insertar(nuevoProducto);
        productos.add(nuevoProducto);
        // actualizar nextId si es necesario
        if (nuevoProducto.getId() >= nextId) nextId = nuevoProducto.getId() + 1;
        System.out.println("✅ Producto '" + nombre + "' creado y guardado en DB con ID " + nuevoProducto.getId() + ".");
    }

    /**
     * Editar un producto existente: actualiza en memoria y en la base de datos.
     * @param actualizado producto con los nuevos valores (debe contener id válido)
     * @return true si se actualizó correctamente
     */
    public boolean editarProducto(Producto actualizado) {
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            if (p.getId() == actualizado.getId()) {
                // actualizar campos en memoria
                p.setNombre(actualizado.getNombre());
                p.setCategoria(actualizado.getCategoria());
                p.setPrecio(actualizado.getPrecio());
                p.setCantidad(actualizado.getCantidad());
                p.setDescripcion(actualizado.getDescripcion());
                p.setStockActual(actualizado.getStockActual());
                p.setStockMinimo(actualizado.getStockMinimo());

                // persistir cambios
                boolean ok = productoDAO.actualizarProducto(p);
                if (ok) {
                    System.out.println("✅ Producto ID " + p.getId() + " actualizado correctamente en DB.");
                } else {
                    System.out.println("⚠️ Error al actualizar producto ID " + p.getId() + " en DB.");
                }
                return ok;
            }
        }
        System.out.println("❌ ERROR: Producto con ID " + actualizado.getId() + " no encontrado para editar.");
        return false;
    }

    /**
     * Eliminar un producto de la base de datos y de la memoria.
     * @param id id del producto a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarProducto(int id) {
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            if (p.getId() == id) {
                // eliminar de DB
                boolean ok = productoDAO.eliminar(id);
                if (ok) {
                    productos.remove(i);
                    System.out.println("✅ Producto ID " + id + " eliminado correctamente.");
                    // registrar movimiento de eliminación en historial
                    controladorInventario.registrarMovimiento(id, p.getNombre(), "Eliminación", 0, "sistema");
                } else {
                    System.out.println("⚠️ Error al eliminar producto ID " + id + " en DB.");
                }
                return ok;
            }
        }
        System.out.println("❌ ERROR: Producto con ID " + id + " no encontrado para eliminar.");
        return false;
    }
}
