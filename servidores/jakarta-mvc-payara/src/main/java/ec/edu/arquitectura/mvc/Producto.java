package ec.edu.arquitectura.mvc;

import java.math.BigDecimal;

public class Producto {
    public long id;
    public String nombre;
    public String descripcion;
    public BigDecimal precio;
    public int stock;

    public Producto(long id, String nombre, String descripcion, BigDecimal precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }
}
