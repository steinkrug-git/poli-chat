package py.com.fpuna.repository.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import py.com.fpuna.model.knowledge.Conocimiento;

import java.util.ArrayList;
import java.util.List;
import py.com.fpuna.repository.ConocimientoRepositoryCustom;

@Repository
@RequiredArgsConstructor
public class ConocimientoRepositoryImpl implements ConocimientoRepositoryCustom {

    private final MongoClient mongoClient;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Override
    public List<Conocimiento> buscarPorContenidoQueContenga(String texto) {
        MongoDatabase db = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = db.getCollection("conocimientos");

        List<Conocimiento> resultados = new ArrayList<>();

        collection.find(new Document("contenido", new Document("$regex", texto).append("$options", "i")))
                .forEach(doc -> {
                    Conocimiento c = new Conocimiento();
                    c.setId(doc.getObjectId("_id").toString());
                    c.setCategoria(doc.getString("categoria"));
                    c.setTitulo(doc.getString("titulo"));
                    c.setContenido(doc.getString("contenido"));
                    c.setKeywords((List<String>) doc.get("keywords"));
                    resultados.add(c);
                });

        return resultados;
    }
}
