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
                .append("import org.springframework.http.ResponseEntity;\n")
                .append("import org.springframework.web.bind.annotation.PostMapping;\n")
                .append("import org.springframework.web.bind.annotation.RequestBody;\n")
                .append("import org.springframework.web.bind.annotation.RestController;\n\n")
    //class
                .append("@RestController\n")
                .append("public class AuthController {\n")
                .append(tab).append("private final AuthService authService;\n\n")
                .append(tab).append("@Autowired\n")
                .append(tab).append("AuthenticationManager authenticationManager;\n\n")
                .append(tab).append("@Autowired\n")
                .append(tab).append("JwtService jwtService;\n\n")
                .append(tab).append("@Autowired\n")
                .append(tab).append("RefreshTokenService refreshTokenService;\n\n")
    //constructor
                .append(tab).append("public AuthController(AuthService authService) {\n")
                .append(tab).append(tab).append("this.authService = authService;\n")
                .append(tab).append("}\n\n")
    //signup
                .append(tab).append("@PostMapping(\"/api/register\")\n")
                .append(tab).append("public ResponseEntity<AuthResponse> register(\"@RequestBody \"").append(user).append(" request\"));\n")
                .append(tab).append("}\n\n")
    //login
                .append(tab).append("@PostMapping(\"/api/login\")\n")
                .append(tab).append("public ResponseEntity<AuthResponse> login(@RequestBody ").append(user).append(" request) {\n")
                .append(tab).append(tab).append("return ResponseEntity.ok(authService.authenticate(request));\n")
                .append(tab).append("}\n")
    //refresh
                .append(tab).append("@PostMapping(\"refreshtoken\")\n")
                .append(tab).append("public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {\n")
                .append(tab).append(tab).append("String requestRefreshToken = request.getRefreshToken();\n")
                .append(tab).append(tab).append("return refreshTokenService.findByToken(requestRefreshToken)\n")
                .append(tab).append(tab).append(tab).append(".map(refreshTokenService::verifyExpiration)\n")
                .append(tab).append(tab).append(tab).append(".map(RefreshToken::getUser)\n")
                .append(tab).append(tab).append(tab).append(".map(user -> {\n")
                .append(tab).append(tab).append(tab).append(tab).append("String token = jwtService.extractUsername(user.getUsername());\n ")//need to verify
                .append(tab).append(tab).append(tab).append(tab).append("return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));\n")
                .append(tab).append(tab).append(tab).append("})\n")
                .append(tab).append(tab).append(tab).append(".orElseThrow(() -> new TokenRefreshException(requestRefreshToken, \"Refresh token is not in database\");\n")
                .append("}");
        return builder.toString();
}
}
