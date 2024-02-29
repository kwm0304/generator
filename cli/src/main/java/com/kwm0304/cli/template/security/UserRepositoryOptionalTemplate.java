package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;

public class UserRepositoryOptionalTemplate {
    public String genMethod(String userClass, String parentDirString, String modelDirStr, String userIdType) {
        String convertedParent = StringUtils.convertPath(parentDirString);
        String convertedModel = StringUtils.convertPath(modelDirStr);
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedParent).append(";\n\n")
                .append("import ").append(convertedModel).append(".").append(userClass).append(";\n")
                .append("import org.springframework.data.jpa.repository.JpaRepository;\n")
                .append("import java.util.Optional;\n\n")
                .append("public interface ").append(userClass).append("Repository extends JPARepository<").append(userClass).append(", ").append(userIdType).append("> {\n")
                .append("    Optional<").append(userClass).append("> findByUsername(String username);\n")
                .append("}");
        return builder.toString();
    }
}
