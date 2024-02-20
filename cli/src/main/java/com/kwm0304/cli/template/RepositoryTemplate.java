package com.kwm0304.cli.template;

import com.kwm0304.cli.model.ModelInfo;
import org.springframework.stereotype.Service;

@Service
public class RepositoryTemplate {
    public String generateRepository(ModelInfo modelInfo) {
        String modelName = modelInfo.getName();
        String repositoryName = modelName + "Repository";
        String idFieldType = modelInfo.getIdFieldType();

        StringBuilder builder = new StringBuilder();
        builder.append("import org.springframework.data.jpa.repository.JpaRepository;\n")
                .append("import org.springframework.stereotype.Repository;\n\n")
                .append("@Repository\n")
                .append("public interface ").append(repositoryName).append(" extends JpaRepository")
                .append("<").append(modelName).append(", ").append(idFieldType).append("> {\n")
                .append("}");
        return builder.toString();
    }
}
