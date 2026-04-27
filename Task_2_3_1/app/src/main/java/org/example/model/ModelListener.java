package org.example.model;

/**
 * Слушатель изменений модели.
 */
public interface ModelListener {

    /**
     * Вызывается моделью после изменения состояния.
     */
    void onModelUpdated(GameSnapshot snapshot);
}
