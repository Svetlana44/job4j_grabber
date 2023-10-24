package ru.job4j.cache;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringJoiner;

public class DirFileCache extends AbstractCache<String, String> {

    private final String cachingDir;

    public DirFileCache(String cachingDir) {
        this.cachingDir = cachingDir;
    }

    public String getCachingDir() {
        return cachingDir;
    }

    /*  Важно! key это относительный путь к файлу в директории.  */
    @Override
    public String load(String key) {
        StringJoiner content = new StringJoiner(System.lineSeparator());
        try (BufferedReader in = new BufferedReader(new FileReader(key))) {
            in.lines().forEach(s -> content.add(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

}