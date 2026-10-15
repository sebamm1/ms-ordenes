package ms_ordenes.ms_ordenes.service;

import ms_ordenes.ms_ordenes.exception.RecursoNoEncontradoException;
import ms_ordenes.ms_ordenes.model.EstadoOrden;
import ms_ordenes.ms_ordenes.model.Orden;
import ms_ordenes.ms_ordenes.repository.OrdenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @InjectMocks
    private OrdenService ordenService;

    private Orden ordenBase;

    @BeforeEach
    void setUp() {
        ordenBase = new Orden(1L, 10L, 2500.0, EstadoOrden.PENDIENTE, LocalDateTime.now());
    }

    @Test
    void testCrearOrden() {
        when(ordenRepository.save(any(Orden.class))).thenReturn(ordenBase);
        Orden creada = ordenService.crearOrden(new Orden());
        assertNotNull(creada);
        assertEquals(1L, creada.getId());
        verify(ordenRepository, times(1)).save(any(Orden.class));
    }

    @Test
    void testObtenerTodas() {
        when(ordenRepository.findAll()).thenReturn(Arrays.asList(ordenBase));
        List<Orden> lista = ordenService.obtenerTodas();
        assertEquals(1, lista.size());
    }

    @Test
    void testObtenerOrdenesPorUsuario() {
        when(ordenRepository.findByIdUsuario(10L)).thenReturn(Arrays.asList(ordenBase));
        List<Orden> lista = ordenService.obtenerOrdenesPorUsuario(10L);
        assertEquals(1, lista.size());
        assertEquals(10L, lista.get(0).getIdUsuario());
    }

    @Test
    void testObtenerOrdenPorId_Exito() {
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(ordenBase));
        Orden encontrada = ordenService.obtenerOrdenPorId(1L);
        assertEquals(1L, encontrada.getId());
    }

    @Test
    void testObtenerOrdenPorId_Falla() {
        when(ordenRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> ordenService.obtenerOrdenPorId(99L));
    }

    @Test
    void testActualizarEstado() {
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(ordenBase));
        when(ordenRepository.save(any(Orden.class))).thenReturn(ordenBase);

        Orden actualizada = ordenService.actualizarEstado(1L, EstadoOrden.PAGADA);

        assertEquals(EstadoOrden.PAGADA, actualizada.getEstado());
        verify(ordenRepository).save(ordenBase);
    }
}