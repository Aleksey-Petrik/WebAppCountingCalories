package ru.javawebinar.topjava.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.getEndInclusive;
import static ru.javawebinar.topjava.util.DateTimeUtil.getStartInclusive;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int idUser) {
        return repository.save(meal, idUser);
    }

    public void delete(int id, int idUser) {
        checkNotFoundWithId(repository.delete(id, idUser), id);
    }

    public Meal get(int id, int idUser) {
        return checkNotFoundWithId(repository.get(id, idUser), id);
    }

    public List<MealTo> getAll(int idUser) {
        return getTos(repository.getAll(idUser), DEFAULT_CALORIES_PER_DAY);
    }

    public void update(Meal meal, int idUser) {
        checkNotFoundWithId(repository.save(meal, idUser), meal.getId());
    }

    public List<Meal> getBetweenHalfOpen(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int idUser) {
        return repository.getBetweenHalfOpen(getStartInclusive(startDate), getEndInclusive(endDate), idUser);
    }
}