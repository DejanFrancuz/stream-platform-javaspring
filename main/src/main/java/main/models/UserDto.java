package main.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String[] permissions;

    public UserDto(Long userId, String firstName, String lastName, String email, String[] permissions) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.permissions = permissions;
    }
}