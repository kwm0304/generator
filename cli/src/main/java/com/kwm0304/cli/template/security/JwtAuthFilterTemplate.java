package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;

public class JwtAuthFilterTemplate {
    public String genAuthFilter(String parentDirString, String serviceDirString, boolean useLombok) {
        String convertedParent = StringUtils.convertPath(parentDirString);
        String convertedService = StringUtils.convertPath(serviceDirString);
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedParent).append(";\n\n")
                .append("import ").append(convertedService).append(".JwtService;\n")
                .append("import ").append(convertedService).append(".UserDetailsServiceImpl;\n")
                .append("import jakarta.servlet.FilterChain;\n")
                .append("import jakarta.servlet.ServletException;\n")
                .append("import jakarta.servlet.http.HttpServletRequest;\n")
                .append("import jakarta.servlet.http.HttpServletResponse;\n")
                .append("import org.springframework.lang.NonNull;\n")
                .append("import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;\n")
                .append("import org.springframework.security.core.context.SecurityContextHolder;\n")
                .append("import org.springframework.security.core.userdetails.UserDetails;\n")
                .append("import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;\n")
                .append("import org.springframework.stereotype.Component;\n")
                .append("import org.springframework.web.filter.OncePerRequestFilter;\n")
                .append("import java.io.IOException;\n\n");

                if (useLombok) {
                    builder.append("@AllArgsConstructor\n");
                }
                builder.append("@Component\n")
                .append("public class JwtAuthFilter extends OncePerRequestFilter {\n")
                .append(tab).append("private final JwtService jwtService;\n")
                .append(tab).append("private final UserDetailsServiceImpl userDetailsServiceImpl;\n");
                if (!useLombok) {
                    builder.append(tab).append("public JwtAuthFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsServiceImpl) {\n")
                            .append(tab).append(tab).append("this.jwtService = jwtService;\n")
                            .append(tab).append(tab).append("this.userDetailsServiceImpl = userDetailsServiceImpl;\n")
                            .append(tab).append("}\n\n");
                }
                builder.append(tab).append("@Override\n")
                        .append(tab).append("protected void doFilterInternal(\n")
                        .append(tab).append(tab).append("@NonNull HttpServletRequest request,\n")
                        .append(tab).append(tab).append("@NonNull HttpServletResponse response,\n")
                        .append(tab).append(tab).append("@NonNull FilterChain filterChain) throws ServletException, IOException {\n")
                        .append(tab).append(tab).append("String authHeader = request.getHeader(\"Authorization\");\n\n")
                        .append(tab).append(tab).append("if (authHeader == null || !authHeader.startsWith(\"Bearer \")) {\n")
                        .append(tab).append(tab).append(tab).append("filterChain.doFilter(request, response);\n")
                        .append(tab).append(tab).append(tab).append("return;\n")
                        .append(tab).append(tab).append("}\n\n")
                        .append(tab).append(tab).append("String token = authHeader.substring(7);\n")
                        .append(tab).append(tab).append("String username = jwtService.extractUsername(token);\n\n")
                        .append(tab).append(tab).append("if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {\n")
                        .append(tab).append(tab).append(tab).append("UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);\n\n")
                        .append(tab).append(tab).append(tab).append("if (jwtService.isValid(token, userDetails)) {\n")
                        .append(tab).append(tab).append(tab).append(tab).append("UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(\n")
                        .append(tab).append(tab).append(tab).append(tab).append(tab).append("userDetails, null, userDetails.getAuthorities()\n")
                        .append(tab).append(tab).append(tab).append(tab).append(");\n")
                        .append(tab).append(tab).append(tab).append(tab).append("authToken.setDetails(\n")
                        .append(tab).append(tab).append(tab).append(tab).append(tab).append("new WebAuthenticationDetailsSource().buildDetails(request)\n")
                        .append(tab).append(tab).append(tab).append(tab).append(");\n")
                        .append(tab).append(tab).append(tab).append(tab).append("SecurityContextHolder.getContext().setAuthentication(authToken);\n")
                        .append(tab).append(tab).append(tab).append("}\n")
                        .append(tab).append(tab).append("}\n")
                        .append(tab).append(tab).append("filterChain.doFilter(request, response);\n")
                        .append(tab).append("}\n")
                        .append("}\n");
return builder.toString();
    }
}
