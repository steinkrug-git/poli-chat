package py.com.fpuna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import py.com.fpuna.model.response.ChatResponse;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final RestTemplate restTemplate;

    private final CategoryService categoryService;
    
    private final SystemMessageService systemMessageService;


    public ChatResponse getChatCPTResponse(String prompt) {

        return new ChatResponse();
    }

    public String getFAQByCategory (String prompt){
        return categoryService.getCategoryFAQ(prompt);
    }
    
    public String getGreating() {
        return systemMessageService.getGreeting();
    }
}
