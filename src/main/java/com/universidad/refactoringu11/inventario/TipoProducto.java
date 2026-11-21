package com.universidad.refactoringu11.inventario;

public enum TipoProducto {

    LIBRO("LIBRO", "Exento de IVA") {
        @Override
        public double calcularRecargoEnvio(double precio) { return 0.0; }
    },
    ELECTRONICO("ELECTRO", "Garantia 1 ano") {
        @Override
        public double calcularRecargoEnvio(double precio) { return precio * 0.08; }
    },
    ROPA("ROPA", "Ver talla") {
        @Override
        public double calcularRecargoEnvio(double precio) { return precio * 0.03; }
    },
    ALIMENTO("ALIMENTO", "Perecedero") {
        @Override
        public double calcularRecargoEnvio(double precio) { return 2.50; }
    };

    private final String prefijoEtiqueta;
    private final String notaEtiqueta;

    TipoProducto(String prefijoEtiqueta, String notaEtiqueta) {
        this.prefijoEtiqueta = prefijoEtiqueta;
        this.notaEtiqueta = notaEtiqueta;
    }

    public abstract double calcularRecargoEnvio(double precio);

    public String generarEtiqueta(String nombreProducto) {
        return "[" + prefijoEtiqueta + "] " + nombreProducto + " - " + notaEtiqueta;
    }
}