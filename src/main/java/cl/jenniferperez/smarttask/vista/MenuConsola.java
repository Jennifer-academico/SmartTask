package cl.jenniferperez.smarttask.vista;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import cl.jenniferperez.smarttask.modelo.Tarea;
import cl.jenniferperez.smarttask.modelo.TareaNormal;
import cl.jenniferperez.smarttask.modelo.TareaUrgente;
import cl.jenniferperez.smarttask.servicio.GestorTareas;

/**
 * Muestra el menu por consola de SmartTask y conecta las acciones
 * del usuario con el gestor de tareas.
 *
 * <p>Su unica responsabilidad es leer teclado e imprimir resultados.
 * Toda la logica real vive en GestorTareas.</p>
 */
public class MenuConsola {

    private final GestorTareas gestorTareas;
    private final Scanner scanner;
    private final PrintStream salida;

    /**
     * Construye el menu conectandolo con el gestor que administra las tareas.
     *
     * @param gestorTareas gestor que contiene la logica de negocio.
     */
    public MenuConsola(GestorTareas gestorTareas) {
        this(gestorTareas, new Scanner(System.in), System.out);
    }

    /**
     * Construye el menu permitiendo personalizar la entrada y la salida.
     *
     * <p>Este constructor es util para realizar pruebas unitarias sin
     * depender del teclado real.</p>
     *
     * @param gestorTareas gestor que contiene la logica de negocio.
     * @param scanner lector de entrada.
     * @param salida destino de los mensajes.
     */
    public MenuConsola(GestorTareas gestorTareas, Scanner scanner, PrintStream salida) {
        this.gestorTareas = gestorTareas;
        this.scanner = scanner;
        this.salida = salida;
    }

    /**
     * Inicia el ciclo del menu hasta que el usuario elija salir.
     */
    public void iniciar() {
        int opcion;

        salida.println("=== Bienvenido a SmartTask ===");

        do {
            mostrarMenu();
            opcion = leerEntero("Elige una opcion: ");

            switch (opcion) {
                case 1:
                    agregarTareaNormal();
                    break;
                case 2:
                    agregarTareaUrgente();
                    break;
                case 3:
                    listarTareas(gestorTareas.listarTareas(), "Todas las tareas");
                    break;
                case 4:
                    listarTareas(gestorTareas.listarActivas(), "Tareas activas");
                    break;
                case 5:
                    listarTareas(gestorTareas.listarCompletadas(), "Tareas completadas");
                    break;
                case 6:
                    completarTarea();
                    break;
                case 7:
                    eliminarTarea();
                    break;
                case 8:
                    editarTarea();
                    break;
                case 0:
                    salida.println("Hasta luego.");
                    break;
                default:
                    salida.println("Opcion invalida, intente nuevamente.");
            }
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        salida.println();
        salida.println("--------------------- MENU ---------------------");
        salida.println("1.- Agregar tarea normal");
        salida.println("2.- Agregar tarea urgente");
        salida.println("3.- Listar todas las tareas");
        salida.println("4.- Listar tareas activas");
        salida.println("5.- Listar tareas completadas");
        salida.println("6.- Marcar tarea como completada");
        salida.println("7.- Eliminar tarea");
        salida.println("8.- Editar tarea");
        salida.println("0.- Salir");
        salida.println("--------------------------------------------------");
    }

    private void agregarTareaNormal() {
        String nombre = leerTextoNoVacio("Nombre de la tarea: ");
        int prioridad = leerEntero("Prioridad (1 a 3): ");
        LocalDate fechaVencimiento = leerFecha("Fecha de vencimiento (dd/mm/aaaa): ");
        int minutosAntes = leerEntero("Minutos antes del vencimiento para avisar: ");

        try {
            gestorTareas.agregarTareaNormal(nombre, prioridad, fechaVencimiento, minutosAntes);
            salida.println("Tarea normal agregada.");
        } catch (IllegalArgumentException error) {
            salida.println("Error: " + error.getMessage());
        }
    }

    private void agregarTareaUrgente() {
        String nombre = leerTextoNoVacio("Nombre de la tarea: ");
        int prioridad = leerEntero("Prioridad (1 a 3): ");
        LocalDate fechaVencimiento = leerFecha("Fecha de vencimiento (dd/mm/aaaa): ");
        int frecuencia = leerEntero("Cada cuantos minutos se repite el recordatorio: ");

        try {
            gestorTareas.agregarTareaUrgente(nombre, prioridad, fechaVencimiento, frecuencia);
            salida.println("Tarea urgente agregada.");
        } catch (IllegalArgumentException error) {
            salida.println("Error: " + error.getMessage());
        }
    }

    private void listarTareas(List<Tarea> tareas, String titulo) {
        salida.println();
        salida.println("--- " + titulo + " ---");

        if (tareas.isEmpty()) {
            salida.println("No hay tareas para mostrar.");
            return;
        }

        for (Tarea tarea : tareas) {
            salida.println(tarea.obtenerDetalle());
        }
    }

    private void completarTarea() {
        int id = leerEntero("ID de la tarea a completar: ");
        boolean resultado = gestorTareas.marcarComoCompletada(id);

        if (resultado) {
            salida.println("Tarea marcada como completada.");
        } else {
            salida.println("No se encontro una tarea con ese ID.");
        }
    }

    private void eliminarTarea() {
        int id = leerEntero("ID de la tarea a eliminar: ");
        boolean resultado = gestorTareas.eliminarTarea(id);

        if (resultado) {
            salida.println("Tarea eliminada.");
        } else {
            salida.println("No se encontro una tarea con ese ID.");
        }
    }

    private void editarTarea() {
        int id = leerEntero("ID de la tarea a editar: ");
        Tarea tarea = gestorTareas.buscarPorId(id);

        if (tarea == null) {
            salida.println("No se encontro una tarea con ese ID.");
            return;
        }

        int opcion;
        do {
            mostrarMenuEdicion(tarea);
            opcion = leerEntero("Elige una opcion: ");
            ejecutarEdicion(opcion, tarea);
        } while (opcion != 0);
    }

    private void mostrarMenuEdicion(Tarea tarea) {
        salida.println();
        salida.println("--- EDITAR TAREA (ID " + tarea.getId() + ") ---");
        salida.println(tarea.obtenerDetalle());
        salida.println("1.- Cambiar nombre");
        salida.println("2.- Cambiar prioridad");
        salida.println("3.- Cambiar fecha de vencimiento");

        if (tarea instanceof TareaNormal) {
            salida.println("4.- Cambiar minutos antes del aviso");
        } else if (tarea instanceof TareaUrgente) {
            salida.println("4.- Cambiar frecuencia del recordatorio");
        }

        salida.println("0.- Volver al menu principal");
    }

    private void ejecutarEdicion(int opcion, Tarea tarea) {
        try {
            switch (opcion) {
                case 1:
                    String nuevoNombre = leerTextoNoVacio("Nuevo nombre: ");
                    tarea.setNombre(nuevoNombre);
                    salida.println("Nombre actualizado.");
                    break;
                case 2:
                    int nuevaPrioridad = leerEntero("Nueva prioridad (1 a 3): ");
                    tarea.setPrioridad(nuevaPrioridad);
                    salida.println("Prioridad actualizada.");
                    break;
                case 3:
                    LocalDate nuevaFecha = leerFecha("Nueva fecha de vencimiento (dd/mm/aaaa): ");
                    tarea.setFechaVencimiento(nuevaFecha);
                    salida.println("Fecha de vencimiento actualizada.");
                    break;
                case 4:
                    editarCampoEspecifico(tarea);
                    break;
                case 0:
                    break;
                default:
                    salida.println("Opcion invalida.");
            }
        } catch (IllegalArgumentException error) {
            salida.println("Error: " + error.getMessage());
        }
    }

    private void editarCampoEspecifico(Tarea tarea) {
        if (tarea instanceof TareaNormal tareaNormal) {
            int nuevosMinutos = leerEntero("Nuevos minutos antes del aviso: ");
            tareaNormal.setMinutosAntes(nuevosMinutos);
            salida.println("Minutos de aviso actualizados.");
        } else if (tarea instanceof TareaUrgente tareaUrgente) {
            int nuevaFrecuencia = leerEntero("Nueva frecuencia (minutos entre recordatorios): ");
            tareaUrgente.setFrecuencia(nuevaFrecuencia);
            salida.println("Frecuencia actualizada.");
        } else {
            salida.println("Opcion invalida.");
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            salida.print(mensaje);
            String entrada = scanner.nextLine();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException error) {
                salida.println("Debe ingresar un numero entero.");
            }
        }
    }

    private String leerTextoNoVacio(String mensaje) {
        String texto;

        do {
            salida.print(mensaje);
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                salida.println("El texto no puede estar vacio.");
            }
        } while (texto.isEmpty());

        return texto;
    }

    private LocalDate leerFecha(String mensaje) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (true) {
            salida.print(mensaje);
            String entrada = scanner.nextLine().trim();

            try {
                return LocalDate.parse(entrada, formato);
            } catch (DateTimeParseException error) {
                salida.println("Formato invalido. Usa dd/mm/aaaa, por ejemplo 25/12/2026.");
            }
        }
    }
}