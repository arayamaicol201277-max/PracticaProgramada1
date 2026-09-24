package dominio;

public interface CanalPersistencia {
    int getId();

    void ejecutar(String sql);
}
