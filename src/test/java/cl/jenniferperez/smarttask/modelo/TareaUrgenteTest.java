package cl.jenniferperez.smarttask.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class TareaUrgenteTest {

    @Test
    void debeCrearTareaConDatosCorrectos() {
        LocalDate vencimiento = LocalDate.now().plusDays(2);
        TareaUrgente tarea = new TareaUrgente(1, "Entregar informe", 3, vencimiento, 15);

        assertEquals(1, tarea.getId());
        assertEquals("Entregar informe", tarea.getNombre());
        assertEquals(3, tarea.getPrioridad());
        assertEquals(vencimiento, tarea.getFechaVencimiento());
        assertEquals(15, tarea.getFrecuencia());
        assertFalse(tarea.estaCompletada());
    }

    @Test
    void debeCompletarLaTarea() {
        TareaUrgente tarea = new TareaUrgente(1, "Entregar informe", 3, LocalDate.now().plusDays(2), 15);

        tarea.completar();

        assertTrue(tarea.estaCompletada());
        assertEquals("COMPLETADA", tarea.obtenerEstadoCompletada());
    }

    @Test
    void debeCambiarFrecuenciaConSetter() {
        TareaUrgente tarea = new TareaUrgente(1, "Entregar informe", 3, LocalDate.now().plusDays(2), 15);

        tarea.setFrecuencia(30);

        assertEquals(30, tarea.getFrecuencia());
        assertTrue(tarea.obtenerMensajeEspecial().contains("30"));
    }

    @Test
    void debeRechazarIdInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new TareaUrgente(0, "Entregar informe", 3, LocalDate.now().plusDays(2), 15));
    }

    @Test
    void debeRechazarNombreVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new TareaUrgente(1, " ", 3, LocalDate.now().plusDays(2), 15));
    }

    @Test
    void debeRechazarPrioridadFueraDeRango() {
        assertThrows(IllegalArgumentException.class,
                () -> new TareaUrgente(1, "Entregar informe", 8, LocalDate.now().plusDays(2), 15));
    }

    @Test
    void debeRechazarFechaVencimientoEnElPasado() {
        assertThrows(IllegalArgumentException.class,
                () -> new TareaUrgente(1, "Entregar informe", 3, LocalDate.now().minusDays(1), 15));
    }

    @Test
    void debeRechazarFrecuenciaInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> new TareaUrgente(1, "Entregar informe", 3, LocalDate.now().plusDays(2), 0));
    }

    @Test
    void debeMostrarTipoYDetalleCorrectos() {
        TareaUrgente tarea = new TareaUrgente(1, "Entregar informe", 3, LocalDate.now().plusDays(2), 15);

        assertEquals("URGENTE", tarea.obtenerTipo());
        assertTrue(tarea.obtenerDetalle().contains("Tipo: URGENTE"));
        assertTrue(tarea.obtenerDetalle().contains("Recordatorio cada 15 minutos"));
    }

    @Test
    void demuestraPolimorfismoConReferenciaTarea() {
        Tarea tarea = new TareaUrgente(1, "Entregar informe", 3, LocalDate.now().plusDays(2), 15);

        assertEquals("URGENTE", tarea.obtenerTipo());
        assertTrue(tarea.obtenerDetalle().contains("Entregar informe"));
    }
}