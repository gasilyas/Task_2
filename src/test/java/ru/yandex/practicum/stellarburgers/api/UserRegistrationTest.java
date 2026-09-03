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

public class UserRegistrationTest {
    private UserRestClient userClient;
    private String accessToken;

    @BeforeEach
    public void createUserSetUp() {
        userClient = new UserRestClient();
    }

    @AfterEach
    public void createUserTearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Успешное создание уникального пользователя возвращает 200")
    public void createUniqueUserReturns200Success() {
        UserDto user = UserDataGenerator.getRandomUser();
        Response response = userClient.createUser(user);

        response.then().statusCode(200)
                .body("success", equalTo(true));

        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Создание не уникального пользователя возвращает 403")
    public void createExistingUserReturns403Error() {
        UserDto user = UserDataGenerator.getRandomUser();
        Response response1 = userClient.createUser(user);
        accessToken = response1.path("accessToken");

        Response response2 = userClient.createUser(user);
        response2.then().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного атрибута возвращает 403")
    public void createUserWithoutRequiredFieldReturns403Error() {

        UserDto userWithoutName = UserDataGenerator.getRandomUser();
        userWithoutName.setName(null);

        Response response = userClient.createUser(userWithoutName);
        response.then().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
