package ru.job4j.grabber;

import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private Connection cnn;

    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    public PsqlStore(Properties cfg) {
        try (InputStream inputStream = PsqlStore.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            cfg.load(inputStream);
            this.cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Post createPost(ResultSet resultSet) throws SQLException {
        /*поля переставлены местами в БД discription и link*/
        return new Post(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(4),
                resultSet.getString(3),
                resultSet.getTimestamp(5).toLocalDateTime()
        );
    }

    @Override
    public void save(Post post) {
/*  Ссылка у вакансии уникальная и в методе save при повторном парсинге  мы будем получать исключения,
 поэтому нужно решить этот вопрос применением INSERT ON CONFLICT statement.  */
        String addPost = "INSERT into post(name,text,link,created) values(?,?,?,?) ON CONFLICT (link) DO NOTHING;";
        try (PreparedStatement preparedStatement = cnn.prepareStatement(addPost)) {
            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setString(3, post.getLink());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement preparedStatement = cnn.prepareStatement("SELECT * FROM post;")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(createPost(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(int id) {
        Post post = new Post(1, "Post did`t found.", "link", "Zerodescription", LocalDateTime.now().withNano(0));
        try (PreparedStatement preparedStatement = cnn.prepareStatement("SELECT * FROM post WHERE id=?;")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    post = createPost(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

 /*   public static void main(String[] args) {
        PsqlStore psqlStore = new PsqlStore(new Properties());
        HabrCareerDateTimeParser habr = new HabrCareerDateTimeParser();
        HabrCareerParse habrCareerParse = new HabrCareerParse(habr);
        List<Post> posts = habrCareerParse.list(PAGE_LINK);

        posts.forEach(psqlStore::save);
        System.out.println(psqlStore.findById(1000130906));
        System.out.println(psqlStore.getAll());
    } */
}