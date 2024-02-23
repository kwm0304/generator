package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;

public class AuthControllerTemplate {
    public String genAuthController(String user, String parentDirString, String modelDir, String userClass) {
        String tab = "    ";
        String directoryPath = parentDirString;
        String convertedDirPath = StringUtils.convertPath(directoryPath);
        StringBuilder builder = new StringBuilder();
    //TODO: import and append AuthResponse model, User model, AuthService
    //imports
        builder.append("package ").append(convertedDirPath).append(".security;\n\n")
                .append("import ").append(modelDir).append(".AuthResponse;\n")
                .append("import ").append(modelDir).append(".").append(userClass).append(";\n")
                .append("import ").append(directoryPath).append(".dto.RefreshRequest;\n")
                .append("import ").append(modelDir).append(".Token;\n")
                .append("import ").append(directoryPath).append(".service.AuthService;\n")
                .append("import ").append(directoryPath).append(".service.JwtService;\n")
                .append("import ").append(directoryPath).append(".service.TokenService;\n\n")
                .append("import org.springframework.beans.factory.annotation.Autowired;\n")
                .append("import org.springframework.security.authentication.AuthenticationManager;\n")
                .append("import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;\n")
                .append("import org.springframework.security.core.Authentication;\n")
                .append("import org.springframework.security.core.userdetails.UsernameNotFoundException;\n")
                .append("import org.springframework.web.bind.annotation.RequestMapping;\n")
                .append("import org.springframework.http.ResponseEntity;\n")
                .append("import org.springframework.web.bind.annotation.PostMapping;\n")
                .append("import org.springframework.web.bind.annotation.RequestBody;\n")
                .append("import org.springframework.web.bind.annotation.RestController;\n\n")
    //class
                .append("@RestController\n")
                .append("@RequestMapping(\"/api\")\n")
                .append("public class AuthController {\n")
                .append(tab).append("@Autowired\n")
                .append(tab).append("private final AuthService authService;\n\n")
                .append(tab).append("@Autowired\n")
                .append(tab).append("private  TokenService tokenService;\n")
                .append(tab).append("@Autowired\n")
                .append("private  JwtService jwtService;\n")
                .append(tab).append("@Autowired\n")
                .append(tab).append("AuthenticationManager authenticationManager;\n\n")

    //signup
                .append(tab).append("@PostMapping(\"/register\")\n")
                .append(tab).append("public String register(\"@RequestBody \"").append(user).append(" request\");\n")
                .append(tab).append("authService.register(request);\n")
                .append(tab).append("\"User successfully registered\"\n")
                .append(tab).append("}\n\n")
    //login
                .append(tab).append("@PostMapping(\"/login\")\n")
                .append(tab).append("public AuthResponse login(@RequestBody ").append("AuthRequest request {\n")
                .append(tab).append(tab).append(tab).append("Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));\n")
                .append(tab).append(tab).append(tab).append("if (authentication.isAuthenticated()) {\n")
                .append(tab).append(tab).append(tab).append(tab).append("Token refreshToken = tokenService.createRefreshToken(request.getUsername());\n")
                .append(tab).append(tab).append(tab).append(tab).append("return new AuthResponse.Builder()\n")
                .append(tab).append(tab).append(tab).append(tab).append(tab).append(".accessToken(jwtService.generateToken(request.getUsername()))\n")
                .append(tab).append(tab).append(tab).append(tab).append(tab).append(".token(refreshToken.getToken()).build();\n")
                .append(tab).append(tab).append(tab).append("} else {\n")
                .append(tab).append(tab).append(tab).append(tab).append("throw new UsernameNotFoundException(\"Invalid user request\");")
                .append(tab).append(tab).append("}\n")
                .append(tab).append("}\n\n")
    //refresh
                .append(tab).append("@PostMapping(\"refreshToken\")\n")
                .append(tab).append("public AuthResponse refreshToken(@RequestBody RefreshRequest request) {\n")
                .append(tab).append(tab).append("return tokenService.findByToken(request.getToken());\n")
                .append(tab).append(tab).append(tab).append(".map(tokenService::verifyExpiration)\n")
                .append(tab).append(tab).append(tab).append(".map(Token::getCustomer)\n")
                .append(tab).append(tab).append(tab).append(tab).append(".map(customer -> {\n")
                .append(tab).append(tab).append(tab).append(tab).append(tab).append("String accessToken = jwtService.generateToken(customer.getUsername());\n")
                .append(tab).append(tab).append(tab).append(tab).append(tab).append("return new AuthResponse.Builder()\n ")//need to verify
                .append(tab).append(tab).append(tab).append(tab).append(tab).append(tab).append(".accessToken(accessToken)\n")
                .append(tab).append(tab).append(tab).append(tab).append(tab).append(tab).append(".token(request.getToken())\n")
                .append(tab).append(tab).append(tab).append(tab).append(tab).append(tab).append(".message(\"Refresh token processed successfully.\")\n")
                .append(tab).append(tab).append(tab).append(tab).append(tab).append(tab).append(".build();\n")
                .append(tab).append(tab).append(tab).append(tab).append("}).orElseThrow(() -> new RuntimeException(\"Invalid refresh token\"));"\n)
                .append(tab).append("}\n")
                .append("}");
        return builder.toString();
    }
}
