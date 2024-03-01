package com.kwm0304.cli.template.security;

import com.kwm0304.cli.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class TokenRepositoryTemplate {
    public String genTokenRepository(String modelDir, String parentDirString, String userClass, String idField) {
        String convertedModel = StringUtils.convertPath(modelDir);
        String lowercaseUser = userClass.toLowerCase();
        String convertedRepository = StringUtils.convertPath(parentDirString) + ".repository";
        String tab = "    ";
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedRepository).append(";\n\n")
                .append("import ").append(convertedModel).append(".Token;\n")
                .append("import org.springframework.data.jpa.repository.JpaRepository;\n")
                .append("import org.springframework.data.jpa.repository.Query;\n")
                .append("import java.util.List;\n")
                .append("import java.util.Optional;\n\n")
                .append("public interface TokenRepository extends JpaRepository<Token, Integer> {\n")
                .append(tab).append("@Query(\"\"\"\n")
                .append(tab).append(tab).append("select t from Token t inner join ").append(userClass).append(" u on t.").append(lowercaseUser).append(".id = u.id\n")
                .append(tab).append(tab).append("where t.").append(lowercaseUser).append(".id = :").append(lowercaseUser).append("Id and t.loggedOut = false\n")
                .append(tab).append(tab).append("\"\"\")\n")
                .append(tab).append("List<Token> findAllTokensByUser(").append(idField).append(" ").append(lowercaseUser).append("Id);\n\n")
                .append(tab).append("Optional<Token> findByToken(String token);\n")
                .append("}");
        return builder.toString();
    }
}
