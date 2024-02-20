package com.kwm0304.cli.template;

import com.kwm0304.cli.StringUtils;
import com.kwm0304.cli.model.ModelInfo;
import org.springframework.stereotype.Service;

@Service
public class ControllerTemplate {


    public String generateController(ModelInfo modelInfo, String parentDirString, boolean useLombok) {
        String directoryPath = parentDirString;
        String convertedDirPath = StringUtils.convertPath(directoryPath);
        String modelName = modelInfo.getName();
        String modelNameLowercase = modelName.substring(0, 1).toLowerCase() + modelName.substring(1);
        String serviceName = modelName + "Service";
        String idFieldType = modelInfo.getIdFieldType();

        StringBuilder builder = new StringBuilder();

        builder.append("package ").append(convertedDirPath).append(".controller;\n\n")
                .append("import ").append(convertedDirPath).append(".model.").append(modelName).append(";\n")
                .append("import ").append(convertedDirPath).append(".service.").append(modelName).append("Service;\n")
                .append("import org.springframework.beans.factory.annotation.Autowired;\n")
                .append("import org.springframework.http.ResponseEntity;\n")
                .append("import org.springframework.web.bind.annotation.*;\n")
                .append("import java.util.List;\n\n")
                .append("@RestController\n")
                .append("@RequestMapping(\"/api/").append(modelNameLowercase).append("s\")\n")
                .append("public class ").append(modelName).append("Controller {\n\n")
                .append("    private final ").append(serviceName).append(" ").append(modelNameLowercase).append("Service;\n\n")
                .append("    @Autowired\n")
                .append("    public ").append(modelName).append("Controller(").append(serviceName).append(" ").append(modelNameLowercase).append("Service) {\n")
                .append("        this.").append(modelNameLowercase).append("Service = ").append(modelNameLowercase).append("Service;\n")
                .append("    }\n\n")
                .append("    @GetMapping\n")
                .append("    public List<").append(modelName).append("> getAll() {\n")
                .append("        return ").append(modelNameLowercase).append("Service.findAll();\n")
                .append("    }\n\n")
                .append("    @GetMapping(\"/{id}\")\n")
                .append("    public ResponseEntity<").append(modelName).append("> getById(@PathVariable ").append(idFieldType).append(" id) {\n")
                .append("        return ").append(modelNameLowercase).append("Service.findById(id)\n")
                .append("                .map(record -> ResponseEntity.ok().body(record))\n")
                .append("                .orElse(ResponseEntity.notFound().build());\n")
                .append("    }\n\n")
                .append("    @PostMapping\n")
                .append("    public ").append(modelName).append(" create(@RequestBody ").append(modelName).append(" ").append(modelNameLowercase).append(") {\n")
                .append("        return ").append(modelNameLowercase).append("Service.save(").append(modelNameLowercase).append(");\n")
                .append("    }\n\n")
                .append("    @PutMapping(\"/{id}\")\n")
                .append("    public ResponseEntity<").append(modelName).append("> update(@PathVariable ").append(idFieldType).append(" id, @RequestBody ").append(modelName).append(" ").append(modelNameLowercase).append("Details) {\n")
                .append("        return ResponseEntity.ok(").append(modelNameLowercase).append("Service.update(id, ").append(modelNameLowercase).append("Details));\n")
                .append("    }\n\n")
                .append("    @DeleteMapping(\"/{id}\")\n")
                .append("    public ResponseEntity<?> delete(@PathVariable ").append(idFieldType).append(" id) {\n")
                .append("        ").append(modelNameLowercase).append("Service.deleteById(id);\n")
                .append("        return ResponseEntity.ok().build();\n")
                .append("    }\n")
                .append("}\n");

        return builder.toString();
    }
}

