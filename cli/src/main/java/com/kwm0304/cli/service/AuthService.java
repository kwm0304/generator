package com.kwm0304.cli.service;

import com.kwm0304.cli.entity.AuthResponse;
import com.kwm0304.cli.entity.Token;
import com.kwm0304.cli.entity.Customer;
import com.kwm0304.cli.repository.TokenRepository;
import com.kwm0304.cli.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
    public class AuthService {
    private final CustomerRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    public AuthService(CustomerRepository userRepository,
                PasswordEncoder passwordEncoder,
                JwtService jwtService,
                TokenRepository tokenRepository,
                AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(Customer request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthResponse(null, "User already exists");
        }

        Customer user = new Customer();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user = userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        saveUserToken(jwt, user);
        return new AuthResponse(jwt, "Customer registration successful");
    }

    public AuthResponse authenticate (Customer request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        Customer user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(user);
        revokeAllTokensByUser(user);
        saveUserToken(jwt, user);

        return new AuthResponse(jwt, "User login successful");
    }

    private void revokeAllTokensByUser(Customer user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());
        if (validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t -> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }

    public void saveUserToken(String jwt, Customer user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setCustomer(user);
        tokenRepository.save(token);
    }
}