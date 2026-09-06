package ru.yandex.practicum.stellarburgers.api.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseRestClient {

    protected static final String BASE_URL = "https://qa-stellarburgers.education-services.ru";
    protected RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured()) // Добавляет логи запросов/ответов прямо в шаги Allure
                .build();
    }
}
