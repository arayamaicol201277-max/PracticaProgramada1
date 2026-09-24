package aplicacion;

import dominio.CanalPersistencia;
import dominio.ProveedorCanales;
import java.util.Objects;

public final class ServicioConsultas {
    private final ProveedorCanales proveedor;

    public ServicioConsultas(ProveedorCanales proveedor) {
        this.proveedor = Objects.requireNonNull(proveedor, "El proveedor es obligatorio.");
    }

    public void ejecutar(String sql) {
        CanalPersistencia canal = proveedor.adquirir();
        try {
            canal.ejecutar(sql);
        } finally {
            proveedor.liberar(canal);
        }
    }
}
