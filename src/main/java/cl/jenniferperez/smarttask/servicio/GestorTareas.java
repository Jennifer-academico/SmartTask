package cl.jenniferperez.smarttask.servicio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import cl.jenniferperez.smarttask.modelo.Tarea;
import cl.jenniferperez.smarttask.modelo.TareaNormal;
import cl.jenniferperez.smarttask.modelo.TareaUrgente;

/**
 * Administra las tareas de SmartTask.
 *
 * <p>Su unica responsabilidad es manejar la coleccion: agregar, buscar,
 * listar, completar y eliminar tareas. No imprime menus ni lee el teclado.</p>
 */
public class GestorTareas {

    private final List<Tarea> tareas;
    private int siguienteId;

    /**
     * Crea un gestor de tareas vacio, listo para agregar tareas.
     */
    public GestorTareas() {
        this.tareas = new ArrayList<>();
        this.siguienteId = 1;
    }

    /**
     * Crea y agrega una tarea normal.
     *
     * @param nombre descripcion de la tarea.
     * @param prioridad prioridad entre 1 y 3.
     * @param fechaVencimiento fecha hasta la cual la tarea es valida.
     * @param minutosAntes minutos de anticipacion para el aviso unico.
     * @return objeto creado y agregado.
     */
    public TareaNormal agregarTareaNormal(String nombre, int prioridad, LocalDate fechaVencimiento, int minutosAntes) {
        TareaNormal nuevaTarea = new TareaNormal(siguienteId, nombre, prioridad, fechaVencimiento, minutosAntes);
        tareas.add(nuevaTarea);
        siguienteId++;
        return nuevaTarea;
    }

    /**
     * Crea y agrega una tarea urgente.
     *
     * @param nombre descripcion de la tarea.
     * @param prioridad prioridad entre 1 y 3.
     * @param fechaVencimiento fecha hasta la cual la tarea es valida.
     * @param frecuencia cada cuantos minutos se repite el recordatorio.
     * @return objeto creado y agregado.
     */
    public TareaUrgente agregarTareaUrgente(String nombre, int prioridad, LocalDate fechaVencimiento, int frecuencia) {
        TareaUrgente nuevaTarea = new TareaUrgente(siguienteId, nombre, prioridad, fechaVencimiento, frecuencia);
        tareas.add(nuevaTarea);
        siguienteId++;
        return nuevaTarea;
    }

    /**
     * Obtiene una copia de todas las tareas.
     *
     * @return nueva lista con todas las tareas.
     */
    public List<Tarea> listarTareas() {
        return new ArrayList<>(tareas);
    }

    /**
     * Obtiene solamente las tareas activas (no completadas).
     *
     * @return lista de tareas activas.
     */
    public List<Tarea> listarActivas() {
        List<Tarea> activas = new ArrayList<>();
        for (Tarea tarea : tareas) {
            if (!tarea.estaCompletada()) {
                activas.add(tarea);
            }
        }
        return activas;
    }

    /**
     * Obtiene solamente las tareas completadas.
     *
     * @return lista de tareas completadas.
     */
    public List<Tarea> listarCompletadas() {
        List<Tarea> completadas = new ArrayList<>();
        for (Tarea tarea : tareas) {
            if (tarea.estaCompletada()) {
                completadas.add(tarea);
            }
        }
        return completadas;
    }

    /**
     * Busca una tarea por su identificador.
     *
     * @param id identificador buscado.
     * @return tarea encontrada o {@code null} si no existe.
     */
    public Tarea buscarPorId(int id) {
        for (Tarea tarea : tareas) {
            if (tarea.getId() == id) {
                return tarea;
            }
        }
        return null;
    }

    /**
     * Marca una tarea como completada.
     *
     * @param id identificador de la tarea.
     * @return {@code true} si se encontro y completo; {@code false} si no existe.
     */
    public boolean marcarComoCompletada(int id) {
        Tarea tareaEncontrada = buscarPorId(id);
        if (tareaEncontrada == null) {
            return false;
        }
        tareaEncontrada.completar();
        return true;
    }

    /**
     * Elimina una tarea usando su identificador.
     *
     * @param id identificador de la tarea.
     * @return {@code true} si fue eliminada; {@code false} si no existe.
     */
    public boolean eliminarTarea(int id) {
        for (int posicion = 0; posicion < tareas.size(); posicion++) {
            Tarea tareaActual = tareas.get(posicion);
            if (tareaActual.getId() == id) {
                tareas.remove(posicion);
                return true;
            }
        }
        return false;
    }

    /**
     * Informa cuantas tareas existen.
     *
     * @return cantidad de tareas guardadas.
     */
    public int cantidadTareas() {
        return tareas.size();
    }
}