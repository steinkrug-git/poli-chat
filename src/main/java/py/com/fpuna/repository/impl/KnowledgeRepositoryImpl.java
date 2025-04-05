package py.com.fpuna.repository.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import py.com.fpuna.model.collection.Knowledge;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import py.com.fpuna.repository.KnowledgeRepository;
import py.com.fpuna.util.KnowledgeUtil;

@Repository
@RequiredArgsConstructor
public class KnowledgeRepositoryImpl implements KnowledgeRepository {

    private final MongoClient mongoClient;
    private final KnowledgeUtil knowledgeUtil;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    private static final int MIN_TAG_MATCHES = 0;

    @Override
    public List<Knowledge> findByQuestion(String text) {
        MongoDatabase db = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = db.getCollection("knowledge");

        String intent = knowledgeUtil.detectIntent(text);
        Set<String> userWords = knowledgeUtil.cleanAndSplitWords(text);

        Document queryQuestion = new Document("question", knowledgeUtil.regexQuery(text));
        if (intent != null) {
            queryQuestion = new Document("$and", List.of(queryQuestion, new Document("intent", intent)));
        }

        List<Document> docs = collection.find(queryQuestion).into(new ArrayList<>());
        if (!docs.isEmpty()) {
            return convertAndReturn(docs);
        }

        Document querySimilar = new Document("similar_questions", new Document("$elemMatch", knowledgeUtil.regexQuery(text)));
        if (intent != null) {
            querySimilar = new Document("$and", List.of(querySimilar, new Document("intent", intent)));
        }

        docs = collection.find(querySimilar).into(new ArrayList<>());
        if (!docs.isEmpty()) {
            return convertAndReturn(docs);
        }

        Document tagQuery = knowledgeUtil.buildTagOnlyQuery(text, intent);
        docs = collection.find(tagQuery).into(new ArrayList<>());

        return docs.stream()
                .sorted((a, b) -> Integer.compare(
                knowledgeUtil.countTagMatches(b, userWords),
                knowledgeUtil.countTagMatches(a, userWords)))
                .filter(doc -> knowledgeUtil.countTagMatches(doc, userWords) >= MIN_TAG_MATCHES)
                .map(knowledgeUtil::convertToKnowledge)
                .toList();
    }

    private List<Knowledge> convertAndReturn(List<Document> docs) {
        return docs.stream()
                .map(knowledgeUtil::convertToKnowledge)
                .toList();
    }

}
