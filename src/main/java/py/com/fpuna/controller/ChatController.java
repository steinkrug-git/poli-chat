package py.com.fpuna.controller;

import org.springframework.web.bind.annotation.PathVariable;
import py.com.fpuna.model.request.ChatBotInputRequest;
import py.com.fpuna.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatGPTService) {
        this.chatService = chatGPTService;
    }

    @PostMapping("/chat/category/{name}")
    public ResponseEntity<String> processCategoryRequest(@PathVariable String name) {
        return new ResponseEntity<>(chatService.getFAQByCategory(name), HttpStatus.OK);
    }


    @PostMapping("/chat/respond")
    public ResponseEntity<String> processAnswerRequest(@RequestBody ChatBotInputRequest chatbotInputRequest) {
        String answer = chatService.getAnswer(chatbotInputRequest.getMessage());
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    @GetMapping("/chat/start")
    public ResponseEntity<String> getGreeting() {
        return new ResponseEntity<>(chatService.getGreeting(), HttpStatus.OK);

    }

}
