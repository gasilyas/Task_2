package ru.yandex.practicum.stellarburgers.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.stellarburgers.api.dto.OrderDto;

import static io.restassured.RestAssured.given;

public class OrderRestClient extends BaseRestClient{
    private static final String ORDERS_PATH = "/api/orders";
    private static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Получить список всех доступных ингредиентов")
    public Response getActualIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH);
    }

    @Step("Отправить запрос на создание заказа")
    public Response createOrder(OrderDto orderDto, String token) {
        var requestSpec = given().spec(getBaseSpec());
        if (token != null) {
            requestSpec.header("Authorization", token);
        }
        return requestSpec
                .body(orderDto)
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Отправить запрос на получение истории заказов пользователя")
    public Response getUserOrders(String token) {
        var requestSpec = given().spec(getBaseSpec());
        if (token != null) {
            requestSpec.header("Authorization", token);
        }
        return requestSpec
                .when()
                .get(ORDERS_PATH);
    }
}
