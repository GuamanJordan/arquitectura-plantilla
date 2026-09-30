package ec.edu.arquitectura.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ProductoRepository {
    private static final ProductoRepository INSTANCE = new ProductoRepository();

    private final Map<Long, Producto> productos = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    private ProductoRepository() {
        crear(new Producto(0, "Laptop", "Equipo de prueba", new BigDecimal("850.00"), 10));
        crear(new Producto(0, "Mouse", "Periferico de prueba", new BigDecimal("15.50"), 50));
        crear(new Producto(0, "Teclado", "Periferico de prueba", new BigDecimal("28.90"), 30));
    }

    public static ProductoRepository getInstance() {
        return INSTANCE;
    }

    public synchronized List<Producto> listar() {
        return new ArrayList<>(productos.values());
    }

    public synchronized Optional<Producto> buscar(long id) {
        return Optional.ofNullable(productos.get(id));
    }

    public synchronized Producto crear(Producto producto) {
        long id = sequence.getAndIncrement();
        Producto nuevo = new Producto(id, producto.nombre, producto.descripcion, producto.precio, producto.stock);
        productos.put(id, nuevo);
        return nuevo;
    }

    public synchronized Optional<Producto> actualizar(long id, Producto producto) {
        if (!productos.containsKey(id)) {
            return Optional.empty();
        }
        Producto actualizado = new Producto(id, producto.nombre, producto.descripcion, producto.precio, producto.stock);
        productos.put(id, actualizado);
        return Optional.of(actualizado);
    }

    public synchronized boolean eliminar(long id) {
        return productos.remove(id) != null;
    }
}
