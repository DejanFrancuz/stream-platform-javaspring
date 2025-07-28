package auth.controller;

import auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import model.LoginRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import users.model.User;
import users.model.UserDto;

import java.time.Duration;
import java.util.Set;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<User> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        // 1) autentifikuj i uzmi user podatke
        System.out.println("ulazim " + request);
        User userDto = authService.login(request);
        System.out.println("userDto " + userDto);
        String username = userDto.getEmail(); // ili getUsername()

        System.out.println("nasao " + userDto + username);


        // 2) izvuci permisije iz User objekta
        Set<String> perms = userDto.getPermissions();

        // 3) generiši token
        String jwt = authService.generateToken(username, perms);

        // 4) postavi HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(true)           // true na HTTPS
                .path("/")
                .maxAge(Duration.ofHours(2))
                .sameSite("Strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 5) vrati korisničke podatke (token je u cookie‑ju)
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        // 1) autentifikuj i uzmi user podatke
        System.out.println("ulazim " + request);
        User userDto = authService.login(request);

        if(userDto != null){
            return  ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/get-person")
    public ResponseEntity<User> getCurrentUser(Authentication auth) {
        String username = auth.getName();

        User user = authService.getPersonData(username);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }
}
