package auth.service;

import auth.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import model.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import users.model.User;
import users.service.UserService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public User login(LoginRequest req) {
        try {
            System.out.println("pucam pre authentication");
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Invalid credentials");
        }

        System.out.println("pre load user by email");
        User user = userService.loadUserByEmail(req.getUsername());  // ili loadUserByUsername
        return user;
    }

    public User getPersonData(String email){
        User user = userService.loadUserByEmail(email);  // ili loadUserByUsername
        return user;
    }

    public String generateToken(String username, Set<String> perms) {
        return jwtUtil.generateToken(username, perms);
    }
}
