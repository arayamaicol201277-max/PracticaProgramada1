package equipo04.contexto03.pedido;

import equipo04.contexto03.opciones.Acompanamiento;
import equipo04.contexto03.opciones.Aderezo;
import equipo04.contexto03.opciones.GradoCoccion;
import equipo04.contexto03.opciones.PreparacionProteina;
import equipo04.contexto03.opciones.TipoPan;
import equipo04.contexto03.opciones.TipoProteina;

import java.util.ArrayList;
import java.util.List;

/**
 * Rol en el patrón: CONCRETE BUILDER.
 * <p>
 * Acumula las elecciones del cliente paso a paso y concentra todas las reglas
 * de validación en {@link #construir()}. Así, la línea de preparación nunca
 * recibe un pedido inconsistente y el producto no necesita conocer esas reglas.
 */
public final class PedidoComidaBuilder implements IPedidoBuilder {

    public static final int MAX_ADEREZOS = 3;
    public static final int MAX_ACOMPANAMIENTOS = 2;

    private String cliente;
    private TipoPan pan;
    private TipoProteina proteina;
    private PreparacionProteina preparacion;
    private GradoCoccion coccion;
    private List<Aderezo> aderezos;
    private List<Acompanamiento> acompanamientos;
    private String notas;

    public PedidoComidaBuilder() {
        reiniciar();
    }

    @Override
    public IPedidoBuilder reiniciar() {
        cliente = null;
        pan = null;
        proteina = null;
        preparacion = null;
        coccion = null;
        aderezos = new ArrayList<>();
        acompanamientos = new ArrayList<>();
        notas = null;
        return this;
    }

    @Override
    public IPedidoBuilder paraCliente(String nombreCliente) {
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            throw new PedidoInvalidoException("El nombre del cliente no puede estar vacío.");
        }
        this.cliente = nombreCliente.trim();
        return this;
    }

    @Override
    public IPedidoBuilder conPan(TipoPan pan) {
        this.pan = requerido(pan, "tipo de pan");
        return this;
    }

    @Override
    public IPedidoBuilder conProteina(TipoProteina proteina) {
        this.proteina = requerido(proteina, "proteína");
        return this;
    }

    @Override
    public IPedidoBuilder conPreparacion(PreparacionProteina preparacion) {
        this.preparacion = requerido(preparacion, "preparación");
        return this;
    }

    @Override
    public IPedidoBuilder conCoccion(GradoCoccion coccion) {
        this.coccion = requerido(coccion, "grado de cocción");
        return this;
    }

    @Override
    public IPedidoBuilder agregarAderezo(Aderezo aderezo) {
        requerido(aderezo, "aderezo");
        if (aderezos.contains(aderezo)) {
            throw new PedidoInvalidoException("El aderezo '" + aderezo.getDescripcion() + "' ya fue agregado.");
        }
        if (aderezos.size() >= MAX_ADEREZOS) {
            throw new PedidoInvalidoException("Se permiten máximo " + MAX_ADEREZOS + " aderezos por pedido.");
        }
        aderezos.add(aderezo);
        return this;
    }

    @Override
    public IPedidoBuilder agregarAcompanamiento(Acompanamiento acompanamiento) {
        requerido(acompanamiento, "acompañamiento");
        if (acompanamientos.size() >= MAX_ACOMPANAMIENTOS) {
            throw new PedidoInvalidoException("Se permiten máximo " + MAX_ACOMPANAMIENTOS + " acompañamientos por pedido.");
        }
        acompanamientos.add(acompanamiento);
        return this;
    }

    @Override
    public IPedidoBuilder conNotas(String notas) {
        this.notas = (notas == null) ? null : notas.trim();
        return this;
    }

    @Override
    public PedidoComida construir() {
        validarObligatorios();
        GradoCoccion coccionFinal = resolverCoccion();
        PreparacionProteina preparacionFinal =
                (preparacion != null) ? preparacion : PreparacionProteina.A_LA_PARRILLA;

        PedidoComida pedido = new PedidoComida(cliente, pan, proteina, preparacionFinal,
                coccionFinal, aderezos, acompanamientos, notas);
        reiniciar(); // el builder queda limpio para el siguiente pedido
        return pedido;
    }

    // ------------------------------------------------------------------
    // Reglas de negocio (privadas: el cliente no las ve ni las duplica)
    // ------------------------------------------------------------------

    private void validarObligatorios() {
        List<String> faltantes = new ArrayList<>();
        if (cliente == null) faltantes.add("cliente");
        if (pan == null) faltantes.add("pan");
        if (proteina == null) faltantes.add("proteína");
        if (!faltantes.isEmpty()) {
            throw new PedidoInvalidoException("Pedido incompleto. Falta: " + String.join(", ", faltantes) + ".");
        }
    }

    /**
     * La res exige que el cliente elija el término. El resto de proteínas,
     * por inocuidad alimentaria, siempre se sirven bien cocidas.
     */
    private GradoCoccion resolverCoccion() {
        if (proteina.permiteElegirCoccion()) {
            if (coccion == null) {
                throw new PedidoInvalidoException("Debe indicar el grado de cocción para "
                        + proteina.getDescripcion() + ".");
            }
            return coccion;
        }
        if (coccion != null && coccion != GradoCoccion.BIEN_COCIDO) {
            throw new PedidoInvalidoException(proteina.getDescripcion()
                    + " solo puede servirse bien cocido (se solicitó: " + coccion.getDescripcion() + ").");
        }
        return GradoCoccion.BIEN_COCIDO;
    }

    private static <T> T requerido(T valor, String campo) {
        if (valor == null) {
            throw new PedidoInvalidoException("El campo '" + campo + "' no puede ser nulo.");
        }
        return valor;
    }
}
