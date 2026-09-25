package equipo04.contexto03.opciones;

/**
 * Catálogo de tipos de pan disponibles para un pedido.
 */
public enum TipoPan {
    BRIOCHE("Pan brioche"),
    AJONJOLI("Pan con ajonjolí"),
    INTEGRAL("Pan integral"),
    SIN_GLUTEN("Pan sin gluten");

    private final String descripcion;

    TipoPan(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
