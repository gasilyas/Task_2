package ru.yandex.practicum.stellarburgers.api;

import org.junit.jupiter.api.DisplayName;
import ru.yandex.practicum.stellarburgers.api.client.UserRestClient;
import ru.yandex.practicum.stellarburgers.api.dto.UserDto;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.stellarburgers.api.utils.UserDataGenerator;
import static org.hamcrest.Matchers.equalTo;

public class UserUpdateTest {
    private UserRestClient userClient;
    private UserDto user;
    private String accessToken;

    @BeforeEach
    public void updateUserSetUp() {
        userClient = new UserRestClient();
        user = UserDataGenerator.getRandomUser();
        Response response = userClient.createUser(user);
        accessToken = response.path("accessToken");
    }

    @AfterEach
    public void updateUserTearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Успешное обновление пользователя возвращает 200")
    public void updateAuthorizedUserFieldsReturns200Success() {

        UserDto freshData = UserDataGenerator.getRandomUser();
        UserDto updatedData = new UserDto();
        updatedData.setName(freshData.getName());
        updatedData.setEmail(freshData.getEmail());

        Response response = userClient.updateUser(updatedData, accessToken);
        response.then().statusCode(200)
                .body("success", equalTo(true))
                .body("user.name", equalTo(freshData.getName()));

        user.setEmail(updatedData.getEmail());
    }

    @Test
    @DisplayName("Обновление неавторизованного пользователя возвращает 401")
    public void updateNonAuthorisedUserReturns401Error() {
        UserDto freshData = UserDataGenerator.getRandomUser();
        UserDto updatedData = new UserDto();
        updatedData.setName(freshData.getName());

        Response response = userClient.updateUser(updatedData, null);
        response.then().statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}