package equipo04.contexto03.pedido;

import equipo04.contexto03.opciones.Acompanamiento;
import equipo04.contexto03.opciones.Aderezo;
import equipo04.contexto03.opciones.GradoCoccion;
import equipo04.contexto03.opciones.PreparacionProteina;
import equipo04.contexto03.opciones.TipoPan;
import equipo04.contexto03.opciones.TipoProteina;

/**
 * Rol en el patrón: BUILDER (interfaz abstracta).
 * <p>
 * Declara los pasos para formular un pedido. Cada paso devuelve el propio
 * builder (interfaz fluida) para encadenar llamadas. El Director y el cliente
 * dependen de esta abstracción y no de la clase concreta (DIP).
 */
public interface IPedidoBuilder {

    IPedidoBuilder reiniciar();

    IPedidoBuilder paraCliente(String nombreCliente);

    IPedidoBuilder conPan(TipoPan pan);

    IPedidoBuilder conProteina(TipoProteina proteina);

    IPedidoBuilder conPreparacion(PreparacionProteina preparacion);

    IPedidoBuilder conCoccion(GradoCoccion coccion);

    IPedidoBuilder agregarAderezo(Aderezo aderezo);

    IPedidoBuilder agregarAcompanamiento(Acompanamiento acompanamiento);

    IPedidoBuilder conNotas(String notas);

    /**
     * Valida las reglas de negocio y consolida el objeto final.
     * Después de construir, el builder queda reiniciado y listo para otro pedido.
     *
     * @throws PedidoInvalidoException si el pedido está incompleto o es incoherente
     */
    PedidoComida construir();
}
