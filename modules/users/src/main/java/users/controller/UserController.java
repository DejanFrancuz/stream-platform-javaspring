package users.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import users.model.User;
import users.model.UserDto;
import users.service.UserService;

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




//    @PreAuthorize("hasAuthority('can_update_users')")
    @PutMapping(value = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        Optional<User> optionalUser = userService.findById(userDto.getUserId());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPermissions(new HashSet<>(Arrays.asList(userDto.getPermissions())));

        return ResponseEntity.ok(userService.save(user));

    }

//    @PreAuthorize("hasAuthority('can_write_users')")
    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User addUser(@RequestBody UserDto user){
        HashSet<String> permissionsSet = new HashSet<>(Arrays.asList(user.getPermissions()));
        User user1 = new User(user.getFirstName(),user.getLastName(),user.getEmail(), permissionsSet);
        user1.setUsername(user.getFirstName() + user.getLastName());
        return userService.save(user1);
    }

//    @PreAuthorize("hasAuthority('can_delete_users')")
    @DeleteMapping( value = "/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id){
        this.userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
