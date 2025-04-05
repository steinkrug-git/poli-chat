
package py.com.fpuna.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import py.com.fpuna.model.collection.SystemMessage;

public interface SystemMessageRepository extends MongoRepository<SystemMessage, String> {
    
     Optional<SystemMessage> findById(String id);
    
}
