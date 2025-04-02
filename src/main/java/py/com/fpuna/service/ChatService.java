package py.com.fpuna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import py.com.fpuna.model.response.ChatResponse;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final RestTemplate restTemplate;

    public ChatResponse getChatCPTResponse(String prompt) {

        return new ChatResponse();
    }
}
