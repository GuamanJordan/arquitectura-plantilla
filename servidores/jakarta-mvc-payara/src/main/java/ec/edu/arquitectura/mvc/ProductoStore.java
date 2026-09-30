package ec.edu.arquitectura.mvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public final class ProductoStore {
    private static final ProductoStore INSTANCE = new ProductoStore();
    private final AtomicLong sequence = new AtomicLong(1);
    private final Map<Long, Producto> productos = new LinkedHashMap<>();

    private ProductoStore() {
        crear("Laptop", "Equipo de prueba", new BigDecimal("850.00"), 10);
        crear("Mouse", "Periferico de prueba", new BigDecimal("15.50"), 50);
    }

    public static ProductoStore getInstance() {
        return INSTANCE;
    }

    public synchronized List<Producto> listar() {
        return new ArrayList<>(productos.values());
    }

    public synchronized Optional<Producto> buscar(long id) {
        return Optional.ofNullable(productos.get(id));
    }

    public synchronized Producto crear(String nombre, String descripcion, BigDecimal precio, int stock) {
        long id = sequence.getAndIncrement();
        Producto producto = new Producto(id, nombre, descripcion, precio, stock);
        productos.put(id, producto);
        return producto;
    }

    public synchronized void actualizar(long id, String nombre, String descripcion, BigDecimal precio, int stock) {
        productos.put(id, new Producto(id, nombre, descripcion, precio, stock));
    }
}
