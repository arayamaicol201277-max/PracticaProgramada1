package equipo04.contexto03.opciones;

import java.math.BigDecimal;

/**
 * Catálogo de acompañamientos opcionales con su precio.
 */
public enum Acompanamiento {
    PAPAS_FRITAS("Papas fritas", new BigDecimal("1200")),
    AROS_DE_CEBOLLA("Aros de cebolla", new BigDecimal("1400")),
    ENSALADA("Ensalada verde", new BigDecimal("1000")),
    REFRESCO("Refresco natural", new BigDecimal("900"));

    private final String descripcion;
    private final BigDecimal precio;

    Acompanamiento(String descripcion, BigDecimal precio) {
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }
}
