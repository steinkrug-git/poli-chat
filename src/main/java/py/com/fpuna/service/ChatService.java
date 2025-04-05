package py.com.fpuna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RestTemplate restTemplate;

    private final CategoryService categoryService;
    
    private final SystemMessageService systemMessageService;

    private final KnowledgeService knowledgeService;

    public String getFAQByCategory(String prompt) {
        return categoryService.getCategoryFAQ(prompt);
    }

    public String getAnswer(String question) {
        return knowledgeService.answerFromKnowledge(question);
    }

    public String getGreeting() {
        return systemMessageService.getGreeting();
    }
}
