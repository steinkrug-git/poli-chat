package py.com.fpuna.repository;

import py.com.fpuna.model.collection.IntentDocument;

import java.util.List;

public interface IntentRepository {
    List<IntentDocument> findAllOrderedByPriority();
}
