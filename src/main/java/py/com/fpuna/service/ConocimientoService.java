package py.com.fpuna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import py.com.fpuna.model.knowledge.Conocimiento;
import py.com.fpuna.repository.ConocimientoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConocimientoService {

    private final ConocimientoRepository conocimientoRepository;

    public void save(Conocimiento conocimiento) {
        conocimientoRepository.save(conocimiento);
    }

    public List<Conocimiento> findByCategoria(String categoria) {
        return conocimientoRepository.findByCategoria(categoria);
    }

    public List<Conocimiento> findByKeyword(String keyword) {
        return conocimientoRepository.findByKeywordsContainingIgnoreCase(keyword);
    }

    public List<Conocimiento> findByContenido(String contenido) {
        return conocimientoRepository.buscarPorContenidoQueContenga(contenido);
    }

    public List<Conocimiento> findAll() {
        return conocimientoRepository.findAll();
    }
}
