package services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import interfaces.IService;
import models.User;
import repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements IService<User, Long>, UserDetailsService {

    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = this.userRepository.findByEmail(username);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+username+" not found");
        }
        return new org.springframework.security.core.userdetails.User(username, myUser.getPassword(), getAuthorities(myUser.getPermissions()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<String> permissions) {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission))
                .collect(Collectors.toList());
    }


    public User loadUserByEmail(String email) throws UsernameNotFoundException {
        User myUser = this.userRepository.findByEmail(email);
        if(myUser == null) {
            throw new UsernameNotFoundException("User name "+email+" not found");
        }
        return myUser;
    }


    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getUsername()));
        return this.userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }

}
