package com.kwm0304.cli.template;

import com.kwm0304.cli.GeneratorConfig;
import com.kwm0304.cli.StringUtils;
import com.kwm0304.cli.model.ModelInfo;
import org.springframework.stereotype.Service;

@Service
public class RepositoryTemplate {

    private final GeneratorConfig generatorConfig;
    public RepositoryTemplate(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
    }
    public String generateRepository(ModelInfo modelInfo) {
        String modelName = modelInfo.getName();
        String repositoryName = modelName + "Repository";
        String idFieldType = modelInfo.getIdFieldType();
        String directoryPath = generatorConfig.getTargetDir().toString();
        String convertedDirPath = StringUtils.convertPath(directoryPath);
        String convertedModel = StringUtils.convertPath(generatorConfig.getModelDirString());

        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(convertedDirPath).append(".repository;\n\n")
                .append("import org.springframework.data.jpa.repository.JpaRepository;\n");
        if (generatorConfig.isGenerateSecurity()) {
            builder.append("import java.util.Optional;\n");
        }
                builder.append("import ").append(convertedModel).append(".").append(modelName).append(";\n")
                .append("import org.springframework.stereotype.Repository;\n\n")
                .append("@Repository\n")
                .append("public interface ").append(repositoryName).append(" extends JpaRepository")
                .append("<").append(modelName).append(", ").append(idFieldType).append("> {\n");
                if (generatorConfig.isGenerateSecurity() && modelName.equalsIgnoreCase(generatorConfig.getUserClass())) {
                    builder.append("    Optional<").append(generatorConfig.getUserClass()).append("> findByUsername(String username);\n");
                }
                builder.append("}");
        return builder.toString();
    }
}
