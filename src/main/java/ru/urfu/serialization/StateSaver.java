package ru.urfu.serialization;

import java.util.List;

/**
 * Сохранильщик состояния объекта.
 */
abstract  public class StateSaver {

    /**
     * Сохранение состояния объекта.
     * Источник не раскрывается.
     * @param obj объект, который хотим сохранить
     */
    abstract public void save(Saveable obj);

    /**
     * Сохранение состояний нескольких объектов.
     * @param objs объекты, которые хотим сохранить
     */
    abstract public void save(List<Saveable> objs);
}
