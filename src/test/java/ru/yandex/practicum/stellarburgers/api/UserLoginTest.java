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

public class UserLoginTest {
    private UserRestClient userClient;
    private UserDto user;
    private String accessToken;

    @BeforeEach
    public void userLoginSetUp() {
        userClient = new UserRestClient();
        user = UserDataGenerator.getRandomUser();
        Response response = userClient.createUser(user);
        accessToken = response.path("accessToken");
    }

    @AfterEach
    public void userLoginTearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Успешная авторизация существующего пользователя возвращает 200")
    public void loginExistingUserReturns200Success() {
        UserDto credentials = UserDto.from(user.getEmail(), user.getPassword());
        Response response = userClient.loginUser(credentials);

        response.then().statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация с несуществующими данными пользователя возвращает 401")
    public void loginWithIncorrectCredentialsReturns401Error() {
        UserDto wrongCredentials = UserDto.from("wrong_email", "wrong_password");
        Response response = userClient.loginUser(wrongCredentials);

        response.then().statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}