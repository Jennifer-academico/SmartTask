package cl.jenniferperez.smarttask;

import cl.jenniferperez.smarttask.servicio.GestorTareas;
import cl.jenniferperez.smarttask.vista.MenuConsola;

/**
 * Punto de entrada de la aplicacion SmartTask.
 */
public class Main {

    private Main() {}

    /**
     * Crea el gestor de tareas y el menu, e inicia la aplicacion.
     *
     * @param args argumentos de consola; no se utilizan en este ejercicio.
     */
    public static void main(String[] args) {
        GestorTareas gestorTareas = new GestorTareas();

        MenuConsola menu = new MenuConsola(gestorTareas);
        menu.iniciar();
    }
}