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

}
