package com.kwm0304.cli.template.security;

import org.springframework.stereotype.Service;

@Service
public class UserFieldsTemplate {
    public String genSecurityMethods() {
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append("\n" +
                "    @Override\n" +
                "    public boolean isAccountNonExpired() {\n" +
                "        return true;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public boolean isAccountNonLocked() {\n" +
                "        return true;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public boolean isCredentialsNonExpired() {\n" +
                "        return true;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public boolean isEnabled() {\n" +
                "        return true;\n" +
                "    }\n" +
                "\n" +
                "    public void setUsername(String username) {\n" +
                "        this.username = username;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public Collection<? extends GrantedAuthority> getAuthorities() {\n" +
                "        return List.of(new SimpleGrantedAuthority(role.name()));\n" +
                "    }\n")
                .append("}");
        return builder.toString();
    }

    public String genRoleEnum(String userClass, boolean useLombok) {
        String lowercaseUser = userClass.toLowerCase();
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append("@Enumerated(value = EnumType.STRING)\n")
                .append(tab).append("private Role role;\n")
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
        return builder.toString();
    }
}
