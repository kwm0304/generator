package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplTemplate {
    public String genUserDetailsService(String parentDir, String userClass, boolean useLombok) {
        String convertedParent = StringUtils.convertPath(parentDir);
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedParent).append(".service;\n\n")
                .append("import ").append(convertedParent).append(".repository.").append(userClass).append("Repository;\n")
                .append("import org.springframework.security.core.userdetails.UserDetails;\n")
                .append("import org.springframework.security.core.userdetails.UserDetailsService;\n")
                .append("import org.springframework.security.core.userdetails.UsernameNotFoundException;\n")
                .append("import org.springframework.stereotype.Service;\n\n");
            if (useLombok) {
                builder.append("@AllArgsConstructor\n");
            }
            builder.append("private final UserRepository;\n");
                    if (!useLombok) {
                        builder.append("public UserDetailsServiceImpl(").append(userClass).append("Repository userRepository) { this.userRepository = userRepository; }\n");
                    }
                    builder.append(tab).append("@Override\n")
                            .append(tab).append("public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {\n")
                            .append(tab).append(tab).append("return userRepository.findByUsername(username)\n")
                            .append(tab).append(tab).append(tab).append(".orElseThrow(() -> new UsernameNotFoundException(\"User not found\"));\n")
                            .append(tab).append("}\n")
                            .append("}");
                    return builder.toString();
    }
}
