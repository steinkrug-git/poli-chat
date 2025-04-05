package py.com.fpuna.model.collection;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "knowledge")
public class Knowledge {

    @Id
    private String id;

    @Field("category")
    private String category;
    @Field("sub_category")
    private String subcategory;
    @Field("question")
    private String question;
    @Field("similar_questions")
    private List<String> similarQuestions;
    @Field("answer")
    private String answer;
    @Field("tags")
    private List<String> tags;
    @Field("source")
    private String source;
    @Field("last_update")
    private ZonedDateTime lastUpdated;
    @Field("intent")
    private String intent;

}
