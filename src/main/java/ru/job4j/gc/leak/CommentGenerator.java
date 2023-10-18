package ru.job4j.gc.leak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/*
Генератор комментариев. Также при создании заполним список фразами,
 а при вызове generate зачистим список,
  а затем сгенерируем 50 комментариев из случайных фраз.
 */
public class CommentGenerator implements Generate {

    private static List<Comment> comments = new ArrayList<>();
    private static List<String> phrases;
    private UserGenerator userGenerator;
    private Random random;

    public CommentGenerator(Random random, UserGenerator userGenerator) {
        this.userGenerator = userGenerator;
        this.random = random;
        read();
    }

    private void read() {
        final String PATH_PHRASES = "src/main/java/ru/job4j/gc/leak/files/phrases.txt";
        try {
            phrases = read(PATH_PHRASES);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static List<Comment> getComments() {
        return comments;
    }

    @Override
    public void generate() {
        final String SEPARATOR = System.lineSeparator();
        final int COUNT = 50;

        comments.clear();
        for (int i = 0; i < COUNT; i++) {
            String comment = String.format("%s%s%s%s%s",
                    phrases.get(random.nextInt(phrases.size())),
                    SEPARATOR, phrases.get(random.nextInt(phrases.size())),
                    SEPARATOR, phrases.get(random.nextInt(phrases.size())));

            comments.add(new Comment(comment, userGenerator.randomUser()));
        }
    }
}
