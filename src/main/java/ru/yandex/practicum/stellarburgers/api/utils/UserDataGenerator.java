package ru.yandex.practicum.stellarburgers.api.utils;

import ru.yandex.practicum.stellarburgers.api.dto.UserDto;

import java.util.UUID;

public class UserDataGenerator {
    public static UserDto getRandomUser() {
        String uniqueUserId = UUID.randomUUID().toString().substring(0, 8);
        return new UserDto(
                "practicum_tester_" + uniqueUserId + "@yandex.ru",
                "qwerty123_" + uniqueUserId,
                "Arkadiy_Volozh_" + uniqueUserId
        );
    }
}
