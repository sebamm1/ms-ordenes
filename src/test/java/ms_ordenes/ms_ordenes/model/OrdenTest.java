package ms_ordenes.ms_ordenes.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrdenTest {

    @Test
    void testConstructoresYGettersSetters() {
        LocalDateTime ahora = LocalDateTime.now();
        
        // Constructor con parámetros
        Orden orden = new Orden(1L, 10L, 5000.0, EstadoOrden.PENDIENTE, ahora);
        assertEquals(1L, orden.getId());
        assertEquals(10L, orden.getIdUsuario());
        assertEquals(5000.0, orden.getTotal());
        assertEquals(EstadoOrden.PENDIENTE, orden.getEstado());
        assertEquals(ahora, orden.getFechaCreacion());

        // Constructor vacío y setters
        Orden ordenVacia = new Orden();
        ordenVacia.setId(2L);
        ordenVacia.setIdUsuario(20L);
        ordenVacia.setTotal(1500.0);
        ordenVacia.setEstado(EstadoOrden.PAGADA);
        ordenVacia.setFechaCreacion(ahora);

        assertEquals(2L, ordenVacia.getId());
        assertEquals(20L, ordenVacia.getIdUsuario());
        assertEquals(1500.0, ordenVacia.getTotal());
        assertEquals(EstadoOrden.PAGADA, ordenVacia.getEstado());
        assertEquals(ahora, ordenVacia.getFechaCreacion());
    }

    @Test
    void testOnCreateAsignaValoresPorDefecto() {
        Orden orden = new Orden();
        orden.onCreate();

        assertNotNull(orden.getFechaCreacion());
        assertEquals(EstadoOrden.PENDIENTE, orden.getEstado());
    }

    @Test
    void testOnCreateNoSobrescribeEstadoSiYaExiste() {
        Orden orden = new Orden();
        orden.setEstado(EstadoOrden.ENVIADA);
        orden.onCreate();

        assertEquals(EstadoOrden.ENVIADA, orden.getEstado());
    }
}