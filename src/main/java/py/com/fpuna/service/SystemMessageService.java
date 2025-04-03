
package py.com.fpuna.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import py.com.fpuna.model.knowledge.SystemMessage;
import py.com.fpuna.repository.SystemMessageRepository;

@Service
@RequiredArgsConstructor
public class SystemMessageService {

    private final SystemMessageRepository repository;
    
    
    public String getGreeting (){
        
        Optional<SystemMessage> greeting = repository.findById("inicio");
        
        if (greeting.isEmpty()){
            return "No encontré preguntas frecuentes para esta categoría";
        }
        
        
        SystemMessage systemMessage = greeting.get();
        String greetingMessage = systemMessage.getContent();
        
        return greetingMessage;
        
    }
}
