package cl.jenniferperez.smarttask.servicio;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.jenniferperez.smarttask.modelo.Tarea;

class GestorTareasTest {

    private GestorTareas gestor;
    private LocalDate vencimiento;

    @BeforeEach
    void prepararGestor() {
        gestor = new GestorTareas();
        vencimiento = LocalDate.now().plusDays(5);
    }

    @Test
    void debeAgregarTareasConIdsConsecutivos() {
        Tarea primera = gestor.agregarTareaNormal("Estudiar", 2, vencimiento, 30);
        Tarea segunda = gestor.agregarTareaUrgente("Entregar informe", 3, vencimiento, 15);

        assertEquals(1, primera.getId());
        assertEquals(2, segunda.getId());
        assertEquals(2, gestor.cantidadTareas());
    }

    @Test
    void debeBuscarPorId() {
        gestor.agregarTareaNormal("Estudiar", 2, vencimiento, 30);

        Tarea encontrada = gestor.buscarPorId(1);

        assertNotNull(encontrada);
        assertEquals("Estudiar", encontrada.getNombre());
        assertNull(gestor.buscarPorId(99));
    }

    @Test
    void debeListarSoloActivas() {
        gestor.agregarTareaNormal("Estudiar", 2, vencimiento, 30);
        gestor.agregarTareaUrgente("Entregar informe", 3, vencimiento, 15);
        gestor.marcarComoCompletada(1);

        List<Tarea> activas = gestor.listarActivas();

        assertEquals(1, activas.size());
        assertEquals(2, activas.get(0).getId());
    }

    @Test
    void debeListarSoloCompletadas() {
        gestor.agregarTareaNormal("Estudiar", 2, vencimiento, 30);
        gestor.agregarTareaUrgente("Entregar informe", 3, vencimiento, 15);
        gestor.marcarComoCompletada(2);

        List<Tarea> completadas = gestor.listarCompletadas();

        assertEquals(1, completadas.size());
        assertEquals(2, completadas.get(0).getId());
    }

    @Test
    void debeInformarResultadoAlCompletar() {
        gestor.agregarTareaNormal("Estudiar", 2, vencimiento, 30);

        assertTrue(gestor.marcarComoCompletada(1));
        assertTrue(gestor.buscarPorId(1).estaCompletada());
        assertFalse(gestor.marcarComoCompletada(99));
    }

    @Test
    void debeInformarResultadoAlEliminar() {
        gestor.agregarTareaNormal("Estudiar", 2, vencimiento, 30);

        assertTrue(gestor.eliminarTarea(1));
        assertEquals(0, gestor.cantidadTareas());
        assertFalse(gestor.eliminarTarea(99));
    }

    @Test
    void listarTareasDebeProtegerLaListaInterna() {
        gestor.agregarTareaNormal("Estudiar", 2, vencimiento, 30);

        List<Tarea> copia = gestor.listarTareas();
        copia.clear();

        assertEquals(1, gestor.cantidadTareas());
    }

    @Test
    void debeEditarNombre() {
        gestor.agregarTareaNormal("Estudiar", 2, vencimiento, 30);

        assertTrue(gestor.editarNombre(1, "Repasar materia"));
        assertEquals("Repasar materia", gestor.buscarPorId(1).getNombre());
        assertFalse(gestor.editarNombre(99, "No existe"));
    }

    @Test
    void debeEditarPrioridad() {
        gestor.agregarTareaNormal("Estudiar", 2, vencimiento, 30);

        assertTrue(gestor.editarPrioridad(1, 3));
        assertEquals(3, gestor.buscarPorId(1).getPrioridad());
        assertFalse(gestor.editarPrioridad(99, 3));
    }

    @Test
    void debeEditarFechaVencimiento() {
        gestor.agregarTareaNormal("Estudiar", 2, vencimiento, 30);
        LocalDate nuevaFecha = LocalDate.now().plusDays(10);

        assertTrue(gestor.editarFechaVencimiento(1, nuevaFecha));
        assertEquals(nuevaFecha, gestor.buscarPorId(1).getFechaVencimiento());
        assertFalse(gestor.editarFechaVencimiento(99, nuevaFecha));
    }

    @Test
    void debeEditarMinutosAntesSoloEnTareaNormal() {
        gestor.agregarTareaNormal("Estudiar", 2, vencimiento, 30);
        gestor.agregarTareaUrgente("Entregar informe", 3, vencimiento, 15);

        assertTrue(gestor.editarMinutosAntes(1, 60));
        assertFalse(gestor.editarMinutosAntes(2, 60));
        assertFalse(gestor.editarMinutosAntes(99, 60));
    }

    @Test
    void debeEditarFrecuenciaSoloEnTareaUrgente() {
        gestor.agregarTareaNormal("Estudiar", 2, vencimiento, 30);
        gestor.agregarTareaUrgente("Entregar informe", 3, vencimiento, 15);

        assertTrue(gestor.editarFrecuencia(2, 45));
        assertFalse(gestor.editarFrecuencia(1, 45));
        assertFalse(gestor.editarFrecuencia(99, 45));
    }
}