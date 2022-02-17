package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        int userId = authUserId();
        checkNew(meal);
        log.info("create new meal {} user id {}", meal, userId);
        return service.create(meal, userId);
    }

    public void delete(int id) {
        int userId = authUserId();
        log.info("delete meal id {} user id {}", id, userId);
        service.delete(id, userId);
    }

    public Meal get(int id) {
        int userId = authUserId();
        log.info("get meal id {} user id {}", id, userId);
        return service.get(id, userId);
    }

    public List<MealTo> getAll() {
        int userId = authUserId();
        log.info("get all meal user id {}", userId);
        return service.getAll(userId);
    }

    public void update(Meal meal, int id) {
        int userId = authUserId();
        assureIdConsistent(meal, id);
        log.info("update meal {} for user id {}", meal, userId);
        service.update(meal, userId);
    }

    public List<MealTo> getBetweenHalfOpen(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                           @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        int userId = authUserId();
        log.info("getBetween dates ({} - {}) time ({} - {})", startDate, endDate, startTime, endTime);

        List<Meal> mealDateFiltered = service.getBetweenHalfOpen(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(mealDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}