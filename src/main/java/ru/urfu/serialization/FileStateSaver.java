package ru.urfu.serialization;

import java.io.File;

/**
 * Сохранение состояния объекта в файл.
 */
public class FileStateSaver extends StateSaver {
    File storeFile;

    FileStateSaver(File file) {
        storeFile = file;
    }

    /**
     * Сохранение состояния объекта в файл.
     * Расположение файла известно заранее. Член storeFile.
     * @param state состояние объекта для сохранения
     */
    public void saveState(State state) {
        // TODO: выгрузка состояния в файл
    }
}
