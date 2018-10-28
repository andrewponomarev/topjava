package ru.javawebinar.topjava.testdata;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static Meal meal1 = new Meal(START_SEQ + 2,
            LocalDateTime.of(2015, 5, 30, 10, 0),
            "Завтрак", 500);
    public static Meal meal2 = new Meal(START_SEQ + 3,
            LocalDateTime.of(2015, 5, 30, 13, 0),
            "Обед", 1000);
    public static Meal meal3 = new Meal(START_SEQ + 4,
            LocalDateTime.of(2015, 5, 30, 20, 0),
            "Ужин", 500);
    public static Meal meal4 = new Meal(START_SEQ + 5,
            LocalDateTime.of(2015, 5, 31, 10, 0),
            "Завтрак", 1000);
    public static Meal meal5 = new Meal(START_SEQ + 6,
            LocalDateTime.of(2015, 5, 31, 13, 0),
            "Обед", 500);
    public static Meal meal6 = new Meal(START_SEQ + 7,
            LocalDateTime.of(2015, 5, 31, 20, 0),
            "Ужин", 510);
    public static Meal meal7 = new Meal(START_SEQ + 8,
            LocalDateTime.of(2015, 5, 30, 9, 0),
            "Завтрак", 510);
    public static Meal meal8 = new Meal(START_SEQ + 9,
            LocalDateTime.of(2015, 5, 30, 11, 0),
            "Завтрак", 510);

    public static List<Meal> user1meals = new LinkedList<>(
            Arrays.asList(meal1, meal2, meal3, meal4, meal5, meal6));
    public static List<Meal> user2meals = new LinkedList<> (
            Arrays.asList(meal7, meal8));

    public static final String[] FIELDS_TO_IGNORE = {};

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected,FIELDS_TO_IGNORE);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields(FIELDS_TO_IGNORE).isEqualTo(expected);
    }
}
