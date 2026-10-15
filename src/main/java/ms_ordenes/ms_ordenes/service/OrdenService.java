package ms_ordenes.ms_ordenes.service;

import ms_ordenes.ms_ordenes.exception.RecursoNoEncontradoException;
import ms_ordenes.ms_ordenes.model.EstadoOrden;
import ms_ordenes.ms_ordenes.model.Orden;
import ms_ordenes.ms_ordenes.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    public Orden crearOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    public List<Orden> obtenerTodas() {
        return ordenRepository.findAll();
    }

    public List<Orden> obtenerOrdenesPorUsuario(Long idUsuario) {
        return ordenRepository.findByIdUsuario(idUsuario);
    }

    public Orden obtenerOrdenPorId(Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Orden no encontrada con ID: " + id));
    }

    public Orden actualizarEstado(Long id, EstadoOrden nuevoEstado) {
        Orden orden = obtenerOrdenPorId(id);
        orden.setEstado(nuevoEstado);
        return ordenRepository.save(orden);
    }
}