package com.kwm0304.cli.service;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.kwm0304.cli.model.ModelField;
import com.kwm0304.cli.model.ModelInfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.springframework.stereotype.Service;

@Service
public class GeneratorService {
    private Path serviceDir;
    private Path controllerDir;
    private Path repositoryDir;
    private Path securityDir;
    private Map<String, ModelInfo> models = new HashMap<>();

    public void makeDirectories(Path targetDir, boolean generateSecurity) {
        if (targetDir == null) {
            System.err.println("Target directory cannot be null");
            return;
        }
        try {
            serviceDir = targetDir.resolve("service");
            controllerDir = targetDir.resolve("controller");
            repositoryDir = targetDir.resolve("repository");

            if (generateSecurity) {
                securityDir = targetDir.resolve("security");
                Files.createDirectories(securityDir);
            }

            Files.createDirectories(serviceDir);
            Files.createDirectories(controllerDir);
            Files.createDirectories(repositoryDir);
        } catch (IOException e) {
            System.err.println("Failed to create directories: " + e.getMessage());
        }
    }
    //calls readModelFile assigns keys and values of entities to models hash map and makes files based on map
    public void parseModelFiles(Path modelDir) {
        try (Stream<Path> paths = Files.walk(modelDir)) {
            paths.filter(Files::isRegularFile).forEach(this::readModelFile);
            models.values().forEach(modelInfo -> {
                makeFiles(modelInfo);
                System.out.println("modelInfo name: " + modelInfo.getName());
                System.out.println("modelInfo fields: " + modelInfo.getFields());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeFiles(ModelInfo modelInfo) {
        Map<String, Path> layerDirs = Map.of(
                "Service", serviceDir,
                "Controller", controllerDir,
                "Repository", repositoryDir
        );
        layerDirs.forEach((layer, dirPath) -> {
            String className = modelInfo.getName() + layer + ".java";
            Path path = dirPath.resolve(className);
            String content = generateLayerContent(modelInfo, layer.toLowerCase());

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(content);
            } catch (IOException e) {
                System.err.println("Failed to write " + layer + " file for " + modelInfo.getName() + ": " + e.getMessage());
            }
        });
    }

    private String generateLayerContent(ModelInfo modelInfo, String lowerCase) {
        return "yo";
    }

    private void readModelFile(Path path) {
        try {
            // Parse the file into a CompilationUnit
            String content = new String(Files.readAllBytes(path));
            CompilationUnit compilationUnit = StaticJavaParser.parse(content);

            // Process each class
            compilationUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(c -> {
                String className = c.getNameAsString();
                ModelInfo currentModel = new ModelInfo(className, new ArrayList<>());

                // Process each field within the class
                c.getFields().forEach(field -> {
                    field.getVariables().forEach(variable -> {
                        String fieldName = variable.getNameAsString();
                        String fieldType = field.getElementType().asString();
                        ModelField modelField = new ModelField(fieldName, fieldType);

                        if (field.getAnnotationByName("Id").isPresent()) {
                            modelField.setId(true);
                        }

                        currentModel.getFields().add(modelField);
                    });
                });

                models.put(className, currentModel);
                System.out.println("Model info: " + currentModel.getName() + currentModel.getFields());
            });

        } catch (IOException e) {
            System.err.println("Failed to read model file: " + e.getMessage());
        }
    }
}
