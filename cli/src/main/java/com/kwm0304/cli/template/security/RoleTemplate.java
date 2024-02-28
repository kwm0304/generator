package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;

public class RoleTemplate {
    public String genRoleEnum(String modelDirPath) {
        String rolePath = modelDirPath;
        String convertedPath = StringUtils.convertPath(rolePath);
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedPath).append(";\n\n")
                .append("public enum Role {\n")
                .append("    ").append("USER,\n")
                .append("    ").append("ADMIN\n")
                .append("}");
        return builder.toString();
    }
}
