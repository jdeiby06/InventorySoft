package modelo;

public class HistorialInventario {
    private int id;
    private int idProducto;
    private int fechaInventario;
    private String producto;
    private String tipoMovimiento;
    private int cantidad;
    private String fecha;
    private String usuario;

    public HistorialInventario(int id, int idProducto, int fechaInventario) {
        this.id = id;
        this.idProducto = idProducto;
        this.fechaInventario = fechaInventario;
        this.producto = "";
        this.tipoMovimiento = "";
        this.cantidad = 0;
        this.fecha = "";
        this.usuario = "";
    }

    /**
     * Constructor con detalles completos para registrar movimientos reales.
     */
    public HistorialInventario(int id, int idProducto, String producto, String tipoMovimiento, int cantidad, String fecha, String usuario) {
        this.id = id;
        this.idProducto = idProducto;
        this.producto = producto;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.usuario = usuario;
        this.fechaInventario = 0;
    }
    public int getId(){
        return id;
    }
    public void setId (int id){
        this.id = id;
    }
    public int getIdProducto(){
        return idProducto;
    }
    public void setIdProducto(int idProducto){
        this.idProducto = idProducto;
    }
    public int getFechaInventario(){
        return fechaInventario;
    }
    public void setFechaInventario(int fechaInventario){
        this.fechaInventario = fechaInventario;
    }
    public String getProducto() {
        return producto;
    }
    public void setProducto(String producto) {
        this.producto = producto;
    }
    public String getTipoMovimiento() {
        return tipoMovimiento;
    }
    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
