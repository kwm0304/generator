package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;

public class SecurityConfigTemplate {
    public String genSecurityConfig(String parentDirString, boolean useLombok) {
        String directoryString = parentDirString;
        String convertedString = StringUtils.convertPath(directoryString);
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedString).append(".config;\n\n")
                .append("import ").append(convertedString).append(".filter.JwtAuthFilter;\n")
                .append("import ").append(convertedString).append("service.UserDetailsServiceImpl;\n")
                .append("import org.springframework.context.annotation.Bean;\n")
                .append("import org.springframework.context.annotation.Configuration;\n")
                .append("import org.springframework.http.HttpStatus;\n")
                .append("import org.springframework.security.authentication.AuthenticationManager;\n")
                .append("import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;\n")
                .append("import org.springframework.security.config.annotation.web.builders.HttpSecurity;\n")
                .append("import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;\n")
                .append("import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;\n")
                .append("import org.springframework.security.config.http.SessionCreationPolicy;\n")
                .append("import org.springframework.security.core.context.SecurityContextHolder;\n")
                .append("import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;\n")
                .append("import org.springframework.security.crypto.password.PasswordEncoder;\n")
                .append("import org.springframework.security.web.SecurityFilterChain;\n")
                .append("import org.springframework.security.web.authentication.HttpStatusEntryPoint;\n")
                .append("import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;\n\n");
                if (useLombok) {
                    builder.append("@AllArgsConstructor\n");
                }
                builder.append("@Configuration\n")
                        .append("@EnableWebSecurity\n")
                        .append("public class SecurityConfig {\n")
                        .append(tab).append("private final UserDetailsServiceImpl userDetailsServiceImpl;\n")
                        .append(tab).append("private final JwtAuthFilter jwtAuthFilter;\n")
                        .append(tab).append("private final CustomLogoutHandler logoutHandler;\n\n");
                        if (!useLombok) {
                            builder.append(tab).append("public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl,\n")
                                    .append(tab).append(tab).append(tab).append("JwtAuthFilter jwtAuthFilter,\n")
                                    .append(tab).append(tab).append(tab).append("CustomLogoutHandler logoutHandler) {\n")
                                    .append(tab).append(tab).append("this.userDetailsServiceImpl = userDetailsServiceImpl;\n")
                                    .append(tab).append(tab).append("this.jwtAuthFilter = jwtAuthFilter;\n")
                                    .append(tab).append(tab).append("this.logoutHandler = logoutHandler;\n")
                                    .append(tab).append("}\n\n");
                        }
                        builder.append(tab).append("@Bean\n")
                                .append(tab).append("SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {\n")
                                .append(tab).append(tab).append("return http\n")
                                .append(tab).append(tab).append(".csrf(AbstractHttpConfigurer::disable)\n")
                                .append(tab).append(tab).append(tab).append(".authorizeHttpRequests(\n")
                                .append(tab).append(tab).append(tab).append(tab).append("req->req.requestMatchers(\"/login/**\",\"/register/**\")\n")
                                .append(tab).append(tab).append(tab).append(tab).append(tab).append(".permitAll()\n")
                                .append(tab).append(tab).append(tab).append(tab).append(tab).append(".requestMatchers(\"/admin/**\").hasAuthority(\"ADMIN\")\n")
                                .append(tab).append(tab).append(tab).append(tab).append(tab).append(".anyRequest()\n")
                                .append(tab).append(tab).append(tab).append(tab).append(tab).append(".authenticated()\n")
                                .append(tab).append(tab).append(tab).append(tab).append(").userDetailsService(userDetailsServiceImpl)\n")
                                .append(tab).append(tab).append(tab).append(tab).append(".sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))\n")
                                .append(tab).append(tab).append(tab).append(tab).append(".addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)\n")
                                .append(tab).append(tab).append(tab).append(tab).append(".exceptionHandling(\n")
                                .append(tab).append(tab).append(tab).append(tab).append(tab).append("e->e.accessDeniedHandler(\n")
                                .append(tab).append(tab).append(tab).append(tab).append(tab).append(tab).append("(request, response, accessDeniedException)->response.setStatus(403)\n")
                                .append(tab).append(tab).append(tab).append(tab).append(tab).append(")\n")
                                .append(tab).append(tab).append(tab).append(tab).append(tab).append(".authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))\n")
                                .append(tab).append(tab).append(tab).append(tab).append(".logout(l->l\n")
                                .append(tab).append(tab).append(tab).append(tab).append(tab).append(".logoutUrl(\"logout\")\n")
                                .append(tab).append(tab).append(tab).append(tab).append(tab).append(".addLogoutHandler(logoutHandler)\n")
                                .append(tab).append(tab).append(tab).append(tab).append(tab).append(".logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()\n")
                                .append(tab).append(tab).append(tab).append(tab).append(tab).append("))\n")
                                .append(tab).append(tab).append(tab).append(tab).append(".build();\n")
                                .append(tab).append("}\n\n")
                                .append(tab).append("@Bean\n")
                                .append(tab).append("public static PasswordEncoder passwordEncoder() {\n")
                                .append(tab).append(tab).append("return new BCRyptPasswordEncoder();\n")
                                .append(tab).append("}\n\n")
                                .append(tab).append("@Bean\n")
                                .append(tab).append("public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {\n")
                                .append(tab).append(tab).append("return configuration.getAuthenticationManager();\n")
                                .append(tab).append("}\n")
                                .append("}");
                        return builder.toString();
    }
}
