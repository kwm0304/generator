package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;

import java.nio.file.Path;

public class LogoutHandlerTemplate {
    public String genLogoutHandler(String parentDirString, String modelDirString, boolean useLombok) {
        String packagePathString = parentDirString;
        String packagePath = StringUtils.convertPath(packagePathString);
        String directoryPath = modelDirString;
        String convertedDirPath = StringUtils.convertPath(directoryPath);
        String tokenImport = convertedDirPath + ".Token;\n";
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(packagePath).append(".config;\n\n")
                .append("import ").append(tokenImport)
                .append("import ").append(packagePath).append(".repository.TokenRepository;\n")
                .append("import jakarta.servlet.http.HttpServletRequest;\n")
                .append("import jakarta.servlet.http.HttpServletResponse;\n")
                .append("import org.springframework.context.annotation.Configuration;\n")
                .append("import org.springframework.security.core.Authentication;\n")
                .append("import org.springframework.security.web.authentication.logout.LogoutHandler;\n\n");
                if (useLombok) {
                    builder.append("@AllArgsConstructor\n");
                }
                builder.append("@Configuration\n")
                        .append("public class CustomLogoutHandler implements LogoutHandler {\n")
                        .append(tab).append("private final TokenRepository tokenRepository;\n");
                if (!useLombok) {
                    constructor();
                }
                builder.append(tab).append("@override\n")
                        .append("public void logout(HttpServletRequest request,\n")
                        .append(tab).append(tab).append(tab).append("HttpServletResponse response,\n")
                        .append(tab).append(tab).append(tab).append("Authentication authentication) {\n")
                        .append(tab).append(tab).append("String authHeader = request.getHeader(\"Authorization\");\n")
                        .append(tab).append(tab).append("if (authHeader == null || !authHeader.startsWith(\"Bearer \")) {\n")
                        .append(tab).append(tab).append(tab).append("return;\n")
                        .append(tab).append(tab).append("}\n\n")
                        .append(tab).append(tab).append("String token = authHeader.substring(7);\n")
                        .append(tab).append(tab).append("Token storedToken = tokenRepository.findByToken(token).orElse(null);\n")
                        .append(tab).append(tab).append("if (storedToken != null) {\n")
                        .append(tab).append(tab).append(tab).append("storedToken.setLoggedOut(true);\n")
                        .append(tab).append(tab).append(tab).append("tokenRepository.save(storedToken);\n")
                        .append(tab).append(tab).append("}\n")
                        .append(tab).append("}\n")
                        .append("}");
                return builder.toString();

    }

    public String constructor() {
        StringBuilder builder = new StringBuilder();
        builder.append("    public CustomLogoutHandler(TokenRepository tokenRepository) {\n")
                .append("        ").append("this.tokenRepository = tokenRepository;\n")
                .append("    }\n");
        return builder.toString();
    }
}
