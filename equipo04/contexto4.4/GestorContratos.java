package equipo04.contexto04;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GestorContratos {
    private final Map<String, IContratoPrototype> prototipos = new HashMap<>();

    public void registrarPrototipo(String clave, IContratoPrototype prototipo) {
        Objects.requireNonNull(clave, "La clave no puede ser nula");
        Objects.requireNonNull(prototipo, "El prototipo no puede ser nulo");
        prototipos.put(clave, prototipo);
    }

    public IContratoPrototype obtenerClon(String clave) {
        IContratoPrototype prototipo = prototipos.get(clave);
        if (prototipo == null) {
            throw new IllegalArgumentException("No existe un prototipo bajo la clave: " + clave);
        }
        return prototipo.clonar();
    }

   
    public <T extends IContratoPrototype> T obtenerClon(String clave, Class<T> tipo) {
        return tipo.cast(obtenerClon(clave));
    }
}
