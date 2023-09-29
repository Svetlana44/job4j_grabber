package ru.job4j.grabber.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class HabrCareerDateTimeParserTest {

    @Test
    void parseDateTimeImpl() {
        String date = "2023-09-29T19:27:14+03:00";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime expected = LocalDateTime.parse("2023-09-29T19:27:14", formatter);
        HabrCareerDateTimeParser habr = new HabrCareerDateTimeParser();
        LocalDateTime actual = habr.parse(date);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void parseDateTimeImplZero() {
        String date = "";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime expected = LocalDateTime.now().withNano(0);
        HabrCareerDateTimeParser habr = new HabrCareerDateTimeParser();
        LocalDateTime actual = habr.parse(date);
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}