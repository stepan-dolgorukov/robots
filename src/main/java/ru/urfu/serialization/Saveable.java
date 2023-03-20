package ru.urfu.serialization;

/**
 * Объект, у которого можно взять состояние.
 */
public interface Saveable {

    /**
     * Взять состояния у объекта.
     */
    State state();

    /**
     * Уникальное имя объекта. Требуется для десериализации.
     */
    String getName();

    /**
     * Загрузить состояние в объект.
     */
    void setState(State state);
}