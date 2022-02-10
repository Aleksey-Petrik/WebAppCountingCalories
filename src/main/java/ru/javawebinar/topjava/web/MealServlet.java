package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealsDAO;
import ru.javawebinar.topjava.dao.MealsDAOImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.CALORIES_PER_DATE;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealsDAO repository;

    @Override
    public void init() throws ServletException {
        repository = new MealsDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        List<MealTo> meals = MealsUtil.getMealTos(repository.getAllMeals(), CALORIES_PER_DATE);

        String id = request.getParameter("id");
        String action = request.getParameter("action");

        if (action == null) {
            log.info("Get all");
            request.setAttribute("meals", meals);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        Meal meal = null;
        int idInt = Integer.parseInt(id);
        switch (action) {
            case "delete":
                log.info("Delete meal id {}", idInt);
                repository.deleteMeal(idInt);
                response.sendRedirect("meals");
                return;
            case "edit":
                meal = repository.getMealById(idInt);
                log.info("Edit meal id {}", meal);
                break;
            case "add":
                meal = new Meal();
                log.info("Add new meal");
                break;
        }

        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String calories = request.getParameter("calories");
        String description = request.getParameter("description");
        String dateTime = request.getParameter("datetime");

        if (calories != null && description != null && dateTime != null) {
            Meal meal = new Meal(id.isEmpty() ? 0 : Integer.parseInt(id), LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            repository.addMeal(meal);
        }
        response.sendRedirect("meals");
    }
}
