package ru.job4j.cache.menu;

import ru.job4j.cache.DirFileCache;

import java.util.Scanner;

public class Emulator {

    public static void printMenu() {
        System.out.println("________________________"
                + "Выберите пункт меню_______________________"
                + System.lineSeparator()
                + "1 - указать кэшируемую директорию"
                + System.lineSeparator()
                + "2 - загрузить содержимое файла в кэш"
                + System.lineSeparator()
                + "3 - получить содержимое файла из кэша"
                + System.lineSeparator()
                + "для завершения работы введите Exit.");

    }

    public static void showMenu() {
        /*        DirFileCache dirFileCache = new DirFileCache("C:\\projects\\job4j_grabber\\src\\main\\java\\ru\\job4j\\cache\\menu");  */
        String cachingDir = "";
        DirFileCache dirFileCache = new DirFileCache(cachingDir);
        try (Scanner scanner = new Scanner(System.in)) {
            printMenu();
            while (true) {
                if (scanner.hasNext()) {
                    String str = scanner.next();
                    if ("1".equals(str)) {
                        System.out.println("Input Dir name: ");
                        if (scanner.hasNext()) {
                            cachingDir = scanner.next();
                            if (!(cachingDir.isEmpty())) {
                                dirFileCache.putFilesToCach(cachingDir);
                            }
                        }
                        printMenu();
                    }
                    if ("3".equals(str)) {
                        System.out.println("Input file name: ");
                        /* C:\projects\job4j_grabber\src\main\java\ru\job4j\cache\menu\names.txt */
                        if (scanner.hasNext()) {
                            cachingDir = scanner.next();
                            if (!(cachingDir.isEmpty())) {
                                dirFileCache.getFilesFromCach(cachingDir);
                            }
                        }
                        printMenu();
                    }
                    /*  положить содержимое файла в кэш  */
                    if ("2".equals(str)) {
                        System.out.println("Input file name: ");
                        if (scanner.hasNext()) {
                            cachingDir = scanner.next();
                            if (!(cachingDir.isEmpty())) {
                                dirFileCache.putFilesToCach(cachingDir);
                            }
                        }
                        printMenu();
                    }
                    if ("Exit".equals(str)) {
                        return;
                    }
                }
            }
        }

    }

    public static void main(String[] args) {
        showMenu();
    }
}
