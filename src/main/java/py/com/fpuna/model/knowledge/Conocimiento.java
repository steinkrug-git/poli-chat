package py.com.fpuna.model.knowledge;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "conocimientos")
public class Conocimiento {
    @Id
    private String id;
    private String categoria;
    private String titulo;
    private String contenido;
    private List<String> keywords;
}
