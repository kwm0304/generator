package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class TokenRepositoryTemplate {
    public String genTokenRepository(String modelDir, String parentDirString) {
        String convertedModel = StringUtils.convertPath(modelDir);
        String convertedRepository = StringUtils.convertPath(parentDirString) + ".repository";
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedRepository).append(";\n\n")
                .append("import ").append(convertedModel).append(".Token;\n")
                .append("import org.springframework.data.jpa.repository.JpaRepository;\n")
                .append("import org.springframework.data.jpa.repository.Query;\n")
                .append("import java.util.List;\n")
                .append("import java.util.Optional;\n\n")
                .append("public interface TokenRepository extends JPARepository<Token, Integer> {\n")
                .append(tab).append("@Query(\"\"\"\n")
                .append(tab).append(tab).append("select t from Token t inner join User u on t.user.id = u.id\n")
                .append(tab).append(tab).append("where t.user.id = :userId and t.loggedOut = false\n")
                .append(tab).append(tab).append("\"\"\")\n")
                .append(tab).append("List<Token> findAllTokensByUser(Integer userId);\n\n")
                .append(tab).append("Optional<Token> findByToken(String token);\n")
                .append("}");
        return builder.toString();
    }
}
