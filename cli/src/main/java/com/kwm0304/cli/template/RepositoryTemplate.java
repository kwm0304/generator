package com.kwm0304.cli.template;

import com.kwm0304.cli.StringUtils;
import com.kwm0304.cli.model.ModelInfo;
import org.springframework.stereotype.Service;

@Service
public class RepositoryTemplate {
    public String generateRepository(ModelInfo modelInfo, String parentDirString, boolean genSecurity, String userClass, String modelDirString) {
        String modelName = modelInfo.getName();
        String repositoryName = modelName + "Repository";
        String idFieldType = modelInfo.getIdFieldType();
        String directoryPath = parentDirString;
        String convertedDirPath = StringUtils.convertPath(directoryPath);
        String convertedModel = StringUtils.convertPath(modelDirString);

        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedDirPath).append(".repository;\n\n")
                .append("import org.springframework.data.jpa.repository.JpaRepository;\n");
        if (genSecurity) {
            builder.append("import java.util.Optional;\n");
        }
                builder.append("import ").append(convertedModel).append(".").append(modelName).append(";\n")
                .append("import org.springframework.stereotype.Repository;\n\n")
                .append("@Repository\n")
                .append("public interface ").append(repositoryName).append(" extends JpaRepository")
                .append("<").append(modelName).append(", ").append(idFieldType).append("> {\n");
                if (genSecurity && modelName.equalsIgnoreCase(userClass)) {
                    builder.append("    Optional<").append(userClass).append("> findByUsername(String username);\n");
                }
                builder.append("}");
        return builder.toString();
    }
}
