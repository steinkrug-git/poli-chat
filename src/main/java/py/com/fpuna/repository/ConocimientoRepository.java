package py.com.fpuna.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import py.com.fpuna.model.knowledge.Conocimiento;

import java.util.List;

public interface ConocimientoRepository extends MongoRepository<Conocimiento, String>, ConocimientoRepositoryCustom {

    List<Conocimiento> findByCategoria(String categoria);

    List<Conocimiento> findByKeywordsContainingIgnoreCase(String keyword);
}
