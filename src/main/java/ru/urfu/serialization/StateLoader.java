package ru.urfu.serialization;

/**
 * Загрузчик состояния объекта.
 */
abstract public class StateLoader {

    /**
     * Загрузка состояния.
     * Источник не раскрывается.
     * @return состояние
     */
    public abstract State loadState();
}
