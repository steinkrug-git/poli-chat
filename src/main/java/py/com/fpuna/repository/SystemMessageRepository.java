
package py.com.fpuna.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
<<<<<<< HEAD
import py.com.fpuna.model.collection.SystemMessage;
=======
import py.com.fpuna.model.knowledge.SystemMessage;
>>>>>>> adc081f584648c1072e875763811a175d3691618

public interface SystemMessageRepository extends MongoRepository<SystemMessage, String> {
    
     Optional<SystemMessage> findById(String id);
    
}
