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

    private final SystemMessageService systemMessageService;

    private final KnowledgeService knowledgeService1;

    public String getFAQByCategory(String prompt) {
        return categoryService.getCategoryFAQ(prompt);
    }
<<<<<<< HEAD

    public String getGreating() {
        return systemMessageService.getGreeting();
    }

    public String getAnswer(String question) {
        return knowledgeService1.answerFromKnowledge(question);
    }

=======
    
    public String getGreating() {
        return systemMessageService.getGreeting();
    }
>>>>>>> adc081f584648c1072e875763811a175d3691618
}
