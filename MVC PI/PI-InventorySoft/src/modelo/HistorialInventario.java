package modelo;

public class HistorialInventario {
    private int id;
    private int idProducto;
    private int fechaInventario;

    public HistorialInventario(int id, int idProducto, int fechaInventario) {
        this.id = id;
        this.idProducto = idProducto;
        this.fechaInventario = fechaInventario;
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
    
}
