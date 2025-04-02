package py.com.fpuna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import py.com.fpuna.model.knowledge.Knowledge;

import java.util.List;
import py.com.fpuna.repository.KnowledgeRepository;

@Service
@RequiredArgsConstructor
public class KnowledgeService {

    private final KnowledgeRepository knowledgeRepository;

    public void save(Knowledge knowledge) {
        knowledgeRepository.save(knowledge);
    }

    public List<Knowledge> findByCategoria(String category) {
        return knowledgeRepository.findByCategory(category);
    }

    public List<Knowledge> findByKeyword(String keyword) {
        return knowledgeRepository.findByKeywordsContainingIgnoreCase(keyword);
    }

    public List<Knowledge> findByContenido(String content) {
        return knowledgeRepository.findByContentThatHas(content);
    }

    public List<Knowledge> findAll() {
        return knowledgeRepository.findAll();
    }
}
