package ru.urfu.serialization;

/**
 * Загрузчик состояния объекта.
 */
abstract public class StateLoader {
    public abstract State loadState();
}
