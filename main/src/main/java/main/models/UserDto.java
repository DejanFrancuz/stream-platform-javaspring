package main.models;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<String> permissions;

    public UserDto(Long userId, String firstName, String lastName, String email, Set<String> permissions) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.permissions = new HashSet<>(permissions);
    }
}