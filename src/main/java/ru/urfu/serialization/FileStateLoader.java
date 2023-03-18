package ru.urfu.serialization;

import java.io.File;

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
        return new State();
    }
}
