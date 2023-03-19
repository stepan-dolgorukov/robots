package ru.urfu.serialization;

import java.util.List;

/**
 * Сохранильщик состояния объекта.
 */
abstract  public class StateSaver {

    /**
     * Сохранение состояния объекта.
     * Источник не раскрывается.
     * @param state состояние объекта для сохранения
     */
    abstract public void save(State state);
    abstract public void save(List<State> states);
}
