package equipo04.contexto03.opciones;

/**
 * Forma en que se prepara la proteína.
 */
public enum PreparacionProteina {
    A_LA_PARRILLA("A la parrilla"),
    EMPANIZADA("Empanizada"),
    AHUMADA("Ahumada"),
    A_LA_PLANCHA("A la plancha");

    private final String descripcion;

    PreparacionProteina(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
