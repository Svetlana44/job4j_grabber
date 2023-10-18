package ru.job4j.gc.leak;

import java.util.Random;
import java.util.Scanner;

/*
Меню. Так как задача стояла продемонстрировать утечку памяти,
 реализацию удаления каждого поста по id пропустили.
  Удаляем все сразу.
 */
public class Menu {

    public static void main(String[] args) {
        Random random = new Random();
        UserGenerator userGenerator = new UserGenerator(random);
        CommentGenerator commentGenerator = new CommentGenerator(random, userGenerator);
        Scanner scanner = new Scanner(System.in);
        PostStore postStore = new PostStore();
        Menu menu = new Menu();
        menu.start(commentGenerator, scanner, userGenerator, postStore);
    }

    private void start(CommentGenerator commentGenerator, Scanner scanner, UserGenerator userGenerator, PostStore postStore) {
        final int ADD_POST = 1;
        final int ADD_MANY_POST = 2;
        final int SHOW_ALL_POSTS = 3;
        final int DELETE_POST = 4;
        final String SELECT = "Выберите меню";
        final String COUNT = "Выберите количество создаваемых постов";
        final String TEXT_OF_POST = "Введите текст";
        final String EXIT = "Конец работы";
        final String MENU = " Введите 1 для создание поста. Введите 2, чтобы создать определенное количество постов. Введите 3, чтобы показать все посты. Введите 4, чтобы удалить все посты. Введите любое другое число для выхода. ";

        boolean run = true;
        while (run) {
            System.out.println(MENU);
            System.out.println(SELECT);
            int userChoice = Integer.parseInt(scanner.nextLine());
            System.out.println(userChoice);
            if (ADD_POST == userChoice) {
                System.out.println(TEXT_OF_POST);
                String text = scanner.nextLine();
                userGenerator.generate();
                commentGenerator.generate();
                postStore.add(new Post(text, CommentGenerator.getComments()));
            } else if (ADD_MANY_POST == userChoice) {
                System.out.println(TEXT_OF_POST);
                String text = scanner.nextLine();
                System.out.println(COUNT);
                String count = scanner.nextLine();
                for (int i = 0; i < Integer.parseInt(count); i++) {
                    createPost(commentGenerator, userGenerator, postStore, text);
                }
            } else if (SHOW_ALL_POSTS == userChoice) {
                System.out.println(postStore.getPosts());
            } else if (DELETE_POST == userChoice) {
                /*           System.out.println(ID_FOR_DELETE);   */
                postStore.removeAll();
            } else {
                run = false;
                System.out.println(EXIT);
            }
        }
    }

    private static void createPost(CommentGenerator commentGenerator, UserGenerator userGenerator, PostStore postStore, String text) {
        userGenerator.generate();
        commentGenerator.generate();
        postStore.add(new Post(text, CommentGenerator.getComments()));
    }
}
