package py.com.fpuna.util;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.com.fpuna.model.collection.IntentDocument;
import py.com.fpuna.model.collection.Knowledge;
import py.com.fpuna.repository.IntentRepository;

import java.text.Normalizer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author alexa
 */
@Component
public class KnowledgeUtil {

    private static final Set<String> STOPWORDS = Set.of(
            "de", "el", "la", "en", "y", "a", "del", "los", "las", "un", "una", "para", "es", "al"
    );

    @Autowired
    private IntentRepository intentRepository;

    public Document buildQuery(String text, String intent) {
        final Document questionFilter = new Document("question", regexQuery(text));
        final Document similarFilter = new Document("similar_questions", new Document("$elemMatch", regexQuery(text)));

        final List<Document> tagFilters = new ArrayList<>();
        for (String word : cleanAndSplitWords(text)) {
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

    public Optional<IntentDocument> detectIntentFromDb(String text) {
        IntentDocument bestIntent = null;
        Long bestScore = 0L;

        Set<String> words = cleanAndSplitWords(text);
        List<IntentDocument> intents = intentRepository.findAllOrderedByPriority();

        for (IntentDocument intent : intents) {
            Set<String> requiredKeywords = intent.getKeywordsRequired().stream()
                    .map(KnowledgeUtil::cleanWord)
                    .collect(Collectors.toSet());

            Set<String> optionalKeywords = intent.getKeywordsOptional().stream()
                    .map(KnowledgeUtil::cleanWord)
                    .collect(Collectors.toSet());

            if (!requiredKeywords.stream().allMatch(words::contains)) continue;

            Long score = optionalKeywords.stream()
                    .filter(words::contains)
                    .count();

            if (score > bestScore) {
                bestScore = score;
                bestIntent = intent;
            }
        }

        return Optional.ofNullable(bestIntent);
    }

    public Set<String> cleanAndSplitWords(String text) {
        return Arrays.stream(text.split("\\s+"))
                .map(KnowledgeUtil::cleanWord)
                .filter(word -> !word.isBlank() && !STOPWORDS.contains(word))
                .collect(Collectors.toSet());
    }

    public static String cleanWord(String word) {
        return Normalizer.normalize(word, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^\\p{L}\\p{Nd}\\s]+", "");
    }

    public Document regexQuery(String text) {
        return new Document("$regex", text).append("$options", "i");
    }

    public int countMatchScore(Document doc, Set<String> userWords, String detectedIntent) {
        int score = 0;

        score += getScoreFromQuestion(doc, userWords, score);
        score += getScoreFromSimilarQuestions(doc, userWords, score);
        score += getScoreFromTags(doc, userWords, score);
        score += getScoreFromIntent(doc, detectedIntent, score);

        return score;
    }

    private static int getScoreFromIntent(Document doc, String detectedIntent, int score) {
        String docIntent = doc.getString("intent");
        if (docIntent != null && docIntent.equalsIgnoreCase(detectedIntent)) {
            score += 20;
        }
        return score;
    }

    private int getScoreFromTags(Document doc, Set<String> userWords, int score) {
        List<String> tags = (List<String>) doc.get("tags");
        if (tags != null) {
            for (String tag : tags) {
                Set<String> tagWords = cleanAndSplitWords(tag);
                for (String word : userWords) {
                    if (tagWords.contains(word)) {
                        score += 1;
                        break;
                    }
                }
            }
        }
        return score;
    }

    private int getScoreFromSimilarQuestions(Document doc, Set<String> userWords, int score) {
        List<String> similarQuestions = (List<String>) doc.get("similar_questions");
        if (similarQuestions != null) {
            for (String similar : similarQuestions) {
                Set<String> similarWords = cleanAndSplitWords(similar);
                for (String word : userWords) {
                    if (similarWords.contains(word)) {
                        score += 1;
                    }
                }
            }
        }
        return score;
    }

    private int getScoreFromQuestion(Document doc, Set<String> userWords, int score) {
        String question = doc.getString("question");
        if (question != null) {
            Set<String> questionWords = cleanAndSplitWords(question);
            for (String word : userWords) {
                if (questionWords.contains(word)) {
                    score += 2;
                }
            }
        }
        return score;
    }
}
