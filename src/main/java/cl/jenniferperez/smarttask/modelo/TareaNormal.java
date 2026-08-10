package cl.jenniferperez.smarttask.modelo;

import java.time.LocalDate;

/**
 * Representa una tarea comun del dia a dia, sin urgencia especial.
 *
 * <p>El usuario define con cuantos minutos de anticipacion, respecto a la
 * fecha de vencimiento, se le debe avisar una sola vez.</p>
 */
public class TareaNormal extends Tarea {

    private int minutosAntes;

    /**
     * Construye una tarea normal.
     *
     * @param id identifica la tarea.
     * @param nombre descripcion de la tarea.
     * @param prioridad prioridad entre 1 y 3.
     * @param fechaVencimiento fecha hasta la cual la tarea es valida.
     * @param minutosAntes minutos de anticipacion para el aviso unico.
     */
    public TareaNormal(int id, String nombre, int prioridad, LocalDate fechaVencimiento, int minutosAntes) {
        super(id, nombre, prioridad, fechaVencimiento);
        validarMinutosAntes(minutosAntes);
        this.minutosAntes = minutosAntes;
    }

    /**
     * Obtiene los minutos de anticipacion del aviso.
     *
     * @return minutos antes del vencimiento para avisar.
     */
    public int getMinutosAntes() {
        return minutosAntes;
    }

    /**
     * Cambia los minutos de anticipacion despues de validarlos.
     *
     * @param minutosAntes nuevos minutos de anticipacion, debe ser mayor que 0.
     */
    public void setMinutosAntes(int minutosAntes) {
        validarMinutosAntes(minutosAntes);
        this.minutosAntes = minutosAntes;
    }

    private void validarMinutosAntes(int minutosAntes) {
        if (minutosAntes <= 0) {
            throw new IllegalArgumentException("Los minutos de aviso deben ser mayor que 0.");
        }
    }

    @Override
    public String obtenerTipo() {
        return "NORMAL";
    }

    @Override
    public String obtenerMensajeEspecial() {
        return "Aviso unico " + minutosAntes + " minutos antes";
    }
}