package py.com.fpuna.controller;

import org.springframework.web.bind.annotation.PathVariable;
import py.com.fpuna.model.request.ChatBotInputRequest;
import py.com.fpuna.model.response.ChatResponse;
import py.com.fpuna.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatGPTService) {
        this.chatService = chatGPTService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> processInputRequest(@RequestBody ChatBotInputRequest chatbotInputRequest) {
        ChatResponse chatCPTResponse = chatService.getChatCPTResponse(chatbotInputRequest.getMessage());
        return new ResponseEntity<>(chatCPTResponse, HttpStatus.OK);
    }

    @PostMapping("/chat/category/{name}")
    public ResponseEntity<String> processCategoryRequest(@PathVariable String name){
        return new ResponseEntity<>(chatService.getFAQByCategory(name), HttpStatus.OK);
    }
}
