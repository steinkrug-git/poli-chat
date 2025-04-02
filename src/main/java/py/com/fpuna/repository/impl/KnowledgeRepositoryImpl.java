package py.com.fpuna.repository.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import py.com.fpuna.model.knowledge.Knowledge;

import java.util.ArrayList;
import java.util.List;
import py.com.fpuna.repository.KnowledgeRepositoryCustom;

@Repository
@RequiredArgsConstructor
public class KnowledgeRepositoryImpl implements KnowledgeRepositoryCustom {

    private final MongoClient mongoClient;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Override
    public List<Knowledge> findByContentThatHas(String text) {
        MongoDatabase db = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = db.getCollection("knowledge");

        List<Knowledge> results = new ArrayList<>();

        collection.find(new Document("content", new Document("$regex", text).append("$options", "i")))
                .forEach(doc -> {
                    Knowledge c = new Knowledge();
                    c.setId(doc.getObjectId("_id").toString());
                    c.setCategory(doc.getString("category"));
                    c.setTitle(doc.getString("title"));
                    c.setContent(doc.getString("content"));
                    c.setKeywords((List<String>) doc.get("keywords"));
                    results.add(c);
                });

        return results;
    }
}
