package ru.urfu.serialization;

/**
 * Объект, у которого можно взять состояние.
 */
public interface Saveable {
    State getState();
    String getName();
}