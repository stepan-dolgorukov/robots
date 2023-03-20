package ru.urfu.serialization;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
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
     * Загрузить состояние объекта из файла.
     * @return состояние
     */
    public State loadState() {
        // TODO: чтение из файла, (k,v)-заполнение объекта State
        storeFile.setReadable(true);

        FileInputStream fis;
        try {
            fis = new FileInputStream(storeFile);
        } catch (FileNotFoundException e) {
            return new State();
        }

        String storeData;

        try {
            storeData = new String(fis.readAllBytes());
        } catch (IOException e) {
            return new State();
        }

        JSONObject jsonState = new JSONObject(storeData);
        return new State(jsonState.toMap());
    }
}
