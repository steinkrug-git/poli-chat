
package py.com.fpuna.model.knowledge;

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
