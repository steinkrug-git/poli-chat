package py.com.fpuna.model.knowledge;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "conocimientos")
public class Knowledge {
    @Id
    private String id;
    private String category;
    private String title;
    private String content;
    private List<String> keywords;
}
