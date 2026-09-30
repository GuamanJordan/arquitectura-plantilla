package ec.edu.arquitectura.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@WebService(serviceName = "ProductoSoapService")
public class ProductoSoapService {
    private static final Map<Long, Producto> PRODUCTOS = new LinkedHashMap<>();
    private static final AtomicLong SEQUENCE = new AtomicLong(1);

    static {
        crearInicial("Laptop", "Equipo de prueba", "850.00", 10);
        crearInicial("Mouse", "Periferico de prueba", "15.50", 50);
    }

    @WebMethod
    public List<Producto> listarProductos() {
        return new ArrayList<>(PRODUCTOS.values());
    }

    @WebMethod
    public Producto buscarProductoPorId(@WebParam(name = "id") long id) {
        return PRODUCTOS.get(id);
    }

    @WebMethod
    public Producto crearProducto(@WebParam(name = "producto") Producto producto) {
        long id = SEQUENCE.getAndIncrement();
        producto.setId(id);
        PRODUCTOS.put(id, producto);
        return producto;
    }

    private static void crearInicial(String nombre, String descripcion, String precio, int stock) {
        long id = SEQUENCE.getAndIncrement();
        PRODUCTOS.put(id, new Producto(id, nombre, descripcion, new BigDecimal(precio), stock));
    }
}
