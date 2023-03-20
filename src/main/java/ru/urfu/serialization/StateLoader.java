package ru.urfu.serialization;

import java.util.List;
import java.util.Map;

/**
 * Загрузчик состояния объекта.
 */
abstract public class StateLoader {

    /**
     * Загрузка состояния объектов.
     * Источник не раскрывается.
     * @return отображение объект -> его состояние
     */
    public abstract Map<String, State> load();
}
