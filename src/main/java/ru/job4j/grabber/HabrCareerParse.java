package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    private final DateTimeParser dateTimeParser;

    private Post post = new Post(1, "title", "link", "description", LocalDateTime.now().withNano(0));
    public static List<Post> posts = new ArrayList<>();

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private String retrieveDescription(String link) throws IOException {
        List<String> descriptions = new ArrayList<>();
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements rows = document.select(".style-ugc");
        rows.forEach(row -> descriptions.add(row.text() + System.lineSeparator()));

        return descriptions.toString();
    }

    static List<String> pageParse(int pageNumber) throws IOException {

        HabrCareerDateTimeParser habr = new HabrCareerDateTimeParser();
        HabrCareerParse habrCareerParse = new HabrCareerParse(habr);

        List<String> links = new ArrayList<>();

        String page = String.format("%s%s", PAGE_LINK, pageNumber);
        Connection connection = Jsoup.connect(page);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-card__inner");
        rows.forEach(row -> {
            Element titleElement = row.select(".vacancy-card__title").first();
            Element linkElement = titleElement.child(0);
            String vacancyName = titleElement.text();

            habrCareerParse.post.title = vacancyName;

            String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
            links.add(link);

            habrCareerParse.post.link = link;
            habrCareerParse.post.id = Integer.parseInt(link.substring(link.lastIndexOf("/") + 1));

            System.out.printf("%s %s%n", vacancyName, link);

            Element date = row.select(".vacancy-card__date").first();
            String linkdate = date.child(0).attr("datetime");
            habrCareerParse.post.created = habr.parse(linkdate);

            /*           System.out.println(date);  */
            System.out.println("Значение атрибута datetime: " + linkdate
                    + System.lineSeparator()
                    + habr.parse(linkdate)
                    + System.lineSeparator()
                    + "_____________________________________________________");
            try {
                habrCareerParse.post.description = habrCareerParse.retrieveDescription(link);
                posts.add(habrCareerParse.post);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return links;
    }

    public static void main(String[] args) {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        System.out.println(habrCareerParse.list(PAGE_LINK));
    }

    @Override
    public List<Post> list(String link) {

        for (int i = 1; i < 6; i++) {
            try {
                pageParse(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }
}