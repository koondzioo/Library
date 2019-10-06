package com.example.library.security;

import com.example.library.model.Role;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        try {

            String authToken = authentication.getCredentials().toString();
            String username = jwtUtil.getUsernameForToken(authToken);

            if ( jwtUtil.validateToken(authToken) ) {

                Claims claims = jwtUtil.getAllClaimsForToken(authToken);
                List<String> rolesMap = claims.get("role", List.class);
                List<Role> roles = new ArrayList<>();
                for ( String rm : rolesMap ) {
                    roles.add(Role.valueOf(rm));
                }

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        roles.stream().map(r -> new SimpleGrantedAuthority(r.name())).collect(Collectors.toList())
                );
                return Mono.just(auth);
            }

            return Mono.error(() -> new IllegalStateException("authentication failed 1"));

        } catch ( Exception e ) {
            return Mono.error(() -> new IllegalStateException("authentication failed 2"));
        }
    }
}
