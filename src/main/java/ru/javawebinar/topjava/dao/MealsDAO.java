package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsDAO {

    void addMeal(Meal meal);

    void deleteMeal(int id);

    void updateMeal(Meal meal);

    List<Meal> getAllMeals();

    Meal getMealById(int id);
}
