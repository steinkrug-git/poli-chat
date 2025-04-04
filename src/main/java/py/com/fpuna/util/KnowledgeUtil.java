package py.com.fpuna.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import py.com.fpuna.model.collection.Knowledge;
import py.com.fpuna.model.knowledge.Intent;

/**
 *
 * @author alexa
 */
@Component
public class KnowledgeUtil {

    public Document buildQuery(String text, String intent) {
        final Document questionFilter = new Document("question", regexQuery(text));
        final Document similarFilter = new Document("similar_questions", new Document("$elemMatch", regexQuery(text)));

        final List<Document> tagFilters = new ArrayList<>();
        for (String word : text.split("\\s+")) {
            tagFilters.add(new Document("tags", new Document("$elemMatch", regexQuery(word))));
        }

        final List<Document> orConditions = new ArrayList<>();
        orConditions.add(questionFilter);
        orConditions.add(similarFilter);
        orConditions.addAll(tagFilters);

        final Document baseQuery = new Document("$or", orConditions);

        if (intent != null && !intent.isEmpty()) {
            return new Document("$and", List.of(baseQuery, new Document("intent", intent)));
        }

        return baseQuery;
    }

    public Knowledge convertToKnowledge(Document doc) {
        final Knowledge detail = new Knowledge();

        detail.setId(doc.getObjectId("_id").toString());
        detail.setCategory(doc.getString("category"));
        detail.setSubcategory(doc.getString("sub_category"));
        detail.setQuestion(doc.getString("question"));
        detail.setSimilarQuestions((List<String>) doc.get("similar_questions"));
        detail.setAnswer(doc.getString("answer"));
        detail.setTags((List<String>) doc.get("tags"));
        detail.setSource(doc.getString("source"));

        if (doc.containsKey("last_update")) {
            try {
                detail.setLastUpdated(ZonedDateTime.parse(doc.getString("last_update")));
            } catch (DateTimeParseException e) {
                detail.setLastUpdated(null);
            }
        }

        return detail;
    }

    public String detectIntent(String text) {
        return Intent.fromText(text)
                .map(Intent::getValue)
                .orElse(null);
    }

    public int countTagMatches(Document doc, Set<String> userWords) {
        List<String> tags = (List<String>) doc.get("tags");
        int matches = 0;

        for (String tag : tags) {
            for (String word : userWords) {
                if (tag.toLowerCase().contains(word)) {
                    matches++;
                    break;
                }
            }
        }

        return matches;
    }

    public Set<String> cleanAndSplitWords(String text) {
        return Arrays.stream(text.toLowerCase().replaceAll("[^\\p{L}\\p{Nd}\\s]+", "").split("\\s+"))
                .filter(word -> !word.isBlank())
                .collect(Collectors.toSet());
    }

    public Document buildTagOnlyQuery(String text, String intent) {
        final List<Document> tagFilters = new ArrayList<>();
        for (String word : cleanAndSplitWords(text)) {
            tagFilters.add(new Document("tags", new Document("$elemMatch", regexQuery(word))));
        }

        final Document baseQuery = new Document("$or", tagFilters);
        if (intent != null && !intent.isEmpty()) {
            return new Document("$and", List.of(baseQuery, new Document("intent", intent)));
        }

        return baseQuery;
    }

    public Document regexQuery(String text) {
        return new Document("$regex", text).append("$options", "i");
    }

}
