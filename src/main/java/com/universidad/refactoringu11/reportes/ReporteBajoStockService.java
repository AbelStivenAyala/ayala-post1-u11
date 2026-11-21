package com.universidad.refactoringu11.reportes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.universidad.refactoringu11.inventario.NotificadorBajoStock;
import com.universidad.refactoringu11.inventario.Producto;

@Service
public class ReporteBajoStockService implements NotificadorBajoStock {

    private final List<String> alertas = new ArrayList<>();

    @Override
    public void notificar(Producto producto, int cantidadReorden) {
        alertas.add(producto.getNombre() + ": reordenar " + cantidadReorden + " unidades");
    }

    public String generarReporte() {
        if (alertas.isEmpty()) {
            return "Reporte de bajo stock: sin alertas";
        }
        return "Reporte de bajo stock:\n" + String.join("\n", alertas);
    }
}