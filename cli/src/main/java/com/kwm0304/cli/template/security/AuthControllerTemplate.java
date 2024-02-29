package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;

public class AuthControllerTemplate {
    public String genAuthController(String parentDirString, String userClass, boolean useLombok, String modelDirString) {
        String convertedParent = StringUtils.convertPath(parentDirString);
        String convertedModel = StringUtils.convertPath(modelDirString);
        String convertedController = convertedParent + ".controller";
        String convertedService = convertedParent + ".service";
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedController).append(";\n\n")
                .append("import ").append(convertedModel).append(".AuthResponse;\n")
                .append("import ").append(convertedModel).append(".").append(userClass).append(";\n")
                .append("import ").append(convertedService).append(".AuthService;\n")
                .append("import org.springframework.http.ResponseEntity;\n")
                .append("import org.springframework.web.bind.annotation.PostMapping;\n")
                .append("import org.springframework.web.bind.annotation.RequestBody;\n")
                .append("import org.springframework.web.bind.annotation.RestController;\n\n");
                if (useLombok) {
                    builder.append("@AllArgsConstructor\n");
                }
                builder.append("@RestController\n")
                .append("public class AuthController {\n")
                .append(tab).append("private final AuthService authService;\n");
                if (!useLombok) {
                    builder.append(tab).append("public AuthController(AuthService authService) {\n")
                            .append(tab).append(tab).append("this.authService = authService;\n")
                            .append(tab).append("}\n");
                }
        builder.append(tab).append("@PostMapping(\"/register\")\n")
                .append(tab).append("public ResponseEntity<AuthResponse> register(@RequestBody ").append(userClass).append(" request) {\n")
                .append(tab).append(tab).append("return ResponseEntity.ok(authService.register(request));\n")
                .append(tab).append("}\n\n")
                .append(tab).append("@PostMapping(\"/login\")\n")
                .append(tab).append("public ResponseEntity<AuthResponse> login(@RequestBody ").append(userClass).append(" request) {\n")
                .append(tab).append(tab).append("return ResponseEntity.ok(authService.authenticate(request));\n")
                .append(tab).append("}\n")
                .append("}");
                return builder.toString();
    }
}
