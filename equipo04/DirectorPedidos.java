package equipo04.contexto03.director;

import equipo04.contexto03.opciones.Acompanamiento;
import equipo04.contexto03.opciones.Aderezo;
import equipo04.contexto03.opciones.GradoCoccion;
import equipo04.contexto03.opciones.PreparacionProteina;
import equipo04.contexto03.opciones.TipoPan;
import equipo04.contexto03.opciones.TipoProteina;
import equipo04.contexto03.pedido.IPedidoBuilder;
import equipo04.contexto03.pedido.PedidoComida;

/**
 * Rol en el patrón: DIRECTOR.
 * <p>
 * Conoce la secuencia de pasos de los combos del menú y la ejecuta sobre
 * cualquier implementación de {@link IPedidoBuilder}. Recibe el builder por
 * constructor (inyección de dependencias), por lo que no está acoplado a
 * ninguna clase concreta.
 */
public class DirectorPedidos {

    private final IPedidoBuilder builder;

    public DirectorPedidos(IPedidoBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException("El director requiere un builder.");
        }
        this.builder = builder;
    }

    /** Combo Clásico: hamburguesa de res término medio con papas y refresco. */
    public PedidoComida prepararComboClasico(String cliente) {
        return builder.reiniciar()
                .paraCliente(cliente)
                .conPan(TipoPan.AJONJOLI)
                .conProteina(TipoProteina.RES)
                .conPreparacion(PreparacionProteina.A_LA_PARRILLA)
                .conCoccion(GradoCoccion.MEDIO)
                .agregarAderezo(Aderezo.KETCHUP)
                .agregarAderezo(Aderezo.MOSTAZA)
                .agregarAcompanamiento(Acompanamiento.PAPAS_FRITAS)
                .agregarAcompanamiento(Acompanamiento.REFRESCO)
                .construir();
    }

    /** Combo Crispy: pollo empanizado en brioche con aros de cebolla. */
    public PedidoComida prepararComboCrispy(String cliente) {
        return builder.reiniciar()
                .paraCliente(cliente)
                .conPan(TipoPan.BRIOCHE)
                .conProteina(TipoProteina.POLLO)
                .conPreparacion(PreparacionProteina.EMPANIZADA)
                .agregarAderezo(Aderezo.CHIPOTLE)
                .agregarAcompanamiento(Acompanamiento.AROS_DE_CEBOLLA)
                .construir();
    }

    /** Combo Verde: opción vegetariana en pan integral con ensalada. */
    public PedidoComida prepararComboVerde(String cliente) {
        return builder.reiniciar()
                .paraCliente(cliente)
                .conPan(TipoPan.INTEGRAL)
                .conProteina(TipoProteina.VEGETARIANA)
                .conPreparacion(PreparacionProteina.A_LA_PLANCHA)
                .agregarAderezo(Aderezo.MOSTAZA)
                .agregarAcompanamiento(Acompanamiento.ENSALADA)
                .construir();
    }
}
