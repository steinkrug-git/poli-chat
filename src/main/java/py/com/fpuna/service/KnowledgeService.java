package py.com.fpuna.service;

import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import py.com.fpuna.model.collection.Knowledge;

import java.util.List;
import py.com.fpuna.repository.KnowledgeRepository;

@Service
@RequiredArgsConstructor
public class KnowledgeService {

    private final KnowledgeRepository knowledgeRepository;

    public String answerFromKnowledge(String userQuestion) {
        List<Knowledge> matches = knowledgeRepository.findByQuestion(userQuestion);

        if (matches.isEmpty()) {
            return "Lo siento, no encontré una respuesta para tu consulta 🤔. Probá reformularla o preguntar algo distinto.";
        }

        Knowledge bestMatch = matches.get(0);

        StringBuilder sb = new StringBuilder();
        sb.append("").append(bestMatch.getAnswer());

        if (bestMatch.getLastUpdated() != null) {
            String fecha = bestMatch.getLastUpdated()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm 'hs'"));
            sb.append("\n\n* Última actualización de esta información: ").append(fecha);
        }

        if (bestMatch.getSource() != null && !bestMatch.getSource().isEmpty()) {
            sb.append("\n* Fuente: ").append(bestMatch.getSource());
        }

        return sb.toString();
    }
}
