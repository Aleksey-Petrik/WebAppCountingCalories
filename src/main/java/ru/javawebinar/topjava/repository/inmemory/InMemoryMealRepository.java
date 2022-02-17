package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.Util;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository.USER_ID;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, USER_ID));
    }

    @Override
    public Meal save(Meal meal, int idUser) {
        Map<Integer, Meal> meals = repository.computeIfAbsent(idUser, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int idUser) {
        Map<Integer, Meal> meals = repository.get(idUser);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int idUser) {
        Map<Integer, Meal> meals = repository.get(idUser);
        return meals != null ? meals.get(id) : null;
    }

    @Override
    public Collection<Meal> getAll(int idUser) {
        return getAllFiltered(idUser, meal -> true);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int idUser) {
        return getAllFiltered(idUser, meal -> Util.isBetweenHalfOpen(meal.getDateTime(), startDate, endDate));
    }

    private List<Meal> getAllFiltered(int idUser, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = repository.get(idUser);
        return meals != null ? meals.values()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList()) : Collections.emptyList();
    }
}

