package cl.jenniferperez.smarttask.modelo;

import java.time.LocalDate;

/**
 * Representa una tarea con alta urgencia, que requiere recordatorios
 * repetidos hasta que se complete.
 */
public class TareaUrgente extends Tarea {

    private int frecuencia;

    /**
     * Construye una tarea urgente.
     *
     * @param id identificador de la tarea.
     * @param nombre descripcion de la tarea.
     * @param prioridad prioridad entre 1 y 3.
     * @param fechaVencimiento fecha hasta la cual la tarea es valida.
     * @param frecuencia cada cuantos minutos se repite el recordatorio.
     */
    public TareaUrgente(int id, String nombre, int prioridad, LocalDate fechaVencimiento, int frecuencia) {
        super(id, nombre, prioridad, fechaVencimiento);
        validarFrecuencia(frecuencia);
        this.frecuencia = frecuencia;
    }

    /**
     * Obtiene la frecuencia de recordatorio en minutos.
     *
     * @return minutos entre cada recordatorio.
     */
    public int getFrecuencia() {
        return frecuencia;
    }

    /**
     * Cambia la frecuencia de recordatorio despues de validarla.
     *
     * @param frecuencia nuevos minutos entre recordatorios, debe ser mayor que 0.
     */
    public void setFrecuencia(int frecuencia) {
        validarFrecuencia(frecuencia);
        this.frecuencia = frecuencia;
    }

    private void validarFrecuencia(int frecuencia) {
        if (frecuencia <= 0) {
            throw new IllegalArgumentException("La frecuencia debe ser mayor que 0.");
        }
    }

    @Override
    public String obtenerTipo() {
        return "URGENTE";
    }

    @Override
    public String obtenerMensajeEspecial() {
        return "Recordatorio cada " + frecuencia + " minutos";
    }
}