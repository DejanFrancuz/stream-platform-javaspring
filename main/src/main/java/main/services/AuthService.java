package main.services;

import lombok.RequiredArgsConstructor;
import main.models.LoginRequest;
import main.models.User;
import main.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public User login(LoginRequest req) {
        try {
            System.out.println("pucam pre authentication" +req);
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Invalid credentials" + ex.fillInStackTrace());
        }

        System.out.println("pre load user by email");
        User user = userService.loadUserByEmail(req.getEmail());
        return user;
    }

    public User getPersonData(String email){
        User user = userService.loadUserByEmail(email);  // ili loadUserByUsername
        return user;
    }
//
//    public User register(LoginRequest req){
//
//    }

    public String generateToken(String username, Set<String> perms) {
        return jwtUtil.generateToken(username, perms);
    }
}
