package presentacion;

import aplicacion.ServicioConsultas;
import dominio.CanalPersistencia;
import infraestructura.PoolCanales;

public final class Main {
    public static void main(String[] args) {
        PoolCanales pool = new PoolCanales(2);
        ServicioConsultas servicio = new ServicioConsultas(pool);

        CanalPersistencia primero = pool.adquirir();
        CanalPersistencia segundo = pool.adquirir();
        verificar(primero != segundo, "Dos peticiones reciben canales de persistencia distintos");
        esperarError(IllegalStateException.class, pool::adquirir,
                "Se rechaza un tercer préstamo: el límite es 2 canales de persistencia activos");

        primero.ejecutar("SELECT * FROM usuarios");
        pool.liberar(primero);
        CanalPersistencia reutilizado = pool.adquirir();
        verificar(reutilizado == primero, "Se reutiliza el mismo canal de persistencia");
        pool.liberar(reutilizado);
        esperarError(IllegalArgumentException.class, () -> pool.liberar(reutilizado),
                "Se rechaza una devolución duplicada");

        servicio.ejecutar("SELECT * FROM pedidos");
        esperarError(IllegalArgumentException.class, () -> servicio.ejecutar("  "),
                "Se rechaza una consulta en blanco");
        CanalPersistencia recuperado = pool.adquirir();
        verificar(recuperado == primero, "finally devuelve el canal aunque falle la consulta");
        pool.liberar(recuperado);
        esperarError(IllegalArgumentException.class, () -> servicio.ejecutar(null),
                "Se rechaza una consulta nula");
        pool.liberar(segundo);

        esperarError(IllegalArgumentException.class, () -> new PoolCanales(0),
                "Se rechaza la capacidad cero");
        esperarError(IllegalArgumentException.class, () -> new PoolCanales(-1),
                "Se rechaza una capacidad negativa");
        esperarError(IllegalArgumentException.class, () -> pool.liberar(null),
                "Se rechaza devolver null");

        PoolCanales otroPool = new PoolCanales(1);
        CanalPersistencia ajeno = otroPool.adquirir();
        esperarError(IllegalArgumentException.class, () -> pool.liberar(ajeno),
                "Se rechaza un canal de otro pool, aunque tenga el mismo identificador");
        otroPool.liberar(ajeno);

        CanalPersistencia finalUno = pool.adquirir();
        CanalPersistencia finalDos = pool.adquirir();
        verificar(finalUno != finalDos, "Los casos inválidos no perdieron ni duplicaron canales");
        esperarError(IllegalStateException.class, pool::adquirir,
                "El pool conserva su capacidad original");
        pool.liberar(finalUno);
        pool.liberar(finalDos);
        System.out.println("\nTodas las pruebas pasaron.");
    }

    private static void verificar(boolean condicion, String caso) {
        if (!condicion) {
            throw new AssertionError(caso);
        }
        System.out.println("Pasó: " + caso);
    }

    private static void esperarError(Class<? extends RuntimeException> tipo,
            Runnable accion, String caso) {
        try {
            accion.run();
        } catch (RuntimeException error) {
            if (!tipo.isInstance(error)) {
                throw new AssertionError("Excepción inesperada: " + caso, error);
            }
            System.out.println("Pasó: " + caso);
            return;
        }
        throw new AssertionError("Faltó la excepción esperada: " + caso);
    }
}
