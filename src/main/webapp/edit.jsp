<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Edit meals</title>
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Edit meal</h2>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">
        <table>
            <tr>
                <td>DateTime:</td>
                <td><input type="datetime-local" id="localdate" name="datetime" value="${meal.dateTime}"/></td>
            </tr>
            <tr>
                <td>Description:</td>
                <td><input type="text" name="description" size=50 value="${meal.description}"/></td>
            </tr>
            <tr>
                <td>Calories:</td>
                <td><input type="number" name="calories" size=50 value="${meal.calories}"/></td>
            </tr>
        </table>
        <br>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</body>
</html>
