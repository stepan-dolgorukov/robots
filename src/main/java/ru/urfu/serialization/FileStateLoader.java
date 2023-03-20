package ru.urfu.serialization;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Загрузчик состояния объекта из файла.
 */
public class FileStateLoader extends StateLoader {
    private File storeFile;

    public FileStateLoader(File file) {
        storeFile = file;
    }

    /**
     * Загрузить состояния объекта из файла.
     * @return отображение name->state
     */
    public Map<String, State> load() {
        storeFile.setReadable(true);

        FileInputStream fis;
        try {
            fis = new FileInputStream(storeFile);
        } catch (FileNotFoundException e) {
            return null;
        }

        String storeData;

        try {
            storeData = new String(fis.readAllBytes());
        } catch (IOException e) {
            return null;
        }

        final JSONObject jsonState = new JSONObject(storeData);
        final Map<String, State> states = new HashMap<>();

        for (final var key : jsonState.toMap().keySet()) {
            final var jsonObjectState = (JSONObject)(jsonState.get(key));
            states.put(key, new State(jsonObjectState.toMap()));
        }

        return states;
    }
}
