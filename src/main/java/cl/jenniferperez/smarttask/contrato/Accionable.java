package cl.jenniferperez.smarttask.contrato;

/**
 * Define el contrato comun para cualquier tarea que pueda completarse
 * y describirse a si misma.
 */
public interface Accionable {

    /**
     * Marca la tarea como completada.
     */
    void completar();

    /**
     * Indica si la tarea ya fue completada.
     *
     * @return {@code true} si esta completada; {@code false} si sigue activa.
     */
    boolean estaCompletada();

    /**
     * Construye un texto con la informacion principal de la tarea.
     *
     * @return detalle listo para mostrar en consola.
     */
    String obtenerDetalle();
}