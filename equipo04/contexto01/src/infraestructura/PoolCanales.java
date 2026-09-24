package infraestructura;

import dominio.CanalPersistencia;
import dominio.ProveedorCanales;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public final class PoolCanales implements ProveedorCanales {
    private final Queue<CanalPersistencia> disponibles = new ArrayDeque<>();
    private final Set<CanalPersistencia> prestados = new HashSet<>();

    public PoolCanales(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        for (int id = 1; id <= capacidad; id++) {
            disponibles.add(new CanalSimulado(id));
        }
    }

    @Override
    public CanalPersistencia adquirir() {
        if (disponibles.isEmpty()) {
            throw new IllegalStateException("No hay canales de persistencia disponibles.");
        }
        CanalPersistencia canal = disponibles.remove();
        prestados.add(canal);
        return canal;
    }

    @Override
    public void liberar(CanalPersistencia canal) {
        if (!prestados.remove(canal)) {
            throw new IllegalArgumentException("El canal de persistencia no está prestado por este pool.");
        }
        disponibles.add(canal);
    }
}
