package cl.jenniferperez.smarttask.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class TareaNormalTest {

    @Test
    void debeCrearTareaConDatosCorrectos() {
        LocalDate vencimiento = LocalDate.now().plusDays(5);
        TareaNormal tarea = new TareaNormal(1, "Estudiar", 2, vencimiento, 30);

        assertEquals(1, tarea.getId());
        assertEquals("Estudiar", tarea.getNombre());
        assertEquals(2, tarea.getPrioridad());
        assertEquals(vencimiento, tarea.getFechaVencimiento());
        assertEquals(30, tarea.getMinutosAntes());
        assertFalse(tarea.estaCompletada());
        assertEquals(LocalDate.now(), tarea.getFechaCreacion());
    }

    @Test
    void debeCompletarLaTarea() {
        TareaNormal tarea = new TareaNormal(1, "Estudiar", 2, LocalDate.now().plusDays(5), 30);

        tarea.completar();

        assertTrue(tarea.estaCompletada());
        assertEquals("COMPLETADA", tarea.obtenerEstadoCompletada());
    }

    @Test
    void debeCambiarNombreYPrioridadConSetters() {
        TareaNormal tarea = new TareaNormal(1, "Estudiar", 2, LocalDate.now().plusDays(5), 30);

        tarea.setNombre("Repasar materia");
        tarea.setPrioridad(3);

        assertEquals("Repasar materia", tarea.getNombre());
        assertEquals(3, tarea.getPrioridad());
    }

    @Test
    void debeCambiarMinutosAntesConSetter() {
        TareaNormal tarea = new TareaNormal(1, "Estudiar", 2, LocalDate.now().plusDays(5), 30);

        tarea.setMinutosAntes(60);

        assertEquals(60, tarea.getMinutosAntes());
    }

    @Test
    void debeRechazarIdInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new TareaNormal(0, "Estudiar", 2, LocalDate.now().plusDays(5), 30));
    }

    @Test
    void debeRechazarNombreVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new TareaNormal(1, " ", 2, LocalDate.now().plusDays(5), 30));
    }

    @Test
    void debeRechazarPrioridadFueraDeRango() {
        assertThrows(IllegalArgumentException.class,
                () -> new TareaNormal(1, "Estudiar", 9, LocalDate.now().plusDays(5), 30));
    }

    @Test
    void debeRechazarFechaVencimientoEnElPasado() {
        assertThrows(IllegalArgumentException.class,
                () -> new TareaNormal(1, "Estudiar", 2, LocalDate.now().minusDays(1), 30));
    }

    @Test
    void debeRechazarMinutosAntesInvalidos() {
        assertThrows(IllegalArgumentException.class,
                () -> new TareaNormal(1, "Estudiar", 2, LocalDate.now().plusDays(5), 0));
    }

    @Test
    void debeMostrarTipoYDetalleCorrectos() {
        TareaNormal tarea = new TareaNormal(1, "Estudiar", 2, LocalDate.now().plusDays(5), 30);

        assertEquals("NORMAL", tarea.obtenerTipo());
        assertTrue(tarea.obtenerDetalle().contains("Aviso unico 30 minutos antes"));
    }
}