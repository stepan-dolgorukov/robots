package ru.urfu.serialization;

import java.util.HashMap;
import java.util.Map;

/**
 * Состояние объекта.
 * Записи хранятся в словаре.
 * Имя параметра, свойства — строка, значение параметра — строка.
 */
public class State {
    private Map<String, Object> storage;
    public State() {
        storage = new HashMap<String, Object>();
    }

    public State(Map<String, Object> properties) {
        storage = new HashMap<String, Object>(properties);
    }

    /**
     * Получение значения параметра по его имени.
     * @param property имя параметра
     * @return значение параметра
     */
    public Object getProperty(String property) {
        return storage.get(property);
    }

    /**
     * Установить значение параметра по его имени.
     * @param property имя параметра
     * @param value значение параметра
     */
    public void setProperty(String property, Object value) {
        storage.put(property, value);
    }

    /**
     * Получить (k,v)-хранилище параметров.
     */
    public Map<String, Object> getStorage() {
        return storage;
    }
}