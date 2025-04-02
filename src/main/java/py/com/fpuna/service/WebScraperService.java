package py.com.fpuna.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import py.com.fpuna.model.knowledge.Knowledge;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebScraperService {

    private final KnowledgeService knowledgeService;

    @Value("${csv.file.path}")
    private String csvFilePath;

    public void scrapeAllSections() {
        Map<String, String> sections = Map.of(
                "Académico", "https://www.pol.una.py/academico",
                "Admisión", "https://www.pol.una.py/admision",
                "Carreras", "https://www.pol.una.py/carreras"
        );

        sections.forEach((category, url) -> {
            try {
                Document doc = Jsoup.connect(url).get();

                String titles = doc.select("h1, h2, h3").text();
                String content = doc.select("p").text();

                if (!content.isBlank()) {
                    Knowledge conocimiento = new Knowledge();
                    conocimiento.setCategory(category);
                    conocimiento.setTitle(titles.length() > 100 ? titles.substring(0, 100) + "..." : titles);
                    conocimiento.setContent(content);
                    conocimiento.setKeywords(List.of(category.toLowerCase()));

                    knowledgeService.save(conocimiento);
                    System.out.println("Sección '" + category + "' guardada desde: " + url);
                }

            } catch (IOException e) {
                System.err.println("Error scraping " + url + ": " + e.getMessage());
            }
        });
    }

    public void insertManualEntry() {
        Knowledge knowledge = new Knowledge();
        knowledge.setCategory("Carreras");
        knowledge.setTitle("Ingeniería en Informática");
        knowledge.setContent("La carrera de Ingeniería en Informática forma profesionales altamente capacitados...");
        knowledge.setKeywords(List.of("informática", "carrera", "software", "ingeniería"));

        knowledgeService.save(knowledge);
        System.out.println("Conocimiento insertado manualmente.");
    }

    public void insertFromCSV() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(csvFilePath), StandardCharsets.UTF_8))) {

            String line;
            boolean isFirst = true;

            while ((line = reader.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                    continue;
                }

                String[] parts = line.split(";", 4);
                if (parts.length < 4) {
                    continue;
                }

                Knowledge knowledge = new Knowledge();
                knowledge.setCategory(parts[0]);
                knowledge.setTitle(parts[1]);
                knowledge.setContent(parts[2]);
                knowledge.setKeywords(Arrays.asList(parts[3].split(",")));

                knowledgeService.save(knowledge);
            }

            System.out.println("Datos cargados desde el CSV correctamente.");
        } catch (Exception e) {
            System.err.println("Error leyendo el CSV: " + e.getMessage());
        }
    }
}
