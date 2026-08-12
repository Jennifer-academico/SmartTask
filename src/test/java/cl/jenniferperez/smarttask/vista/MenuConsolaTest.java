package cl.jenniferperez.smarttask.vista;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import cl.jenniferperez.smarttask.servicio.GestorTareas;

class MenuConsolaTest {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Test
    void debeAgregarYListarTareaNormal() {
        GestorTareas gestor = new GestorTareas();
        String fecha = LocalDate.now().plusDays(5).format(FORMATO);
        String entrada = String.join("\n",
                "1", "Estudiar", "2", fecha, "30",
                "3",
                "0") + "\n";
        ByteArrayOutputStream bytesSalida = new ByteArrayOutputStream();

        MenuConsola menu = new MenuConsola(
                gestor,
                new Scanner(entrada),
                new PrintStream(bytesSalida, true, StandardCharsets.UTF_8));

        menu.iniciar();

        String texto = bytesSalida.toString(StandardCharsets.UTF_8);
        assertEquals(1, gestor.cantidadTareas());
        assertTrue(texto.contains("Tarea normal agregada"));
        assertTrue(texto.contains("Estudiar"));
    }

    @Test
    void debeAgregarTareaUrgenteYCompletarla() {
        GestorTareas gestor = new GestorTareas();
        String fecha = LocalDate.now().plusDays(2).format(FORMATO);
        String entrada = String.join("\n",
                "2", "Entregar informe", "3", fecha, "15",
                "6", "1",
                "5",
                "0") + "\n";
        ByteArrayOutputStream bytesSalida = new ByteArrayOutputStream();

        MenuConsola menu = new MenuConsola(
                gestor,
                new Scanner(entrada),
                new PrintStream(bytesSalida, true, StandardCharsets.UTF_8));

        menu.iniciar();

        String texto = bytesSalida.toString(StandardCharsets.UTF_8);
        assertTrue(gestor.buscarPorId(1).estaCompletada());
        assertTrue(texto.contains("Tarea urgente agregada"));
        assertTrue(texto.contains("Tarea marcada como completada"));
    }

    @Test
    void debeEliminarUnaTarea() {
        GestorTareas gestor = new GestorTareas();
        String fecha = LocalDate.now().plusDays(5).format(FORMATO);
        String entrada = String.join("\n",
                "1", "Estudiar", "2", fecha, "30",
                "7", "1",
                "0") + "\n";
        ByteArrayOutputStream bytesSalida = new ByteArrayOutputStream();

        MenuConsola menu = new MenuConsola(
                gestor,
                new Scanner(entrada),
                new PrintStream(bytesSalida, true, StandardCharsets.UTF_8));

        menu.iniciar();

        String texto = bytesSalida.toString(StandardCharsets.UTF_8);
        assertEquals(0, gestor.cantidadTareas());
        assertTrue(texto.contains("Tarea eliminada"));
    }

    @Test
    void debeRecuperarseDeOpcionInvalidaYEntradaNoNumerica() {
        GestorTareas gestor = new GestorTareas();
        String entrada = "texto\n99\n0\n";
        ByteArrayOutputStream bytesSalida = new ByteArrayOutputStream();

        MenuConsola menu = new MenuConsola(
                gestor,
                new Scanner(entrada),
                new PrintStream(bytesSalida, true, StandardCharsets.UTF_8));

        menu.iniciar();

        String texto = bytesSalida.toString(StandardCharsets.UTF_8);
        assertTrue(texto.contains("Debe ingresar un numero entero"));
        assertTrue(texto.contains("Opcion invalida"));
    }

    @Test
    void debeInformarCuandoNoExisteElIdBuscado() {
        GestorTareas gestor = new GestorTareas();
        String entrada = String.join("\n", "6", "99", "0") + "\n";
        ByteArrayOutputStream bytesSalida = new ByteArrayOutputStream();

        MenuConsola menu = new MenuConsola(
                gestor,
                new Scanner(entrada),
                new PrintStream(bytesSalida, true, StandardCharsets.UTF_8));

        menu.iniciar();

        String texto = bytesSalida.toString(StandardCharsets.UTF_8);
        assertTrue(texto.contains("No se encontro una tarea con ese ID"));
    }

    @Test
    void debeRecuperarseDeFechaConFormatoInvalido() {
        GestorTareas gestor = new GestorTareas();
        String fecha = LocalDate.now().plusDays(5).format(FORMATO);
        String entrada = String.join("\n",
                "1", "Estudiar", "2", "31-13-2026", fecha, "30",
                "0") + "\n";
        ByteArrayOutputStream bytesSalida = new ByteArrayOutputStream();

        MenuConsola menu = new MenuConsola(
                gestor,
                new Scanner(entrada),
                new PrintStream(bytesSalida, true, StandardCharsets.UTF_8));

        menu.iniciar();

        String texto = bytesSalida.toString(StandardCharsets.UTF_8);
        assertTrue(texto.contains("Formato invalido"));
        assertEquals(1, gestor.cantidadTareas());
    }

    @Test
    void debeEditarNombreDeUnaTarea() {
        GestorTareas gestor = new GestorTareas();
        String fecha = LocalDate.now().plusDays(5).format(FORMATO);
        String entrada = String.join("\n",
                "1", "Estudiar", "2", fecha, "30",
                "8", "1", "1", "Repasar materia", "0",
                "0") + "\n";
        ByteArrayOutputStream bytesSalida = new ByteArrayOutputStream();

        MenuConsola menu = new MenuConsola(
                gestor,
                new Scanner(entrada),
                new PrintStream(bytesSalida, true, StandardCharsets.UTF_8));

        menu.iniciar();

        String texto = bytesSalida.toString(StandardCharsets.UTF_8);
        assertEquals("Repasar materia", gestor.buscarPorId(1).getNombre());
        assertTrue(texto.contains("Nombre actualizado"));
    }

    @Test
    void debeListarTareasActivasYCompletadas() {
        GestorTareas gestor = new GestorTareas();
        String fecha = LocalDate.now().plusDays(5).format(FORMATO);
        String entrada = String.join("\n",
                "1", "Estudiar", "2", fecha, "30",
                "6", "1",
                "4",
                "5",
                "0") + "\n";
        ByteArrayOutputStream bytesSalida = new ByteArrayOutputStream();

        MenuConsola menu = new MenuConsola(
                gestor,
                new Scanner(entrada),
                new PrintStream(bytesSalida, true, StandardCharsets.UTF_8));

        menu.iniciar();

        String texto = bytesSalida.toString(StandardCharsets.UTF_8);
        assertTrue(texto.contains("Tareas activas"));
        assertTrue(texto.contains("Tareas completadas"));
    }

    @Test
    void debeEditarTareaUrgenteCompleta() {
        GestorTareas gestor = new GestorTareas();
        String fecha = LocalDate.now().plusDays(5).format(FORMATO);
        String nuevaFecha = LocalDate.now().plusDays(10).format(FORMATO);
        String entrada = String.join("\n",
                "2", "Entregar informe", "3", fecha, "15",
                "8", "1",
                "2", "1",
                "3", nuevaFecha,
                "4", "45",
                "0",
                "0") + "\n";
        ByteArrayOutputStream bytesSalida = new ByteArrayOutputStream();

        MenuConsola menu = new MenuConsola(
                gestor,
                new Scanner(entrada),
                new PrintStream(bytesSalida, true, StandardCharsets.UTF_8));

        menu.iniciar();

        String texto = bytesSalida.toString(StandardCharsets.UTF_8);
        assertEquals(1, gestor.buscarPorId(1).getPrioridad());
        assertTrue(texto.contains("Prioridad actualizada"));
        assertTrue(texto.contains("Fecha de vencimiento actualizada"));
        assertTrue(texto.contains("Frecuencia actualizada"));
    }

    @Test
    void debeRechazarOpcionInvalidaAlEditar() {
        GestorTareas gestor = new GestorTareas();
        String fecha = LocalDate.now().plusDays(5).format(FORMATO);
        String entrada = String.join("\n",
                "1", "Estudiar", "2", fecha, "30",
                "8", "1",
                "9",
                "0",
                "0") + "\n";
        ByteArrayOutputStream bytesSalida = new ByteArrayOutputStream();

        MenuConsola menu = new MenuConsola(
                gestor,
                new Scanner(entrada),
                new PrintStream(bytesSalida, true, StandardCharsets.UTF_8));

        menu.iniciar();

        String texto = bytesSalida.toString(StandardCharsets.UTF_8);
        assertTrue(texto.contains("Opcion invalida"));
    }
}