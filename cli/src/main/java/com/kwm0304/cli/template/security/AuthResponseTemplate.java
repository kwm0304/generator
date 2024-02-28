package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;

public class AuthResponseTemplate {
    public String genAuthResponse(String modelDirPath, boolean useLombok) {
        String convertedPath = StringUtils.convertPath(modelDirPath);
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedPath).append(";\n\n");
                if (useLombok) {
                    builder.append("@AllArgsConstructor\n")
                            .append("@Getter\n");
                }
                builder.append("public class AuthResponse {\n")
                        .append(tab).append("private String token;\n")
                        .append(tab).append("private String message;\n");
                if (!useLombok) {
                    builder.append(tab).append("public AuthResponse(String token, String message) {\n")
                            .append(tab).append(tab).append("this.token = token;\n")
                            .append(tab).append(tab).append("this.message = message;\n")
                            .append(tab).append("}\n\n")
                            .append(tab).append("public String getToken() { return token; }\n")
                            .append(tab).append("public String getMessage() { return message; }\n");
                }
        builder.append("}");
                return builder.toString();

    }
}
