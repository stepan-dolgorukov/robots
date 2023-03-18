package ru.urfu.serialization;

import java.util.HashMap;
import java.util.Map;

/**
 * Состояние объекта.
 * Записи хранятся в словаре.
 * Имя параметра, свойства — строка, значение параметра — строка.
 */
public class State {
    private Map<String, String> storage;
    public State() {
        storage = new HashMap<String, String>();
    }

    public State(Map<String, String> properties) {
        storage = new HashMap<String, String>(properties);
    }

    /**
     * Получение значения параметра по его имени.
     * @param property имя параметра
     * @return значение параметра
     */
    public String getProperty(String property) {
        return storage.get(property);
    }

    /**
     * Установить значение параметра по его имени.
     * @param property имя параметра
     * @param value значение параметра
     */
    public void setProperty(String property, String value) {
        storage.put(property, value);
    }
}