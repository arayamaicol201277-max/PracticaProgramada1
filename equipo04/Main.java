package equipo04.contexto03;

import equipo04.contexto03.director.DirectorPedidos;
import equipo04.contexto03.opciones.Acompanamiento;
import equipo04.contexto03.opciones.Aderezo;
import equipo04.contexto03.opciones.GradoCoccion;
import equipo04.contexto03.opciones.PreparacionProteina;
import equipo04.contexto03.opciones.TipoPan;
import equipo04.contexto03.opciones.TipoProteina;
import equipo04.contexto03.pedido.IPedidoBuilder;
import equipo04.contexto03.pedido.PedidoComida;
import equipo04.contexto03.pedido.PedidoComidaBuilder;
import equipo04.contexto03.pedido.PedidoInvalidoException;

import java.math.BigDecimal;

/**
 * Rol en el patrón: CLIENT.
 * <p>
 * Demostración y casos de prueba del Contexto 4.3 (patrón Builder):
 * plataforma de pedidos personalizados de una cadena de comida rápida.
 */
public class Main {

    private static int pruebasOk = 0;
    private static int pruebasFallidas = 0;

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println(" Contexto 4.3 - Patrón Builder (Pedidos)");
        System.out.println("==============================================\n");

        // El cliente depende de la abstracción, no de la clase concreta.
        IPedidoBuilder builder = new PedidoComidaBuilder();
        DirectorPedidos director = new DirectorPedidos(builder);

        demostrarCombosDelDirector(director);
        demostrarPedidoPersonalizado(builder);
        ejecutarCasosBorde(builder);

        System.out.println("\n==============================================");
        System.out.printf(" Resultado: %d pruebas correctas, %d fallidas%n", pruebasOk, pruebasFallidas);
        System.out.println("==============================================");
    }

    // ------------------------------------------------------------------
    // 1. Construcción guiada por el Director (combos del menú)
    // ------------------------------------------------------------------
    private static void demostrarCombosDelDirector(DirectorPedidos director) {
        System.out.println(">> 1. Combos predefinidos construidos por el Director\n");

        PedidoComida clasico = director.prepararComboClasico("Ana Rojas");
        System.out.println(clasico + "\n");
        verificar("Combo Clásico usa res término medio",
                clasico.getProteina() == TipoProteina.RES && clasico.getCoccion() == GradoCoccion.MEDIO);
        verificar("Total del Combo Clásico = ¢5,600 (3500 + 1200 + 900)",
                clasico.calcularTotal().compareTo(new BigDecimal("5600")) == 0);

        PedidoComida crispy = director.prepararComboCrispy("Luis Salas");
        System.out.println(crispy + "\n");
        verificar("Pollo se asigna automáticamente como bien cocido",
                crispy.getCoccion() == GradoCoccion.BIEN_COCIDO);

        PedidoComida verde = director.prepararComboVerde("María Jiménez");
        System.out.println(verde + "\n");
        verificar("Combo Verde usa proteína vegetariana",
                verde.getProteina() == TipoProteina.VEGETARIANA);
    }

    // ------------------------------------------------------------------
    // 2. Construcción paso a paso hecha directamente por el cliente
    // ------------------------------------------------------------------
    private static void demostrarPedidoPersonalizado(IPedidoBuilder builder) {
        System.out.println();
        System.out.println(">> 2. Pedido totalmente personalizado (sin Director)\n");

        PedidoComida pedido = builder
                .paraCliente("Carlos Mora")
                .conPan(TipoPan.SIN_GLUTEN)
                .conProteina(TipoProteina.RES)
                .conPreparacion(PreparacionProteina.AHUMADA)
                .conCoccion(GradoCoccion.TRES_CUARTOS)
                .agregarAderezo(Aderezo.BBQ)
                .agregarAderezo(Aderezo.CHIPOTLE)
                .agregarAcompanamiento(Acompanamiento.ENSALADA)
                .conNotas("Sin cebolla, alergia a los lácteos")
                .construir();

        System.out.println(pedido + "\n");
        verificar("Se respetan pan, preparación y cocción elegidos",
                pedido.getPan() == TipoPan.SIN_GLUTEN
                        && pedido.getPreparacion() == PreparacionProteina.AHUMADA
                        && pedido.getCoccion() == GradoCoccion.TRES_CUARTOS);
        verificar("Total con extras = ¢5,050 (3500 + 250 + 300 + 1000)",
                pedido.calcularTotal().compareTo(new BigDecimal("5050")) == 0);

        // Pedido mínimo: solo lo obligatorio. Se aplican valores por defecto.
        PedidoComida minimo = builder
                .paraCliente("Sofía Vargas")
                .conPan(TipoPan.BRIOCHE)
                .conProteina(TipoProteina.CERDO)
                .construir();
        System.out.println(minimo + "\n");
        verificar("Pedido mínimo: preparación por defecto 'A la parrilla' y sin extras",
                minimo.getPreparacion() == PreparacionProteina.A_LA_PARRILLA
                        && minimo.getAderezos().isEmpty()
                        && minimo.getAcompanamientos().isEmpty());
    }

    // ------------------------------------------------------------------
    // 3. Casos borde y reglas de negocio
    // ------------------------------------------------------------------
    private static void ejecutarCasosBorde(IPedidoBuilder builder) {
        System.out.println();
        System.out.println(">> 3. Casos borde y validaciones\n");

        esperarRechazo("Pedido sin pan ni proteína se rechaza", () ->
                builder.reiniciar().paraCliente("Sin datos").construir());

        esperarRechazo("Res sin grado de cocción se rechaza", () ->
                builder.reiniciar().paraCliente("Test").conPan(TipoPan.BRIOCHE)
                        .conProteina(TipoProteina.RES).construir());

        esperarRechazo("Pollo término rojo se rechaza (inocuidad)", () ->
                builder.reiniciar().paraCliente("Test").conPan(TipoPan.BRIOCHE)
                        .conProteina(TipoProteina.POLLO).conCoccion(GradoCoccion.ROJO).construir());

        esperarRechazo("Más de 3 aderezos se rechaza", () ->
                builder.reiniciar()
                        .agregarAderezo(Aderezo.MAYONESA).agregarAderezo(Aderezo.MOSTAZA)
                        .agregarAderezo(Aderezo.KETCHUP).agregarAderezo(Aderezo.BBQ));

        esperarRechazo("Aderezo duplicado se rechaza", () ->
                builder.reiniciar().agregarAderezo(Aderezo.BBQ).agregarAderezo(Aderezo.BBQ));

        esperarRechazo("Más de 2 acompañamientos se rechaza", () ->
                builder.reiniciar()
                        .agregarAcompanamiento(Acompanamiento.PAPAS_FRITAS)
                        .agregarAcompanamiento(Acompanamiento.REFRESCO)
                        .agregarAcompanamiento(Acompanamiento.ENSALADA));

        esperarRechazo("Nombre de cliente vacío se rechaza", () ->
                builder.reiniciar().paraCliente("   "));

        esperarRechazo("Valor nulo en un paso se rechaza", () ->
                builder.reiniciar().conPan(null));

        esperarExcepcion("El Director no acepta un builder nulo",
                IllegalArgumentException.class, () -> new DirectorPedidos(null));

        // Valor límite permitido: una proteína que no es res acepta "bien cocido" explícito.
        PedidoComida vegetarianoExplicito = builder.reiniciar().paraCliente("Test")
                .conPan(TipoPan.INTEGRAL).conProteina(TipoProteina.VEGETARIANA)
                .conCoccion(GradoCoccion.BIEN_COCIDO).construir();
        verificar("Proteína distinta de res con 'bien cocido' explícito se acepta",
                vegetarianoExplicito.getCoccion() == GradoCoccion.BIEN_COCIDO);

        // Si construir() falla, el builder conserva lo ya ingresado:
        // el cliente corrige solo el paso faltante y vuelve a consolidar.
        builder.reiniciar().paraCliente("Diego Solano")
                .conPan(TipoPan.AJONJOLI).conProteina(TipoProteina.RES);
        esperarRechazo("Primer intento sin cocción de res se rechaza", builder::construir);
        PedidoComida corregido = builder.conCoccion(GradoCoccion.ROJO).construir();
        verificar("Tras agregar la cocción, el mismo builder consolida el pedido corregido",
                "Diego Solano".equals(corregido.getCliente())
                        && corregido.getPan() == TipoPan.AJONJOLI
                        && corregido.getCoccion() == GradoCoccion.ROJO);

        // El builder se reinicia tras construir: no arrastra datos al siguiente pedido.
        builder.reiniciar();
        PedidoComida primero = builder.paraCliente("Uno").conPan(TipoPan.INTEGRAL)
                .conProteina(TipoProteina.POLLO).agregarAderezo(Aderezo.MAYONESA).construir();
        PedidoComida segundo = builder.paraCliente("Dos").conPan(TipoPan.INTEGRAL)
                .conProteina(TipoProteina.POLLO).construir();
        verificar("El builder se reinicia: el 2.º pedido no hereda aderezos del 1.º",
                primero.getAderezos().size() == 1 && segundo.getAderezos().isEmpty());
        verificar("Cada construcción produce un objeto distinto con su propio número de orden",
                primero != segundo && primero.getNumeroOrden() != segundo.getNumeroOrden());

        // El producto es inmutable: la lista expuesta no se puede alterar.
        esperarExcepcion("La lista de aderezos del pedido es de solo lectura",
                UnsupportedOperationException.class,
                () -> primero.getAderezos().add(Aderezo.BBQ));
    }

    // ------------------------------------------------------------------
    // Utilidades de prueba
    // ------------------------------------------------------------------
    private static void verificar(String descripcion, boolean condicion) {
        if (condicion) {
            pruebasOk++;
            System.out.println("  [OK]    " + descripcion);
        } else {
            pruebasFallidas++;
            System.out.println("  [FALLA] " + descripcion);
        }
    }

    private static void esperarRechazo(String descripcion, Runnable accion) {
        esperarExcepcion(descripcion, PedidoInvalidoException.class, accion);
    }

    private static void esperarExcepcion(String descripcion, Class<? extends Exception> tipo, Runnable accion) {
        try {
            accion.run();
            verificar(descripcion + " (no se lanzó excepción)", false);
        } catch (Exception e) {
            if (tipo.isInstance(e)) {
                pruebasOk++;
                String detalle = e.getMessage() != null ? " -> " + e.getMessage() : "";
                System.out.println("  [OK]    " + descripcion + detalle);
            } else {
                verificar(descripcion + " (excepción inesperada: " + e + ")", false);
            }
        }
        System.out.println();
    }
}
