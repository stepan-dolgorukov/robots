package ru.urfu.serialization;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Сохранение состояния объекта в файл.
 */
public class FileStateSaver extends StateSaver {
    File storeFile;

    public FileStateSaver(File file) {
        storeFile = file;
    }

    /**
     * Сохранение состояния объекта в файл.
     * Расположение файла известно заранее. Член storeFile.
     * @param state состояние объекта для сохранения
     */
    public void saveState(State state) {
        try {
            storeFile.createNewFile();
        } catch (IOException e) {
            return;
        }
        storeFile.setWritable(true);

        final JSONObject json = new JSONObject(state.getStorage());
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(storeFile);
        } catch (FileNotFoundException e) {
            return;
        }
        try {
            fos.write(json.toString(0).getBytes());
        } catch (IOException e) {
            return;
        }
    }
}