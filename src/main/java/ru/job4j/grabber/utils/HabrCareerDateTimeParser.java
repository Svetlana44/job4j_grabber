package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {

    /* Реализовать метод, преобразующий дату из формата career.habr.com, к виду "yyyy-mm-ddТhh:mm:ss" */
    @Override
    public LocalDateTime parse(String parse) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        if (parse.isEmpty()) {
            return LocalDateTime.now().withNano(0);
        }

        return LocalDateTime.parse(parse, formatter);
    }
}
