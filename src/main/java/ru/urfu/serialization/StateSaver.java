package ru.urfu.serialization;

/**
 * Сохранильщик состояния объекта.
 */
abstract  public class StateSaver {

    /**
     * Сохранение состояния объекта.
     * Источник не раскрывается.
     * @param state состояние объекта для сохранения
     */
    abstract public void saveState(State state);
}
