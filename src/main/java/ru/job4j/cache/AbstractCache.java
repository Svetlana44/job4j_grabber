package ru.job4j.cache;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/*
Создать структуру данных типа кеш. Кеш должен быть абстрактный.
 То есть необходимо, чтобы можно было задать ключ получения объекта кеша, и,
 в случае если его нет в памяти, задать поведение загрузки этого объекта в кеш.
 */
public abstract class AbstractCache<K, V> {

    private final Map<K, SoftReference<V>> cache = new HashMap<>();

    public final void put(K key, SoftReference<V> value) {
        cache.put(key, value);
    }

    public final SoftReference<V> get(K key) {
        return cache.get(key);
    }

    protected abstract V load(K key);

    public final File[] arrayFiles(String cachingDirOrFile) {
        /* Создаем объект File для указанной директории  */
        File directory = new File(cachingDirOrFile);
        File[] files = new File[]{};
        /* Получаем список всех txt файлов в директории  */
        if (directory.isDirectory()) {
            files = directory.listFiles(f -> f.getName().endsWith("txt"));
        }
        if (directory.isFile()) {
            files = new File[]{directory};
        }
        return files;
    }

    public final void putFilesToCach(String cachingDir) {
        File[] files = arrayFiles(cachingDir);
        /* Проверяем, что список файлов не пустой  */
        if (files != null) {
            /* Перебираем все файлы и добавляем имена в список  */
            for (File file : files) {
                if (file.isFile()) {
                    SoftReference<String> softReference = new SoftReference(this.load((K) file.getAbsolutePath()));
                    this.put((K) file.getAbsolutePath(), (SoftReference<V>) softReference);
                    /*  System.out.println(softReference.get());  */
                }
            }
        }
    }

    public final void getFilesFromCach(String cachedFile) {
        /*  Надо проверять, есть ли ключ,
         есть ли объект по мягкой ссылке,
          и в соответствии с ответами выполнять
           или не выполнять чтение файла с диска
            и сохранение информации в карте. */
        if (cache.containsKey(cachedFile) || cache.get(cachedFile) != null) {
            System.out.println(cache.get(cachedFile).get());
        }
    }
}
