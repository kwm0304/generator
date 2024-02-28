package com.kwm0304.cli.template.security;

public class UserFieldsTemplate {
    public String genMethods(String userClass, boolean useLombok) {
        String lowercaseUser = userClass.toLowerCase();
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append("@Enumerated(value = EnumType.STRING)\n")
                .append(tab).append("private Role role;\n\n")
                .append(tab).append("@OneToMany(mappedBy = ").append(lowercaseUser).append(")\n")
                .append(tab).append("private List<Token> tokens;\n");
        if (!useLombok) {
            builder.append(tab).append("public Role getRole() { return role; }\n")
                    .append(tab).append("public void setRole(Role role) {\n")
                    .append(tab).append(tab).append("this.role = role;\n")
                    .append(tab).append("}\n\n")
                    .append(tab).append("public List<Token> getTokens() {\n")
                    .append(tab).append(tab).append("return tokens;\n")
                    .append(tab).append("}\n\n")
                    .append(tab).append("public void setTokens(List<Token> tokens) {\n")
                    .append(tab).append(tab).append("this.tokens = tokens;\n")
                    .append(tab).append("}\n");
        }
        builder.append("}");
        return builder.toString();
    }
}
