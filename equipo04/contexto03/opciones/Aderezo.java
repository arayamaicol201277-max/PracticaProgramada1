package equipo04.contexto03.opciones;

import java.math.BigDecimal;

/**
 * Catálogo de aderezos con su costo adicional.
 */
public enum Aderezo {
    MAYONESA("Mayonesa", BigDecimal.ZERO),
    MOSTAZA("Mostaza", BigDecimal.ZERO),
    KETCHUP("Salsa de tomate", BigDecimal.ZERO),
    BBQ("Salsa BBQ", new BigDecimal("250")),
    CHIPOTLE("Mayonesa chipotle", new BigDecimal("300"));

    private final String descripcion;
    private final BigDecimal precioExtra;

    Aderezo(String descripcion, BigDecimal precioExtra) {
        this.descripcion = descripcion;
        this.precioExtra = precioExtra;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecioExtra() {
        return precioExtra;
    }
}
