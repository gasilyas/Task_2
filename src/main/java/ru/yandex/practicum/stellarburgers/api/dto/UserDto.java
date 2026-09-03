package ru.yandex.practicum.stellarburgers.api.dto;

public class UserDto {
    private String email;
    private String password;
    private String name;

    public UserDto() {}

    public UserDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserDto from(String email, String password) {
        UserDto dto = new UserDto();
        dto.setEmail(email);
        dto.setPassword(password);
        return dto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
