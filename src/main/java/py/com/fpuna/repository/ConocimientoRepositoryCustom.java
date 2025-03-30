package py.com.fpuna.repository;

import py.com.fpuna.model.knowledge.Conocimiento;

import java.util.List;

public interface ConocimientoRepositoryCustom {
    List<Conocimiento> buscarPorContenidoQueContenga(String texto);
}
