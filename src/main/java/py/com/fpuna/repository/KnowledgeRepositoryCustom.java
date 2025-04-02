package py.com.fpuna.repository;

import py.com.fpuna.model.knowledge.Knowledge;

import java.util.List;

public interface KnowledgeRepositoryCustom {
    List<Knowledge> findByContentThatHas(String texto);
}
