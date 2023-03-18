package ru.urfu.serialization;

/**
 * Transmitable ~ Able to transmit.
 * Объект, у которого можно снять состояние.
 */
public interface Transmitable {
    State getState();
}
