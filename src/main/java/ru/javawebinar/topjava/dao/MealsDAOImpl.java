package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsDAOImpl implements MealsDAO {
    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(0);

    public MealsDAOImpl() {
        List<Meal> meals = new ArrayList<>(MealsUtil.getTestData());
        meals.forEach(this::addMeal);
    }

    @Override
    public void addMeal(Meal meal) {
        if (meal.getId() == null || meal.getId() == 0) {
            meal.setId(id.incrementAndGet());
        }
        storage.put(meal.getId(), meal);
    }

    @Override
    public void deleteMeal(int id) {
        storage.remove(id);
    }

    @Override
    public void updateMeal(Meal meal) {
        storage.put(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Meal getMealById(int id) {
        return storage.get(id);
    }
}
