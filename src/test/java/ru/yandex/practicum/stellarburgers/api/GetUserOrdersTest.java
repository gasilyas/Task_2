package ru.yandex.practicum.stellarburgers.api;

import org.junit.jupiter.api.DisplayName;
import ru.yandex.practicum.stellarburgers.api.client.*;
import ru.yandex.practicum.stellarburgers.api.dto.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.stellarburgers.api.utils.UserDataGenerator;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetUserOrdersTest {
    private UserRestClient userClient;
    private OrderRestClient orderClient;
    private String accessToken;

    @BeforeEach
    public void getUserOrdersSetUp() {
        userClient = new UserRestClient();
        orderClient = new OrderRestClient();

        UserDto user = UserDataGenerator.getRandomUser();
        Response response = userClient.createUser(user);
        accessToken = response.path("accessToken");

        Response ingredientsResponse = orderClient.getActualIngredients();
        List<String> validIngredients = ingredientsResponse.path("data._id");

        OrderDto order = new OrderDto(validIngredients);
        orderClient.createOrder(order, accessToken);
    }

    @AfterEach
    public void getUserOrdersTearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Успешное получение списка заказов авторизованным пользователем возвращает 200")
    public void getOrdersByAuthorizedUserReturns200Success() {
        Response response = orderClient.getUserOrders(accessToken);

        response.then().statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение списка заказов не авторизованным пользователем возвращает 401")
    public void getOrdersByNonAuthorizedUserReturns401Error() {
        Response response = orderClient.getUserOrders(null);

        response.then().statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
