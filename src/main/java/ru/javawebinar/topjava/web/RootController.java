package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class RootController {
    @Autowired
    private UserService userService;

    @Autowired
    private MealService mealService;

    @Autowired
    private MealRestController mealController;

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users";
    }

    @PostMapping("/users")
    public String setUser(HttpServletRequest request) {
        int userId = Integer.valueOf(request.getParameter("userId"));
        SecurityUtil.setAuthUserId(userId);
        return "redirect:users";
    }

    @GetMapping("/meals")
    public String meals(Model model) {
        int userId = SecurityUtil.authUserId();
        model.addAttribute("meals", MealsUtil.getWithExcess(mealService.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping("/meals/delete&{id}")
    public String delete(@PathVariable("id") int id) {
        int userId = SecurityUtil.authUserId();
        mealService.delete(id, userId);
        return "redirect:/meals";
    }

    @GetMapping("/meals/update")
    public String update(@RequestParam("id") int id, HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        Meal meal = mealService.get(id, userId);
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/meals/create")
    public String create(HttpServletRequest request) {
        final Meal meal =
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        request.setAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/meals/change")
    public String change(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        if (request.getParameter("id").isEmpty()) {
            Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime"))
                    , request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
            mealService.create(meal, userId);
        } else {
            int id = Integer.parseInt(request.getParameter("id"));
            Meal meal = new Meal(id,LocalDateTime.parse(request.getParameter("dateTime"))
                    , request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
            mealService.update(meal, userId);
        }
        return "redirect:/meals";
    }

    @PostMapping("/meals/filter")
    public String filter(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", mealController.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}
