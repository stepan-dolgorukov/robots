package ru.urfu.serialization;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
     *
     * @param obj объект, который хотим сохранить в файл
     */
    @Override
    public void save(Saveable obj) {
        try {
            storeFile.createNewFile();
        } catch (IOException e) {
            return;
        }
        storeFile.setWritable(true);

        final JSONObject jsonState = new JSONObject(obj.state().getStorage());
        final JSONObject jsonObj = new JSONObject();
        jsonObj.put(obj.getName(), jsonState);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(storeFile);
        } catch (FileNotFoundException e) {
            return;
        }
        try {
            fos.write(jsonObj.toString(0).getBytes());
        } catch (IOException e) {
            return;
        }
    }

    /**
     * Сохранение состояний объектов в файл.
     * Расположение файла известно заранее. Член storeFile.
     *
     * @param objs список объектов на сохранение
     */
    @Override
    public void save(List<Saveable> objs) {
        try {
            storeFile.createNewFile();
        } catch (IOException e) {
            return;
        }
        storeFile.setWritable(true);

        final JSONObject json = new JSONObject();

        for (final var obj : objs) {
            final JSONObject jsonState = new JSONObject(obj.state().getStorage());
            json.put(obj.getName(), jsonState);
        }

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