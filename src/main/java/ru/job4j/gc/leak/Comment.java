package ru.job4j.gc.leak;

import java.util.Objects;

public class Comment {
    private String text;

    private User user;

    public Comment(String text, User user) {
        this.text = text;
        this.user = user;
    }

    /*getter/setter*/

/*    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    } */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comment comment = (Comment) o;
        return Objects.equals(text, comment.text) && Objects.equals(user, comment.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, user);
    }
}
