package com.kwm0304.cli.template;

import com.kwm0304.cli.StringUtils;
import com.kwm0304.cli.model.ModelInfo;
import org.springframework.stereotype.Service;

//TODO: need to pass in path to repository and import it
@Service
public class ServiceTemplate {

    public String generateService(ModelInfo modelInfo, String parentDirString) {
        String directoryPath = parentDirString;
        String convertedDirPath = StringUtils.convertPath(directoryPath);
        String modelName = modelInfo.getName();
        String modelNameLowercase = modelName.substring(0, 1).toLowerCase() + modelName.substring(1);
        String repositoryName = modelName + "Repository";

        StringBuilder builder = new StringBuilder();

        builder.append("package ").append(convertedDirPath).append(".service;\n\n")
                .append("import ").append(convertedDirPath).append(".repository.").append(modelName).append("Repository;\n")
                .append("import org.springframework.beans.factory.annotation.Autowired;\n")
                .append("import org.springframework.stereotype.Service;\n")
                .append("import java.util.List;\n")
                .append("import java.util.Optional;\n\n")
                .append("@Service\n")
                .append("public class ").append(modelName).append("Service {\n\n")
                .append("    private final ").append(repositoryName).append(" ").append(modelNameLowercase).append("Repository;\n\n")
                .append("    @Autowired\n")
                .append("    public ").append(modelName).append("Service(").append(repositoryName).append(" ").append(modelNameLowercase).append("Repository) {\n")
                .append("        this.").append(modelNameLowercase).append("Repository = ").append(modelNameLowercase).append("Repository;\n")
                .append("    }\n\n")
                .append("    public List<").append(modelName).append("> findAll() {\n")
                .append("        return ").append(modelNameLowercase).append("Repository.findAll();\n")
                .append("    }\n\n")
                .append("    public Optional<").append(modelName).append("> findById(Long id) {\n")
                .append("        return ").append(modelNameLowercase).append("Repository.findById(id);\n")
                .append("    }\n\n")
                .append("    public ").append(modelName).append(" save(").append(modelName).append(" ").append(modelNameLowercase).append(") {\n")
                .append("        return ").append(modelNameLowercase).append("Repository.save(").append(modelNameLowercase).append(");\n")
                .append("    }\n\n")
                .append("    public ").append(modelName).append(" update(Long id, ").append(modelName).append(" ").append(modelNameLowercase).append("Details) {\n")
                .append("        if (!").append(modelNameLowercase).append("Repository.existsById(id)) {\n")
                .append("            throw new RuntimeException(\"").append(modelName).append(" not found with id \" + id);\n")
                .append("        }\n")
                .append("        ").append(modelNameLowercase).append("Details.setId(id);\n")
                .append("        return ").append(modelNameLowercase).append("Repository.save(").append(modelNameLowercase).append("Details);\n")
                .append("    }\n\n")
                .append("    public void deleteById(Long id) {\n")
                .append("        if (!").append(modelNameLowercase).append("Repository.existsById(id)) {\n")
                .append("            throw new RuntimeException(\"").append(modelName).append(" not found with id \" + id);\n")
                .append("        }\n")
                .append("        ").append(modelNameLowercase).append("Repository.deleteById(id);\n")
                .append("    }\n")
                .append("}\n");

        return builder.toString();
    }
}
