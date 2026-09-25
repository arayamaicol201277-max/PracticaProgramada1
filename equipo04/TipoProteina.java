package equipo04.contexto03.opciones;

import java.math.BigDecimal;

/**
 * Catálogo de proteínas. Cada proteína define su precio base y si
 * admite que el cliente elija el grado de cocción (regla de inocuidad).
 */
public enum TipoProteina {
    RES("Carne de res", new BigDecimal("3500"), true),
    POLLO("Pechuga de pollo", new BigDecimal("3000"), false),
    CERDO("Lomo de cerdo", new BigDecimal("3200"), false),
    VEGETARIANA("Torta de garbanzo", new BigDecimal("2800"), false);

    private final String descripcion;
    private final BigDecimal precioBase;
    private final boolean permiteElegirCoccion;

    TipoProteina(String descripcion, BigDecimal precioBase, boolean permiteElegirCoccion) {
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.permiteElegirCoccion = permiteElegirCoccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    /** Solo la res puede servirse con un término distinto a "bien cocido". */
    public boolean permiteElegirCoccion() {
        return permiteElegirCoccion;
    }
}
