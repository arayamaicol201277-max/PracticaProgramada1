package equipo04.contexto04;

import java.util.Objects;

public class Clausula {
    private final String titulo;
    private final String texto;

    public Clausula(String titulo, String texto) {
        this.titulo = Objects.requireNonNull(titulo, "El título no puede ser nulo");
        this.texto = Objects.requireNonNull(texto, "El texto no puede ser nulo");
    }

    public String getTitulo() { return titulo; }
    public String getTexto() { return texto; }
}
