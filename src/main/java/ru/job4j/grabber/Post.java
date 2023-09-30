/* модель данных Post:

 - id типа int - идентификатор вакансии (берется из нашей базы данных);
 - title типа String - название вакансии;
 - link типа String - ссылка на описание вакансии;
 - description типа String - описание вакансии;
 - created типа LocalDateTime - дата создания вакансии.*/
package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    int id;
    String title;
    String link;
    String description;
    LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Post{"
                + "id=" + id
                + ", title='" + title + '\''
                + ", link='" + link + '\''
                + ", description='" + description + '\''
                + ", created=" + created
                + '}';
    }
}
