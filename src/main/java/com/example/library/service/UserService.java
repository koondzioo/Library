package com.example.library.service;


import com.example.library.exception.ExceptionCode;
import com.example.library.exception.MyException;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final UserRepository userRepository;

    public Mono<User> findByUsername(String username ) {

/*        final String userUsername = "user";
        final User user = new User( userUsername, passwordEncoder.encode("user"), true, Set.of(Role.ROLE_USER) );

        final String adminUsername = "admin";
        final User admin = new User( adminUsername, passwordEncoder.encode("admin"), true, Set.of(Role.ROLE_ADMIN) );

        if ( username.equals(userUsername) ) {
            return Mono.just(user);
        }

        if ( username.equals(adminUsername) ) {
            return Mono.just(admin);
        }*/

        return Mono.error(new SecurityException("find by username exception"));
    }

    public Mono<User> registerNewUser(User user) {
        System.out.println("tutaj");
        try{
            if (!validationService.validateUser(user)) {
                throw new IllegalArgumentException("USER IS NOT CORRECT!");
            }
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }catch (Exception e)
        {
            e.printStackTrace();
            throw new MyException(ExceptionCode.SERVICE, "REGISTER USER EXCEPTION");
        }
    }
}
