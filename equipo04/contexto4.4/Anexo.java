package equipo04.contexto04;

import java.util.Objects;

public class Anexo {
    private final String nombre;
    private final byte[] contenido;

    public Anexo(String nombre, byte[] contenido) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        this.contenido = Objects.requireNonNull(contenido, "El contenido no puede ser nulo").clone();
    }

    public String getNombre() { return nombre; }
    public byte[] getContenido() { return contenido.clone(); }
}
