package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    static void pageParse(int pageNumber) throws IOException {
        String page = String.format("%s%s", PAGE_LINK, pageNumber);
        Connection connection = Jsoup.connect(page);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-card__inner");
        rows.forEach(row -> {
            Element titleElement = row.select(".vacancy-card__title").first();
            Element linkElement = titleElement.child(0);
            String vacancyName = titleElement.text();
            String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
            System.out.printf("%s %s%n", vacancyName, link);

            Element date = row.select(".vacancy-card__date").first();
            String linkdate = date.child(0).attr("datetime");

            HabrCareerDateTimeParser habr = new HabrCareerDateTimeParser();

            /*           System.out.println(date);  */
            System.out.println("Значение атрибута datetime: " + linkdate
                    + System.lineSeparator()
                    + habr.parse(linkdate)
                    + System.lineSeparator()
                    + "_____________________________________________________");
        });
    }

    public static void main(String[] args) throws IOException {
        for (int i = 1; i < 6; i++) {
            pageParse(i);
        }
    }
}