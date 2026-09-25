package equipo04.contexto03.opciones;

/**
 * Término de cocción de la proteína.
 */
public enum GradoCoccion {
    ROJO("Término rojo"),
    MEDIO("Término medio"),
    TRES_CUARTOS("Tres cuartos"),
    BIEN_COCIDO("Bien cocido");

    private final String descripcion;

    GradoCoccion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
