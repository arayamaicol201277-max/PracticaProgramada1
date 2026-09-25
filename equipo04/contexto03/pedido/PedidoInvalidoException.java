package equipo04.contexto03.pedido;

/**
 * Se lanza cuando un paso de construcción recibe un dato inválido
 * o cuando el pedido no cumple las reglas de negocio al consolidarse.
 */
public class PedidoInvalidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PedidoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
