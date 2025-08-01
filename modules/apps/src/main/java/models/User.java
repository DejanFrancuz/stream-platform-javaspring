package models;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> permissions = new HashSet<>();

    @Column
    private String password;

    @Column
    private Boolean deleted;


    public User() {
    }

    public User(String firstName,String lastName, String email, Set<String> permissions){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.permissions =  new HashSet<>(permissions);
    }

    public void addPermission(String permission) {
        if( permission == null){
            permissions = new HashSet<>();
        }
        permissions.add(permission);
    }
}
