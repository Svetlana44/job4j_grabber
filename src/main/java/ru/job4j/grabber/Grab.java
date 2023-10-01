/*
В этом проекты используется quartz для запуска парсера. Но не напрямую.

Абстрагируем через интерфейс.
ru.job4j.grabber.Grab
 */

package ru.job4j.grabber;

import org.quartz.SchedulerException;

public interface Grab {
    void init() throws SchedulerException;
}
