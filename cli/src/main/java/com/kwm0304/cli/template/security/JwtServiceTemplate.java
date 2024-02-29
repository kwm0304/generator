package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;

public class JwtServiceTemplate {
    public String genJwtService(String parentDir, String modelDir, String userClass, boolean useLombok) {
        String convertedParent = StringUtils.convertPath(parentDir);
        String convertedModel = StringUtils.convertPath(modelDir);
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedParent).append(".service;\n\n")
                .append("import ").append(convertedModel).append(".").append(userClass).append(";\n")
                .append("import ").append(convertedParent).append(".repository.TokenRepository;\n")
                .append("import io.jsonwebtoken.Claims;\n")
                .append("import io.jsonwebtoken.Jwts;\n")
                .append("import io.jsonwebtoken.io.Decoders;\n")
                .append("import io.jsonwebtoken.security.Keys;\n")
                .append("import org.springframework.security.core.userdetails.UserDetails;\n")
                .append("import org.springframework.stereotype.Service;\n")
                .append("import java.util.Date;\n")
                .append("import java.util.function.Function;\n")
                .append("import javax.crypto.SecretKey;\n\n");
                if (useLombok) {
                    builder.append("@RequiredArgsConstructor\n");
                }
                builder.append("@Service\n")
                .append("public class JwtService {\n")
                .append(tab).append("//move this to application.properties or yml\n")
                .append(tab).append("private final String SECRET_KEY=\"4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c\";\n")
                .append(tab).append("private final TokenRepository tokenRepository;\n");
                if (!useLombok) {
                    builder.append(tab).append("public JwtService(TokenRepository tokenRepository) { this.tokenRepository = tokenRepository; }\n");
                }
                builder.append(tab).append("public String extractUsername(String token) { return extractClaim(token, Claims::getSubject); }\n")
                        .append(tab).append("public boolean isValid(String token, UserDetails user) {\n")
                        .append(tab).append(tab).append("String username = extractUsername(token);\n")
                        .append(tab).append(tab).append("boolean validToken = tokenRepository.findByToken(token)\n")
                        .append(tab).append(tab).append(tab).append(".map(t -> !t.isLoggedOut())\n")
                        .append(tab).append(tab).append(tab).append(".orElse(false);\n\n")
                        .append(tab).append(tab).append("return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken;\n")
                        .append(tab).append("}\n\n")
                        .append(tab).append("private boolean isTokenExpired(String token) { return extractExpiration(token).before(new Date()); }\n")
                        .append(tab).append("private Date extractExpiration(String token) { return extractClaim(token, Claims::getExpiration); }\n")
                        .append(tab).append("private <T> T extractClaim(String token, Function<Claims, T> resolver) {\n")
                        .append(tab).append(tab).append("Claims claims = extractAllClaims(token);\n")
                        .append(tab).append(tab).append("return resolver.apply(claims);\n")
                        .append(tab).append("}\n\n")
                        .append(tab).append("private CLaims extractAllClaims(String token) {\n")
                        .append(tab).append(tab).append("return Jwts\n")
                        .append(tab).append(tab).append(tab).append(".parser()\n")
                        .append(tab).append(tab).append(tab).append(".verifyWith(getSigninKey())\n")
                        .append(tab).append(tab).append(tab).append(".build()\n")
                        .append(tab).append(tab).append(tab).append(".parseSignedClaims(token)\n")
                        .append(tab).append(tab).append(tab).append(".getPayload();\n")
                        .append(tab).append("}\n\n")
                        .append(tab).append("public String generateToken(").append(userClass).append(" user) {\n")
                        .append(tab).append(tab).append("String token = Jwts\n")
                        .append(tab).append(tab).append(tab).append(".builder()\n")
                        .append(tab).append(tab).append(tab).append(".subject(user.getUsername())\n")
                        .append(tab).append(tab).append(tab).append(".issuedAt(new Date(System.currentTimeMillis()))\n")
                        .append(tab).append(tab).append(tab).append(".expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))\n")
                        .append(tab).append(tab).append(tab).append(".signWith(getSigninKey())\n")
                        .append(tab).append(tab).append(tab).append(".compact();\n")
                        .append(tab).append(tab).append("return token;\n")
                        .append(tab).append("}\n\n")
                        .append(tab).append("private SecretKey getSigninKey() {\n")
                        .append(tab).append(tab).append("byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);\n")
                        .append(tab).append(tab).append("return Keys.hmacShaKeyFor(keyBytes);\n")
                        .append(tab).append("}\n")
                        .append("}");
                return builder.toString();
    }
}
