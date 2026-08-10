package cl.jenniferperez.smarttask.modelo;

import java.time.LocalDate;

import cl.jenniferperez.smarttask.contrato.Accionable;

/**
 * Representa una tarea generica dentro de SmartTask.
 *
 * <p>Esta clase concentra los atributos y validaciones comunes a toda tarea.
 * Las clases hijas definen su tipo y su mensaje especial.</p>
 */
public abstract class Tarea implements Accionable {
    private final int id;
    private String nombre;
    private int prioridad;
    private boolean completada;
    private final LocalDate fechaCreacion;
    private LocalDate fechaVencimiento;

    /**
     * Construye una nueva tarea.
     *
     * @param id identificador positivo de la tarea.
     * @param nombre descripcion de la tarea.
     * @param prioridad numero entre 1 y 3.
     * @param fechaVencimiento fecha hasta la cual la tarea es valida.
     * @throws IllegalArgumentException si algun dato no es valido.
     */
    public Tarea(int id, String nombre, int prioridad, LocalDate fechaVencimiento) {
        validarId(id);
        validarNombre(nombre);
        validarPrioridad(prioridad);
        validarFechaVencimiento(fechaVencimiento);
        this.id = id;
        this.nombre = nombre.trim();
        this.prioridad = prioridad;
        this.completada = false;
        this.fechaCreacion = LocalDate.now();
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * Obtiene el identificador de la tarea.
     *
     * @return identificador de la tarea.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre de la tarea.
     *
     * @return nombre de la tarea.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia el nombre de la tarea despues de validarlo.
     *
     * @param nombre nuevo nombre, no puede estar vacio.
     */
    public void setNombre(String nombre) {
        validarNombre(nombre);
        this.nombre = nombre.trim();
    }

    /**
     * Obtiene la prioridad de la tarea.
     *
     * @return prioridad entre 1 y 3.
     */
    public int getPrioridad() {
        return prioridad;
    }

    /**
     * Cambia la prioridad de la tarea despues de validarla.
     *
     * @param prioridad nueva prioridad entre 1 y 3.
     */
    public void setPrioridad(int prioridad) {
        validarPrioridad(prioridad);
        this.prioridad = prioridad;
    }

    /**
     * Obtiene la fecha en que se creo la tarea.
     *
     * @return fecha de creacion.
     */
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Obtiene la fecha de vencimiento de la tarea.
     *
     * @return fecha de vencimiento.
     */
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * Cambia la fecha de vencimiento despues de validarla.
     *
     * @param fechaVencimiento nueva fecha de vencimiento.
     */
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        validarFechaVencimiento(fechaVencimiento);
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public void completar() {
        this.completada = true;
    }

    @Override
    public boolean estaCompletada() {
        return completada;
    }

    /**
     * obtiene tipo concreto de tarea.
     * @return tipo de tarea.
     */
    public abstract String obtenerTipo();

    /**
     * Entrega un mensaje especial segun el tipo de tarea.
     * @return mensaje particular de la tarea.
     */
    public abstract String obtenerMensajeEspecial();

    @Override
    public String obtenerDetalle() {
        return "[" + obtenerEstadoCompletada() + "]" +
                "ID: " + id +
                "    |Tipo: " + obtenerTipo() +
                "    |Tarea: " + nombre +
                "    |Prioridad: " + prioridad +
                "    |Vence: " + fechaVencimiento +
                "    | " + obtenerMensajeEspecial();
    }

    /**
     * Obtiene el estado de la tarea en palabras.
     *
     * @return "COMPLETADA" o "ACTIVA" segun corresponda.
     */
    public String obtenerEstadoCompletada() {
        if (completada) {
            return "COMPLETADA";
        }
        return "ACTIVA";
    }

    private void validarId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("id debe ser mayor que 0");
        }
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre no puede estar vacío");
        }
    }

    private void validarPrioridad(int prioridad) {
        if (prioridad < 1 || prioridad > 3) {
            throw new IllegalArgumentException("Prioridad debe estar entre 1 y 3");
        }
    }

    private void validarFechaVencimiento(LocalDate fechaVencimiento) {
        if (fechaVencimiento == null) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser nula");
        }
        if (fechaVencimiento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a hoy");
        }
    }
}