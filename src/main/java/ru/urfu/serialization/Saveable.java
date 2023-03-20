package ru.urfu.serialization;

/**
 * Объект, у которого можно взять состояние.
 */
public interface Saveable {
    State state();
    String getName();
    void setState(State state);
}