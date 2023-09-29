package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {

    /* Реализовать метод, преобразующий дату из формата career.habr.com, к виду "yyyy-mm-ddТhh:mm:ss" */
    @Override
    public LocalDateTime parse(String parse) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        if (parse.isEmpty()) {
            return LocalDateTime.now().withNano(0);
        }
        String subStr = parse.substring(0, 19);

        return LocalDateTime.parse(subStr, formatter);
    }
}
