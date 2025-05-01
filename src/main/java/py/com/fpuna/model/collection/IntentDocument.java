package py.com.fpuna.model.collection;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "intent")
public class IntentDocument {
    private String name;
    private List<String> keywordsRequired;
    private List<String> keywordsOptional;
    private int priority;
    private String responseTemplate;

}
