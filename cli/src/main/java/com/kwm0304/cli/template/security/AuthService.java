package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public String genAuthService(String parentDirString, String modelDir, String userClass, boolean useLombok) {
        String convertedService = StringUtils.convertPath(parentDirString) + ".service";
        String convertedModel = StringUtils.convertPath(modelDir);
        String convertedRepository = StringUtils.convertPath(parentDirString) + ".repository";
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedService).append(";\n\n")
                .append("import ").append(convertedModel).append(".AuthResponse;\n")
                .append("import ").append(convertedModel).append(".Token;\n")
                .append("import ").append(convertedModel).append(".").append(userClass).append(";\n")
                .append("import ").append(convertedRepository).append(".TokenRepository;\n")
                .append("import ").append(convertedRepository).append(".").append(userClass).append("Repository;\n")
                .append("import org.springframework.security.authentication.AuthenticationManager;\n")
                .append("import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;\n")
                .append("import org.springframework.security.crypto.password.PasswordEncoder;\n")
                .append("import org.springframework.stereotype.Service;\n")
                .append("import java.util.List;\n\n");
        if (useLombok) {
            builder.append("@AllArgsConstructor\n");
        }
        builder.append("@Service\n")
                .append(tab).append("public class AuthService {\n")
                .append(tab).append("private final ").append(userClass).append("Repository userRepository;\n")
                .append(tab).append("private final PasswordEncoder passwordEncoder;\n")
                .append(tab).append("private final JwtService jwtService;\n")
                .append(tab).append("private final TokenRepository tokenRepository;\n")
                .append(tab).append("private final AuthenticationManager authenticationManager;\n");
        if (!useLombok) {
            builder.append(tab).append("public AuthService(").append(userClass).append("Repository userRepository,\n")
                    .append(tab).append(tab).append(tab).append(tab).append("PasswordEncoder passwordEncoder,\n")
                    .append(tab).append(tab).append(tab).append(tab).append("JwtService jwtService,\n")
                    .append(tab).append(tab).append(tab).append(tab).append("TokenRepository tokenRepository,\n")
                    .append(tab).append(tab).append(tab).append(tab).append("AuthenticationManager authenticationManager) {\n")
                    .append(tab).append(tab).append("this.userRepository = userRepository;\n")
                    .append(tab).append(tab).append("this.passwordEncoder = passwordEncoder;\n")
                    .append(tab).append(tab).append("this.jwtService = jwtService;\n")
                    .append(tab).append(tab).append("this.tokenRepository = tokenRepository;\n")
                    .append(tab).append(tab).append("this.authenticationManager = authenticationManager;\n")
                    .append(tab).append("}\n\n");
        }
        builder.append(tab).append("public AuthResponse register(").append(userClass).append(" request) {\n")
                .append(tab).append(tab).append("if (userRepository.findByUsername(request.getUsername()).isPresent()) {\n")
                .append(tab).append(tab).append(tab).append("return new AuthResponse(null, \"User already exists\");\n")
                .append(tab).append(tab).append("}\n\n")
                .append(tab).append(tab).append(userClass).append(" user = new ").append(userClass).append("();\n")
                .append(tab).append(tab).append("user.setUsername(request.getUsername());\n")
                .append(tab).append(tab).append("user.setPassword(passwordEncoder.encode(request.getPassword()));\n")
                //TODO: loop through and set other user fields
                .append(tab).append(tab).append("user.setRole(request.getRole());\n")
                .append(tab).append(tab).append("user = userRepository.save(user);\n")
                .append(tab).append(tab).append("String jwt = jwtService.generateToken(user);\n")
                .append(tab).append(tab).append("saveUserToken(jwt, user);\n")
                .append(tab).append(tab).append("return new AuthResponse(jwt, \"").append(userClass).append(" registration successful\");\n")
                .append(tab).append("}\n\n")
                .append(tab).append("public AuthResponse authenticate (").append(userClass).append(" request) {\n")
                .append(tab).append(tab).append("authenticationManager.authenticate(\n")
                .append(tab).append(tab).append(tab).append("new UsernamePasswordAuthenticationToken(\n")
                .append(tab).append(tab).append(tab).append(tab).append("request.getUsername(),\n")
                .append(tab).append(tab).append(tab).append(tab).append("request.getPassword()\n")
                .append(tab).append(tab).append(tab).append(")\n")
                .append(tab).append(tab).append(");\n")
                .append(tab).append(tab).append(userClass).append(" user = userRepository.findByUsername(request.getUsername()).orElseThrow();\n")
                .append(tab).append(tab).append("String jwt = jwtService.generateToken(user);\n")
                .append(tab).append(tab).append("revokeAllTokensByUser(user);\n")
                .append(tab).append(tab).append("saveUserToken(jwt, user);\n\n")
                .append(tab).append(tab).append("return new AuthResponse(jwt, \"User login successful\");\n")
                .append(tab).append("}\n\n")
                .append(tab).append("private void revokeAllTokensByUser(").append(userClass).append(" user) {\n")
                .append(tab).append(tab).append("List<Token> validTokens = tokenRepository.findAllTokensByUser(user.getId());\n")
                .append(tab).append(tab).append("if (validTokens.isEmpty()) {\n")
                .append(tab).append(tab).append(tab).append("return;\n")
                .append(tab).append(tab).append("}\n\n")
                .append(tab).append(tab).append("validTokens.forEach(t -> {\n")
                .append(tab).append(tab).append(tab).append("t.setLoggedOut(true);\n")
                .append(tab).append(tab).append("});\n\n")
                .append(tab).append(tab).append("tokenRepository.saveAll(validTokens);\n")
                .append(tab).append("}\n\n")
                .append(tab).append("public void saveUserToken(String jwt, ").append(userClass).append(" user) {\n")
                .append(tab).append(tab).append("Token token = new Token();\n")
                .append(tab).append(tab).append("token.setToken(jwt);\n")
                .append(tab).append(tab).append("token.setLoggedOut(false);\n")
                .append(tab).append(tab).append("token.set").append(userClass).append("(user);\n")
                .append(tab).append(tab).append("tokenRepository.save(token);\n")
                .append(tab).append("}\n")
                .append("}");
        return builder.toString();
    }
}
