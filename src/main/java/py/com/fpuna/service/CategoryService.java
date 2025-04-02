package py.com.fpuna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import py.com.fpuna.model.knowledge.Category;
import py.com.fpuna.repository.CategoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public String getCategoryFAQ(String name) {
        Optional<Category> catOpt = categoryRepository.findByName(name);

        if (catOpt.isEmpty()) {
            return "No encontré preguntas frecuentes para esa categoría.";
        }

        Category category = catOpt.get();

        StringBuilder sb = new StringBuilder();
        sb.append("Oh, elegiste ").append(category.getName().toLowerCase()).append(", algunas preguntas frecuentes suelen ser:\n\n");

        category.getFrequentlyAskedQuestions().forEach(pregunta -> {
            sb.append(pregunta).append("\n");
        });

        sb.append("\nSelecciona o formula tu propia duda 👀");

        return sb.toString();
    }
}

