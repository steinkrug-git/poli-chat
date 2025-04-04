
<<<<<<< HEAD:src/main/java/py/com/fpuna/model/collection/SystemMessage.java
package py.com.fpuna.model.collection;
=======
package py.com.fpuna.model.knowledge;
>>>>>>> adc081f584648c1072e875763811a175d3691618:src/main/java/py/com/fpuna/model/knowledge/SystemMessage.java

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "system_messages")
public class SystemMessage {
    
    @Id
    private String id;
    @Field("key")
    private String key;
    @Field("type")
    private String type;
    @Field("content")
    private String content;
}
