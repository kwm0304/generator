package com.kwm0304.cli.service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.kwm0304.cli.GeneratorConfig;
import com.kwm0304.cli.model.FileContent;
import com.kwm0304.cli.model.ModelField;
import com.kwm0304.cli.model.ModelInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneratorService {
    private Path serviceDir;
    private Path controllerDir;
    private Path repositoryDir;
    private Path configDir;
    private Path filterDir;
    private Map<String, ModelInfo> models = new HashMap<>();
    private BuilderService builderService;
    private SecurityService securityService;
    private final GeneratorConfig generatorConfig;
    @Autowired
    private ParserService parserService;

    public GeneratorService(BuilderService builderService,
                            SecurityService securityService,
                            ParserService parserService,
                            GeneratorConfig generatorConfig) {
        this.builderService = builderService;
        this.securityService = securityService;
        this.parserService = parserService;
        this.generatorConfig = generatorConfig;
    }

    public void makeDirectories() {
        if (generatorConfig.getTargetDir() == null) {
            System.err.println("Target directory cannot be null");
            return;
        }
        try {
            serviceDir = generatorConfig.getTargetDir().resolve("service");
            controllerDir = generatorConfig.getTargetDir().resolve("controller");
            repositoryDir = generatorConfig.getTargetDir().resolve("repository");

            if (generatorConfig.isGenerateSecurity()) {
                configDir = generatorConfig.getTargetDir().resolve("config");
                filterDir = generatorConfig.getTargetDir().resolve("filter");
                Files.createDirectories(configDir);
                Files.createDirectories(filterDir);
            }

            Files.createDirectories(serviceDir);
            Files.createDirectories(controllerDir);
            Files.createDirectories(repositoryDir);
        } catch (IOException e) {
            System.err.println("Failed to create directories: " + e.getMessage());
        }
    }
    //calls readModelFile assigns keys and values of entities to models hash map and makes files based on map
    public void parseModelFiles() {
        try (Stream<Path> paths = Files.walk(generatorConfig.getModelDir())) {
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
        if (generatorConfig.isGenerateSecurity()) {
            genSecurityFiles();
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

    public String getIdTypeForUser(String userClass) {
        String cleanUser = userClass.toLowerCase();
        for (String className : models.keySet()) {
            if (className.equalsIgnoreCase(cleanUser)) {
                ModelInfo modelInfo = models.get(className);
                for (ModelField field : modelInfo.getFields()) {
                    if (field.isId()) {
                        return field.getType();
                    }
                }
            }
        }
        return null;
    }

    private String generateLayerContent(ModelInfo modelInfo) {
        switch (layer) {
            case "controller":
                return builderService.makeControllerLayer(modelInfo);
            case "service":
                return builderService.makeServiceLayer(modelInfo);
            case "repository":
                return builderService.makeRepositoryLayer(modelInfo);
            default:
                return "";
        }
    }

    public void genSecurityFiles() {
        parserService.modifyUserMethods();
        String userIdType = getIdTypeForUser();
        List<FileContent> configFiles = securityService.makeConfigFiles();
        for (FileContent file : configFiles) {
            writeSecurityFiles(file.getFileName(), configDir, file.getContent());
        }

        List<FileContent> filterFiles = securityService.makeFilterFiles();
        for (FileContent file : filterFiles) {
            writeSecurityFiles(file.getFileName(), filterDir, file.getContent());
        }

        List<FileContent> serviceFiles = securityService.makeServiceFiles();
        for (FileContent file : serviceFiles) {
            writeSecurityFiles(file.getFileName(), serviceDir, file.getContent());
        }

        List<FileContent> controllerFiles = securityService.makeControllerFiles();
        for (FileContent file : controllerFiles) {
            writeSecurityFiles(file.getFileName(), controllerDir, file.getContent());
        }

        List<FileContent> modelFiles = securityService.makeModelFiles();
        for (FileContent file : modelFiles) {
            writeSecurityFiles(file.getFileName(), generatorConfig.getModelDir(), file.getContent());
        }

        List<FileContent> repositoryFiles = securityService.makeRepositoryFiles(userIdType);
        for (FileContent file : repositoryFiles) {
            writeSecurityFiles(file.getFileName(), repositoryDir, file.getContent());
        }
    }

    public void writeSecurityFiles(String fileName, Path dirPath, String content) {
        Path filePath = dirPath.resolve(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Failed to write security file " + fileName + ": " + e.getMessage());
        }
    }
}
