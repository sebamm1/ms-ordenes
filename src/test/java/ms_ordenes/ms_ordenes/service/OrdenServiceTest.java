package ms_ordenes.ms_ordenes.service;

import ms_ordenes.ms_ordenes.model.EstadoOrden;
import ms_ordenes.ms_ordenes.model.Orden;
import ms_ordenes.ms_ordenes.repository.OrdenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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

    private Orden ordenPrueba;

    @BeforeEach
    void setUp() {
        ordenPrueba = new Orden(1L, new BigDecimal("29990.00"));
        ordenPrueba.setId(1L);
        ordenPrueba.setEstado(EstadoOrden.PENDIENTE);
    }

    @Test
    void crearOrden_AsignaEstadoPendienteYGuardaCorrectamente() {
        when(ordenRepository.save(any(Orden.class))).thenReturn(ordenPrueba);

        Orden resultado = ordenService.crearOrden(new Orden(1L, new BigDecimal("29990.00")));

        assertNotNull(resultado);
        assertEquals(EstadoOrden.PENDIENTE, resultado.getEstado());
        verify(ordenRepository, times(1)).save(any(Orden.class));
    }

    @Test
    void actualizarEstado_TransicionValida_ActualizaEstado() {
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(ordenPrueba));
        when(ordenRepository.save(any(Orden.class))).thenReturn(ordenPrueba);

        Orden resultado = ordenService.actualizarEstado(1L, EstadoOrden.CONFIRMADO);

        assertEquals(EstadoOrden.CONFIRMADO, resultado.getEstado());
    }

    @Test
    void actualizarEstado_TransicionInvalida_LanzaExcepcion() {
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(ordenPrueba));

        assertThrows(IllegalArgumentException.class, () -> {
            ordenService.actualizarEstado(1L, EstadoOrden.ENTREGADO);
        });
    }
}