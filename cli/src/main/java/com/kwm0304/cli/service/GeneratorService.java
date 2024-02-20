package com.kwm0304.cli.service;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.kwm0304.cli.model.ModelField;
import com.kwm0304.cli.model.ModelInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;


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
    public void parseModelFiles(Path modelDir) {
        try (Stream<Path> paths = Files.walk(modelDir)) {
            paths.filter(Files::isRegularFile).forEach(this::readModelFile);
            models.values().forEach(modelInfo -> {
                makeFiles(modelInfo);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
