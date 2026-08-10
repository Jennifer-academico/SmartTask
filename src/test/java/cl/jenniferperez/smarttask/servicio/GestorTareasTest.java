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
}