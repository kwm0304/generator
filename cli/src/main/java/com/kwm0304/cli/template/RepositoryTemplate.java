package com.kwm0304.cli.template;

import com.kwm0304.cli.StringUtils;
import com.kwm0304.cli.model.ModelInfo;
import org.springframework.stereotype.Service;

@Service
public class RepositoryTemplate {
    public String generateRepository(ModelInfo modelInfo, String parentDirString) {
        String modelName = modelInfo.getName();
        String repositoryName = modelName + "Repository";
        String idFieldType = modelInfo.getIdFieldType();
        String directoryPath = parentDirString;
        String convertedDirPath = StringUtils.convertPath(directoryPath);

        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedDirPath).append(".repository;\n\n")
                .append("import org.springframework.data.jpa.repository.JpaRepository;\n")
                .append("import org.springframework.stereotype.Repository;\n\n")
                .append("@Repository\n")
                .append("public interface ").append(repositoryName).append(" extends JpaRepository")
                .append("<").append(modelName).append(", ").append(idFieldType).append("> {\n")
                .append("}");
        return builder.toString();
    }
}
