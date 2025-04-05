package py.com.fpuna.repository;

import py.com.fpuna.model.collection.Knowledge;

import java.util.List;

public interface KnowledgeRepository {
    List<Knowledge> findByQuestion(String texto);
}
