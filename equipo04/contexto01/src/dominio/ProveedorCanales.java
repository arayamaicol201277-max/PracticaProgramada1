package dominio;

public interface ProveedorCanales {
    CanalPersistencia adquirir();

    void liberar(CanalPersistencia canal);
}
