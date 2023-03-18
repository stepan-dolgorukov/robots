package ru.urfu.serialization;

/**
 * Сохранильщик состояния объекта.
 */
abstract  public class StateSaver {
    abstract public void saveState(State state);
}
