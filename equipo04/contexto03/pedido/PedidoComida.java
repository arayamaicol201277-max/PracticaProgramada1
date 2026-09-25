package equipo04.contexto03.pedido;

import equipo04.contexto03.opciones.Acompanamiento;
import equipo04.contexto03.opciones.Aderezo;
import equipo04.contexto03.opciones.GradoCoccion;
import equipo04.contexto03.opciones.PreparacionProteina;
import equipo04.contexto03.opciones.TipoPan;
import equipo04.contexto03.opciones.TipoProteina;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Rol en el patrón: PRODUCT.
 * <p>
 * Representa el pedido ya consolidado que pasa a la línea de preparación.
 * Es inmutable: todos sus atributos son finales y las listas se exponen como
 * solo lectura, de modo que la cocina no puede recibir un pedido a medio armar
 * ni modificado después de confirmarse.
 * <p>
 * El constructor es de visibilidad de paquete: el cliente no puede instanciarlo
 * directamente, solo a través de un builder.
 */
public final class PedidoComida {

    private static final AtomicInteger SECUENCIA = new AtomicInteger(1000);

    private final int numeroOrden;
    private final String cliente;
    private final TipoPan pan;
    private final TipoProteina proteina;
    private final PreparacionProteina preparacion;
    private final GradoCoccion coccion;
    private final List<Aderezo> aderezos;
    private final List<Acompanamiento> acompanamientos;
    private final String notas;

    PedidoComida(String cliente, TipoPan pan, TipoProteina proteina,
                 PreparacionProteina preparacion, GradoCoccion coccion,
                 List<Aderezo> aderezos, List<Acompanamiento> acompanamientos,
                 String notas) {
        this.numeroOrden = SECUENCIA.incrementAndGet();
        this.cliente = cliente;
        this.pan = pan;
        this.proteina = proteina;
        this.preparacion = preparacion;
        this.coccion = coccion;
        // Copias defensivas: el pedido no comparte estado con el builder.
        this.aderezos = Collections.unmodifiableList(new ArrayList<>(aderezos));
        this.acompanamientos = Collections.unmodifiableList(new ArrayList<>(acompanamientos));
        this.notas = notas;
    }

    public int getNumeroOrden() { return numeroOrden; }
    public String getCliente() { return cliente; }
    public TipoPan getPan() { return pan; }
    public TipoProteina getProteina() { return proteina; }
    public PreparacionProteina getPreparacion() { return preparacion; }
    public GradoCoccion getCoccion() { return coccion; }
    public List<Aderezo> getAderezos() { return aderezos; }
    public List<Acompanamiento> getAcompanamientos() { return acompanamientos; }
    public String getNotas() { return notas; }

    /** Precio base de la proteína + aderezos con costo + acompañamientos. */
    public BigDecimal calcularTotal() {
        BigDecimal total = proteina.getPrecioBase();
        for (Aderezo a : aderezos) {
            total = total.add(a.getPrecioExtra());
        }
        for (Acompanamiento a : acompanamientos) {
            total = total.add(a.getPrecio());
        }
        return total;
    }

    /** Genera el tiquete que se envía a la línea de preparación. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ------------------------------------------\n");
        sb.append(String.format("  ORDEN #%d  |  Cliente: %s%n", numeroOrden, cliente));
        sb.append("  ------------------------------------------\n");
        sb.append("  Pan ............ ").append(pan.getDescripcion()).append('\n');
        sb.append("  Proteína ....... ").append(proteina.getDescripcion())
          .append(" (").append(preparacion.getDescripcion()).append(")\n");
        sb.append("  Cocción ........ ").append(coccion.getDescripcion()).append('\n');
        sb.append("  Aderezos ....... ").append(aderezos.isEmpty() ? "Ninguno" : describirAderezos()).append('\n');
        sb.append("  Acompañamientos  ").append(acompanamientos.isEmpty() ? "Ninguno" : describirAcompanamientos()).append('\n');
        if (notas != null && !notas.isEmpty()) {
            sb.append("  Notas .......... ").append(notas).append('\n');
        }
        sb.append(String.format("  TOTAL .......... ¢%,.2f%n", calcularTotal()));
        sb.append("  ------------------------------------------");
        return sb.toString();
    }

    private String describirAderezos() {
        List<String> nombres = new ArrayList<>();
        for (Aderezo a : aderezos) {
            nombres.add(a.getDescripcion());
        }
        return String.join(", ", nombres);
    }

    private String describirAcompanamientos() {
        List<String> nombres = new ArrayList<>();
        for (Acompanamiento a : acompanamientos) {
            nombres.add(a.getDescripcion());
        }
        return String.join(", ", nombres);
    }
}
