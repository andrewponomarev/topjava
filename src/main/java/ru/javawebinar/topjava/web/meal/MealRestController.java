package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    @Autowired
    private MealServiceImpl service;

    public List<MealWithExceed> getAllUserMeals() {
        return MealsUtil.getWithExceeded(
                service.getMealsByUserId(authUserId()),
                DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealWithExceed> getUserMealFilteredByDate(LocalTime startTime, LocalTime endTime) {
        return MealsUtil.getFilteredWithExceeded(
                service.getMealsByUserId(authUserId()),
                DEFAULT_CALORIES_PER_DAY,
                startTime,
                endTime
        );
    }

    public Meal get(int id) {
        return service.get(id);
    }

    public void save(Meal meal) {
        service.update(meal);
    }

    public void delete(int id) {
        service.delete(id);
    }

    public List<MealWithExceed> getAll() {
        return MealsUtil.getWithExceeded(
                service.getAll(),
                DEFAULT_CALORIES_PER_DAY);
    }







}