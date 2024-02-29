package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CorsConfigTemplate {
    public String genCorsConfig(String parentDirString) {
        String convertedParent = StringUtils.convertPath(parentDirString);
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedParent).append(";\n\n")
                .append("import org.springframework.context.annotation.Bean;\n")
                .append("import org.springframework.context.annotation.Configuration;\n")
                .append("import org.springframework.web.cors.CorsConfiguration;\n")
                .append("import org.springframework.web.cors.CorsConfigurationSource;\n")
                .append("import org.springframework.web.cors.UrlBasedCorsConfigurationSource;\n\n")
                .append("@Configuration\n")
                .append("public class CorsConfig {\n")
                .append(tab).append("@Bean\n")
                .append(tab).append("public CorsConfigurationSource corsConfigurationSource() {\n")
                .append(tab).append(tab).append("CorsConfiguration configuration = new CorsConfiguration();\n")
                .append(tab).append(tab).append("configuration.setAllowCredentials(true);\n")
                .append(tab).append(tab).append("configuration.addAllowedOrigin(\"your frontend url\");\n")
                .append(tab).append(tab).append("configuration.addAllowedMethod(\"*\");\n")
                .append(tab).append(tab).append("configuration.addExposedHeader(\"*\");\n")
                .append(tab).append(tab).append("UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();\n")
                .append(tab).append(tab).append("source.registerCorsConfiguration(\"/**\", configuration);\n")
                .append(tab).append(tab).append("return source;\n")
                .append(tab).append("}\n")
                .append("}");
        return builder.toString();
    }
}
