package equipo04.contexto02;

import java.util.EnumMap;
import java.util.Map;

enum CanalContacto { // canal contacto
    CORREO, TEXTO, PUSH
}

interface CanalEnvio { // Producto (entrega el msj)
    void enviar(String mensaje, String destino);
}

class EnvioCorreo implements CanalEnvio {
    public void enviar(String mensaje, String destino) {
        System.out.println("Correo: " + destino + " comunica: " + mensaje);
    }
}

class EnvioTexto implements CanalEnvio {
    public void enviar(String mensaje, String destino) {
        System.out.println("SMS: " + destino + " comunica: " + mensaje);
    }
}

class EnvioPush implements CanalEnvio {
    public void enviar(String mensaje, String destino) {
        System.out.println("Push: " + destino + " comunica: " + mensaje);
    }
}

abstract class DespachadorAlerta { // Creator sabe a cual canal le toca armar

    abstract CanalEnvio construirCanal();

    void avisar(String mensaje, String destino) {
        if (mensaje == null || mensaje.isEmpty()) {
            throw new IllegalArgumentException("falta el mensaje de la alerta");
        }
        if (destino == null || destino.isEmpty()) {
            throw new IllegalArgumentException("falta el destinatario");
        }
        CanalEnvio canal = construirCanal();
        canal.enviar(mensaje, destino);
    }
}

class DespachadorCorreo extends DespachadorAlerta {
    CanalEnvio construirCanal() { return new EnvioCorreo(); }
}

class DespachadorTexto extends DespachadorAlerta {
    CanalEnvio construirCanal() { return new EnvioTexto(); }
}

class DespachadorPush extends DespachadorAlerta {
    CanalEnvio construirCanal() { return new EnvioPush(); }
}

class CentralAlertas { // client pregunta por preferencias de user

    private final Map<CanalContacto, DespachadorAlerta> despachadores = new EnumMap<>(CanalContacto.class);

    CentralAlertas() {
        despachadores.put(CanalContacto.CORREO, new DespachadorCorreo());
        despachadores.put(CanalContacto.TEXTO, new DespachadorTexto());
        despachadores.put(CanalContacto.PUSH, new DespachadorPush());
    }

    void lanzar(String mensaje, String destino, CanalContacto preferencia) {
        DespachadorAlerta d = despachadores.get(preferencia);
        if (d == null) {
            throw new IllegalStateException("no hay despachador para " + preferencia);
        }
        d.avisar(mensaje, destino);
    }
}

public class Main {
    public static void main(String[] args) {
        CentralAlertas central = new CentralAlertas();

        central.lanzar("Caida del servidor principal", "hola@a.com", CanalContacto.CORREO);
        central.lanzar("Acceso no autorizado detectado", "+5066667777", CanalContacto.TEXTO);
        central.lanzar("Backup finalizo con errores", "dispositivo_123", CanalContacto.PUSH);

        // un par de casos que deberian fallar, por si aca
        probarError(() -> central.lanzar("", "eh@ok.com", CanalContacto.CORREO),
                "mensaje vacio");
        probarError(() -> central.lanzar("Alerta", "", CanalContacto.TEXTO),
                "destino vacio");
        probarError(() -> central.lanzar("Alerta", "eh@ok.com", null),
                "preferencia nula");
    }

    private static void probarError(Runnable accion, String caso) {
        try {
            accion.run();
            System.out.println("ERROR: se esperaba que fallara " + caso);
        } catch (RuntimeException e) {
            System.out.println("Ok, fallo como se esperaba (" + caso + "): " + e.getMessage());
        }
    }
}
