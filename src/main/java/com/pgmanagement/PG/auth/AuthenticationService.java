package com.pgmanagement.PG.auth;

import com.pgmanagement.PG.entity.Role;
import com.pgmanagement.PG.entity.User;
import com.pgmanagement.PG.jwtconfig.JwtService;
import com.pgmanagement.PG.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    //private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        repository.save(user);
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        /*var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);*/
        return AuthenticationResponse.builder()
                /*.accessToken(jwtToken)
                .refreshToken(refreshToken)*/
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        /*var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);*/
        return AuthenticationResponse.builder()
                /*.accessToken(jwtToken)
                .refreshToken(refreshToken)*/
                .token(jwtToken)
                .build();
    }
}
