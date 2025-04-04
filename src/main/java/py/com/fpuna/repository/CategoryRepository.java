package py.com.fpuna.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import py.com.fpuna.model.collection.Category;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findByName(String categoryName);

}
