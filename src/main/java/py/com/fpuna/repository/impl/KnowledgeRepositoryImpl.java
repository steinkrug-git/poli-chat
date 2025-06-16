package py.com.fpuna.repository.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import py.com.fpuna.model.collection.IntentDocument;
import py.com.fpuna.model.collection.Knowledge;
import py.com.fpuna.repository.KnowledgeRepository;
import py.com.fpuna.util.KnowledgeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class KnowledgeRepositoryImpl implements KnowledgeRepository {

    private final MongoClient mongoClient;
    private final KnowledgeUtil knowledgeUtil;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    private static final int MIN_SCORE = 3;

    @Override
    public List<Knowledge> findByQuestion(String text) {
        MongoDatabase db = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = db.getCollection("knowledge");

        Optional<IntentDocument> optionalIntent = knowledgeUtil.detectIntentFromDb(text);
        String intent = optionalIntent.map(IntentDocument::getName).orElse(null);
        Set<String> userWords = knowledgeUtil.cleanAndSplitWords(text);

        Document query = knowledgeUtil.buildQuery(text, intent);

        List<Document> docs = collection.find(query).into(new ArrayList<>());

        return docs.stream()
                .sorted((a, b) -> Integer.compare(
                knowledgeUtil.countMatchScore(b, userWords, intent),
                knowledgeUtil.countMatchScore(a, userWords, intent)))
                .filter(doc -> knowledgeUtil.countMatchScore(doc, userWords, intent) >= MIN_SCORE)
                .map(knowledgeUtil::convertToKnowledge)
                .toList();
    }

    private List<Knowledge> convertAndReturn(List<Document> docs) {
        return docs.stream()
                .map(knowledgeUtil::convertToKnowledge)
                .toList();
    }

}
