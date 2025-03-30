package py.com.fpuna.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import py.com.fpuna.model.knowledge.Conocimiento;

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

    private final ConocimientoService conocimientoService;

    @Value("${csv.file.path}")
    private String csvFilePath;

    public void scrapeAllSections() {
        Map<String, String> sections = Map.of(
                "Académico", "https://www.pol.una.py/academico",
                "Admisión", "https://www.pol.una.py/admision",
                "Carreras", "https://www.pol.una.py/carreras"
        );

        sections.forEach((categoria, url) -> {
            try {
                Document doc = Jsoup.connect(url).get();

                String titulos = doc.select("h1, h2, h3").text();
                String contenido = doc.select("p").text();

                if (!contenido.isBlank()) {
                    Conocimiento conocimiento = new Conocimiento();
                    conocimiento.setCategoria(categoria);
                    conocimiento.setTitulo(titulos.length() > 100 ? titulos.substring(0, 100) + "..." : titulos);
                    conocimiento.setContenido(contenido);
                    conocimiento.setKeywords(List.of(categoria.toLowerCase()));

                    conocimientoService.save(conocimiento);
                    System.out.println("Sección '" + categoria + "' guardada desde: " + url);
                }

            } catch (IOException e) {
                System.err.println("Error scraping " + url + ": " + e.getMessage());
            }
        });
    }

    public void insertManualEntry() {
        Conocimiento conocimiento = new Conocimiento();
        conocimiento.setCategoria("Carreras");
        conocimiento.setTitulo("Ingeniería en Informática");
        conocimiento.setContenido("La carrera de Ingeniería en Informática forma profesionales altamente capacitados...");
        conocimiento.setKeywords(List.of("informática", "carrera", "software", "ingeniería"));

        conocimientoService.save(conocimiento);
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

                Conocimiento conocimiento = new Conocimiento();
                conocimiento.setCategoria(parts[0]);
                conocimiento.setTitulo(parts[1]);
                conocimiento.setContenido(parts[2]);
                conocimiento.setKeywords(Arrays.asList(parts[3].split(",")));

                conocimientoService.save(conocimiento);
            }

            System.out.println("Datos cargados desde el CSV correctamente.");
        } catch (Exception e) {
            System.err.println("Error leyendo el CSV: " + e.getMessage());
        }
    }
}
