package modelo;

public class Producto{
    private int id;
    private String nombre;
    private String categoria;
    private double precio;
    private int cantidad;
    private String descripcion; // Agregado atributo descripcion
    private int stock_actual;
    private int stock_minimo = 20;
   

    public Producto(int id, String nombre, String categoria, double precio, int cantidad, String descripcion, int stock_actual, int stock_minimo) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.stock_actual = stock_actual;
        this.stock_minimo = stock_minimo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public int getStockActual() {
        return stock_actual;
    }
    public void setStockActual (int stock_actual){
        this.stock_actual = stock_actual;
    }
    public int getStockMinimo() {
        return stock_minimo;
    }
    public void setStockMinimo(int stock_minimo) {
        this.stock_minimo = stock_minimo;
    }

}
