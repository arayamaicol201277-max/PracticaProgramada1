package infraestructura;

import dominio.CanalPersistencia;

public final class CanalSimulado implements CanalPersistencia {
    private final int id;

    public CanalSimulado(int id) {
        this.id = id;
        System.out.println("Creado canal de persistencia simulado #" + id);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void ejecutar(String sql) {
        if (sql == null || sql.isBlank()) {
            throw new IllegalArgumentException("La consulta no puede estar vacía.");
        }
        System.out.println("Canal de persistencia #" + id + " ejecuta (simulación): " + sql);
    }
}
