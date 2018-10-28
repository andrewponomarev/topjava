package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepositoryImpl;
import ru.javawebinar.topjava.testdata.UserTestData;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.testdata.MealTestData.*;
import static ru.javawebinar.topjava.testdata.UserTestData.ADMIN;
import static ru.javawebinar.topjava.testdata.UserTestData.USER;
import static ru.javawebinar.topjava.testdata.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(meal1.getId(), USER_ID);
        assertMatch(meal, meal1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1, 1);
    }

    @Test
    public void delete() {
        service.delete(meal1.getId(), USER_ID);
        List<Meal> userMeals = new LinkedList<>(user1meals);
        userMeals.remove(meal1);
        assertMatch(service.getAll(USER_ID), userMeals);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(1,1);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> all = service.getBetweenDates(
                LocalDate.of(2015, 5, 30),
                LocalDate.of(2015, 5, 30),
                USER_ID);
        assertMatch(all, meal1, meal2, meal3);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> all = service.getBetweenDateTimes(
                LocalDateTime.of(2015, 5, 30, 9, 0),
                LocalDateTime.of(2015, 5, 30, 21, 0),
                USER_ID);
        assertMatch(all, meal1, meal2, meal3);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, user1meals);
    }

    @Test
    public void update() {
        Meal updated = new Meal(meal1);
        updated.setCalories(123456);
        service.update(updated, USER_ID);
        assertMatch(service.get(meal1.getId(), USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        service.delete(1,1);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2016, 5, 30, 10, 0),
                "Завтрак", 500);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        List<Meal> userMeals = new LinkedList<>(user1meals);
        userMeals.add(newMeal);
        assertMatch(service.getAll(USER_ID), userMeals);
    }
}