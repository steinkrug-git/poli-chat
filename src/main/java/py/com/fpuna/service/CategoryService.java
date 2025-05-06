package py.com.fpuna.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import py.com.fpuna.model.collection.Category;
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
        category.getFrequentlyAskedQuestions().forEach(pregunta -> {
            sb.append(pregunta).append("\n");
        });

        return sb.toString();
    }
}

