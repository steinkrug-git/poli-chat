package py.com.fpuna.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import py.com.fpuna.model.knowledge.Knowledge;

import java.util.List;

public interface KnowledgeRepository extends MongoRepository<Knowledge, String>, KnowledgeRepositoryCustom {

    List<Knowledge> findByCategory(String category);

    List<Knowledge> findByKeywordsContainingIgnoreCase(String keyword);
}
