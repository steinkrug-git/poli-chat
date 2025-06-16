package py.com.fpuna.repository.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import py.com.fpuna.cache.CachedIntent;
import py.com.fpuna.model.collection.IntentDocument;
import py.com.fpuna.repository.IntentRepository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class IntentRepositoryImpl implements IntentRepository {

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private CachedIntent cache;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Override
    public List<IntentDocument> findAllOrderedByPriority() {

        if (!cache.isExpired()){
            return cache.getValue();
        }

        MongoDatabase db = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = db.getCollection("intent");

        List<IntentDocument> result = collection.find()
                .sort(Sorts.ascending("priority"))
                .map(doc -> {
                    IntentDocument intent = new IntentDocument();
                    intent.setName(doc.getString("name"));
                    intent.setKeywordsRequired(doc.getList("keywords_required", String.class));
                    intent.setKeywordsOptional(doc.getList("keywords_optional", String.class));
                    intent.setPriority(doc.getInteger("priority", 99));
                    intent.setResponseTemplate(doc.getString("response_template"));
                    return intent;
                })
                .into(new ArrayList<>());

        cache.update(result);
        return result;
    }
}
