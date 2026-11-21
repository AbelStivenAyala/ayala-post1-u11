package com.universidad.refactoringu11.inventario;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class InventarioService {

    private static final String ENTRADA = "ENTRADA";
    private static final String SALIDA = "SALIDA";
    private static final int FACTOR_REORDEN = 3;

    private final List<Producto> productos = new ArrayList<>();
    private final List<String> movimientos = new ArrayList<>();

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public String actualizarInventario(Long productoId, int cantidad, String tipoMovimiento) {
        if (!sonDatosValidos(productoId, cantidad, tipoMovimiento)) return "ERROR_DATOS_INVALIDOS";
        Producto producto = buscarProducto(productoId);
        if (producto == null) return "ERROR_PRODUCTO_NO_EXISTE";
        if (!aplicarMovimiento(producto, cantidad, tipoMovimiento)) return "ERROR_STOCK_INSUFICIENTE";
        registrarMovimiento(producto, cantidad, tipoMovimiento);
        return necesitaReorden(producto)
                ? alertarBajoStock(producto, calcularCantidadReorden(producto))
                : "OK_" + producto.getStock();
    }

    private boolean sonDatosValidos(Long productoId, int cantidad, String tipoMovimiento) {
        return productoId != null
                && cantidad != 0
                && (tipoMovimiento.equals(ENTRADA) || tipoMovimiento.equals(SALIDA));
    }

    private Producto buscarProducto(Long productoId) {
        return productos.stream()
                .filter(p -> p.getId().equals(productoId))
                .findFirst()
                .orElse(null);
    }

    // Devuelve false (sin modificar el stock) si una SALIDA dejaria el stock negativo
    private boolean aplicarMovimiento(Producto producto, int cantidad, String tipoMovimiento) {
        int nuevoStock = tipoMovimiento.equals(ENTRADA)
                ? producto.getStock() + cantidad
                : producto.getStock() - cantidad;
        if (tipoMovimiento.equals(SALIDA) && nuevoStock < 0) return false;
        producto.setStock(nuevoStock);
        return true;
    }

    private boolean necesitaReorden(Producto producto) {
        return producto.getStock() < producto.getStockMinimo();
    }

    private int calcularCantidadReorden(Producto producto) {
        return (producto.getStockMinimo() * FACTOR_REORDEN) - producto.getStock();
    }

    private void registrarMovimiento(Producto producto, int cantidad, String tipoMovimiento) {
        String registro = tipoMovimiento + " de " + cantidad + " unidades de "
                + producto.getNombre() + " -> stock final: " + producto.getStock();
        movimientos.add(registro);
        System.out.println(registro);
    }

    private String alertarBajoStock(Producto producto, int cantidadReorden) {
        System.out.println("ALERTA: " + producto.getNombre()
                + " bajo stock minimo. Reordenar " + cantidadReorden + " unidades.");
        return "OK_BAJO_STOCK_REORDEN_" + cantidadReorden;
    }
}