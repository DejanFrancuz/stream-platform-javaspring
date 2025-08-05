package main.controllers;

import main.models.User;
import main.models.UserDto;
import main.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @PreAuthorize("hasAuthority('can_read_users')")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers(){
        return userService.findAll();
    };

    @GetMapping(value = "/getone",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<User> getUser(@RequestParam("id") Long id){
        return userService.findById(id);
    };

    @GetMapping(value = "/get-user-by-email",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByEmail(@RequestParam("email") String email){
        return userService.loadUserByEmail(email);
    };




    @PutMapping(value = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User addUser(@RequestBody UserDto userDto){
        HashSet<String> permissionsSet = new HashSet<>();
        permissionsSet.add("Member");
        User user = new User(userDto.getFirstName(),userDto.getLastName(),userDto.getEmail(), permissionsSet);
        user.setUsername(userDto.getFirstName() + userDto.getLastName());
        return userService.save(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping( value = "/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id){
        this.userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
