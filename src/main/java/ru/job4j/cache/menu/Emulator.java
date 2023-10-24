package ru.job4j.cache.menu;

import ru.job4j.cache.DirFileCache;

import java.io.File;
import java.lang.ref.SoftReference;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Emulator {
    public static void main(String[] args) {
        DirFileCache dirFileCache = new DirFileCache("C:\\projects\\job4j_grabber\\src\\main\\java\\ru\\job4j\\cache\\menu");
        List<Path> paths = new ArrayList<>();
        /* Создаем объект File для указанной директории  */
        File directory = new File(dirFileCache.getCachingDir());

        /* Получаем список всех txt файлов в директории  */
        File[] files = directory.listFiles(f -> f.getName().endsWith("txt"));

        /* Проверяем, что список файлов не пустой  */
        if (files != null) {
            /* Перебираем все файлы и добавляем имена в список  */
            for (File file : files) {
                if (file.isFile()) {
                    SoftReference<String> softReference = new SoftReference(dirFileCache.load(file.getAbsolutePath()));
                    dirFileCache.put(file.getAbsolutePath(), softReference);
                    System.out.println(softReference.get());
                }
            }
        }
    }
}
