package ru.yandex.practicum.stellarburgers.api;

import org.junit.jupiter.api.DisplayName;
import ru.yandex.practicum.stellarburgers.api.client.*;
import ru.yandex.practicum.stellarburgers.api.dto.*;
import ru.yandex.practicum.stellarburgers.api.utils.UserDataGenerator;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderSubmissionTest {
    private UserRestClient userClient;
    private OrderRestClient orderClient;
    private String accessToken;

    private List<String> validIngredients;

    @BeforeEach
    public void submitOrderSetUp() {
        userClient = new UserRestClient();
        orderClient = new OrderRestClient();
        accessToken = null;

        Response ingredientsResponse = orderClient.getActualIngredients();
        validIngredients = ingredientsResponse.path("data._id");
    }

    @AfterEach
    public void submitOrderTearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем возвращает 200")
    public void createOrderWithAuthorizationReturns200Success() {

        UserDto user = UserDataGenerator.getRandomUser();
        Response userResponse = userClient.createUser(user);
        accessToken = userResponse.path("accessToken");

        OrderDto order = new OrderDto(validIngredients);
        Response response = orderClient.createOrder(order, accessToken);

        response.then().statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа не авторизованным пользователем возвращает 200")
    public void createOrderWithoutAuthorizationSuccess() {
        OrderDto order = new OrderDto(validIngredients);
        Response response = orderClient.createOrder(order, null);

        response.then().statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов возвращает 400")
    public void createOrderWithoutIngredientsReturns400Error() {
        OrderDto emptyOrder = new OrderDto(Collections.emptyList());
        Response response = orderClient.createOrder(emptyOrder, accessToken);

        response.then().statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с хэшем несуществующих ингредиентов возвращает 500")
    public void createOrderWithInvalidIngredientHashReturns500Error() {
        OrderDto badHashOrder = new OrderDto(List.of("unexistent_ingredient_hash"));
        Response response = orderClient.createOrder(badHashOrder, accessToken);

        response.then().statusCode(500);
    }
}
