package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;

public class TokenTemplate {
    public String genToken(String userClass, String modelDirString, boolean useLombok) {
        String tab = "    ";
        String modelPath = modelDirString;
        String lowercaseUser = userClass.toLowerCase();
        String convertedPackage = StringUtils.convertPath(modelPath);
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedPackage).append(";\n\n")
                .append("import java.persistence.*;\n\n");
        if (useLombok) {
            builder.append("@Getter\n")
                    .append("@Setter\n");
        }
        builder.append("@Entity\n")
                .append("@Table(name = \"token\")\n")
                .append("public class Token {\n")
                .append(tab).append("@Id\n")
                .append(tab).append("@GeneratedValue(strategy = GenerationType.IDENTITY)\n")
                .append("@Column(name = \"id\")\n")
                .append(tab).append("private Integer id;\n\n")
                .append(tab).append("@Column(name = \"token\")\n")
                .append(tab).append("private String token;\n\n")
                .append(tab).append("@Column(name = \"is_logged_out\")\n\n")
                .append(tab).append("@ManyToOne\n")
                .append(tab).append("@JoinColumn(name = \"").append(lowercaseUser).append("_id)\n")
                .append(tab).append("private ").append(userClass).append(" ").append(lowercaseUser).append(";\n\n");
                if (!useLombok) {
                    builder.append(tab).append("public Integer getId() { return id; } \n")
                            .append("public void setId(Integer id) { this.id = id; }\n")
                            .append(tab).append("public String getToken() { return token; }\n")
                            .append(tab).append("public void setToken(String token) { this.token = token; }\n")
                            .append(tab).append("public boolean isLoggedOut() { return loggedOut; }\n")
                            .append(tab).append("public void setLoggedOut(boolean loggedOut) { this.loggedOut = loggedOut; } \n")
                            .append(tab).append("public ").append(userClass).append(" get").append(userClass).append("() {\n")
                            .append(tab).append(tab).append("return ").append(userClass).append(";\n")
                            .append(tab).append("}\n")
                            .append(tab).append("public void set").append(userClass).append("(").append(userClass).append(" ").append(lowercaseUser).append(") {\n")
                            .append(tab).append(tab).append("this.").append(lowercaseUser).append(" = ").append(lowercaseUser).append(";\n")
                            .append(tab).append("}");
                }
        builder.append("}");
                return builder.toString();
    }
}
