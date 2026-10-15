package ms_ordenes.ms_ordenes.controller;

import ms_ordenes.ms_ordenes.exception.GlobalExceptionHandler;
import ms_ordenes.ms_ordenes.exception.RecursoNoEncontradoException;
import ms_ordenes.ms_ordenes.model.EstadoOrden;
import ms_ordenes.ms_ordenes.model.Orden;
import ms_ordenes.ms_ordenes.service.OrdenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrdenControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrdenService ordenService;

    @InjectMocks
    private OrdenController ordenController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ordenController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testCrearOrden_Exito() throws Exception {
        Orden ordenRespuesta = new Orden(1L, 1L, 5000.0, EstadoOrden.PENDIENTE, null);

        when(ordenService.crearOrden(any(Orden.class))).thenReturn(ordenRespuesta);

        String jsonEnvio = """
                {
                    "idUsuario": 1,
                    "total": 5000.0
                }
                """;

        mockMvc.perform(post("/api/ordenes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEnvio))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.total").value(5000.0));
    }

    @Test
    void testCrearOrden_FallaValidacion() throws Exception {
        String jsonInvalido = """
                {
                    "idUsuario": null,
                    "total": null
                }
                """;

        mockMvc.perform(post("/api/ordenes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testListarOrdenes() throws Exception {
        when(ordenService.obtenerTodas()).thenReturn(Arrays.asList(new Orden()));

        mockMvc.perform(get("/api/ordenes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testObtenerPorId_Exito() throws Exception {
        Orden orden = new Orden(1L, 1L, 100.0, EstadoOrden.PENDIENTE, null);
        when(ordenService.obtenerOrdenPorId(1L)).thenReturn(orden);

        mockMvc.perform(get("/api/ordenes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testObtenerPorId_NoEncontrado() throws Exception {
        when(ordenService.obtenerOrdenPorId(99L))
                .thenThrow(new RecursoNoEncontradoException("Orden no encontrada"));

        mockMvc.perform(get("/api/ordenes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Orden no encontrada"));
    }

    @Test
    void testObtenerPorUsuario() throws Exception {
        when(ordenService.obtenerOrdenesPorUsuario(1L)).thenReturn(Arrays.asList(new Orden()));

        mockMvc.perform(get("/api/ordenes/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testActualizarEstado() throws Exception {
        Orden ordenActualizada = new Orden(1L, 1L, 100.0, EstadoOrden.PAGADA, null);
        when(ordenService.actualizarEstado(eq(1L), any(EstadoOrden.class))).thenReturn(ordenActualizada);

        mockMvc.perform(patch("/api/ordenes/1/estado")
                .param("estado", "PAGADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PAGADA"));
    }
}