package py.com.fpuna.model.knowledge;

import lombok.Getter;
import java.util.*;

@Getter
public enum Intent {

    FECHA_EXAMEN("fecha_examen"),
    LUGAR_EXAMEN("lugar_examen"),
    COSTO_INSCRIPCION("costo_inscripcion"),
    CONTENIDO_EXAMEN("contenido_examen"),
    LUGAR_INSCRIPCION("lugar_inscripcion");

    private final String value;

    Intent(String value) {
        this.value = value;
    }

    private static final Map<String, Intent> keywordToIntentMap = new HashMap<>();

    static {
        // Asociar palabras clave a intenciones
        keywordToIntentMap.put("cuándo", FECHA_EXAMEN);
        keywordToIntentMap.put("fecha", FECHA_EXAMEN);
        keywordToIntentMap.put("día", FECHA_EXAMEN);
        
        keywordToIntentMap.put("dónde", LUGAR_EXAMEN);
        keywordToIntentMap.put("lugar", LUGAR_EXAMEN);
        keywordToIntentMap.put("ubicación", LUGAR_EXAMEN);
        keywordToIntentMap.put("día", LUGAR_EXAMEN);
        
        keywordToIntentMap.put("dónde", LUGAR_INSCRIPCION);
        keywordToIntentMap.put("lugar", LUGAR_INSCRIPCION);
        
        keywordToIntentMap.put("cuánto", COSTO_INSCRIPCION);
        keywordToIntentMap.put("precio", COSTO_INSCRIPCION);
        keywordToIntentMap.put("costo", COSTO_INSCRIPCION);
        keywordToIntentMap.put("cuesta", COSTO_INSCRIPCION);
        
        keywordToIntentMap.put("materia", CONTENIDO_EXAMEN);
        keywordToIntentMap.put("temas", CONTENIDO_EXAMEN);
        keywordToIntentMap.put("contenido", CONTENIDO_EXAMEN);
    }

    public static Optional<Intent> fromText(String text) {
        String lower = text.toLowerCase();
        for (Map.Entry<String, Intent> entry : keywordToIntentMap.entrySet()) {
            if (lower.contains(entry.getKey())) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }
}
