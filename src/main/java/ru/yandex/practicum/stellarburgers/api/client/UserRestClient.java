package ru.yandex.practicum.stellarburgers.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.stellarburgers.api.dto.UserDto;

import static io.restassured.RestAssured.given;

public class UserRestClient extends BaseRestClient{

    private static final String AUTH_PATH = "/api/auth";

    @Step("Отправить запрос на регистрацию пользователя")
    public Response createUser(UserDto userDto) {
        return given()
                .spec(getBaseSpec())
                .body(userDto)
                .when()
                .post(AUTH_PATH + "/register");
    }

    @Step("Отправить запрос на логин пользователя")
    public Response loginUser(UserDto credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(AUTH_PATH + "/login");
    }

    @Step("Отправить запрос на обновление профиля пользователя")
    public Response updateUser(UserDto userDto, String token) {
        var requestSpec = given().spec(getBaseSpec());
        if (token != null) {
            requestSpec.header("Authorization", token);
        }
        return requestSpec
                .body(userDto)
                .when()
                .patch(AUTH_PATH + "/user");
    }

    @Step("Отправить запрос на удаление пользователя")
    public Response deleteUser(String token) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", token)
                .when()
                .delete(AUTH_PATH + "/user");
    }

}
