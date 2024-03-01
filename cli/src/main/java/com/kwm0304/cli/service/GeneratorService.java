package com.kwm0304.cli.service;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.kwm0304.cli.model.FileContent;
import com.kwm0304.cli.model.ModelField;
import com.kwm0304.cli.model.ModelInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private Path configDir;
    private Path filterDir;
    private Map<String, ModelInfo> models = new HashMap<>();
    private BuilderService builderService;
    private SecurityService securityService;

    public GeneratorService(BuilderService builderService, SecurityService securityService) {
        this.builderService = builderService;
        this.securityService = securityService;
    }

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
                configDir = targetDir.resolve("config");
                filterDir = targetDir.resolve("filter");
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
    public void parseModelFiles(Path modelDir, boolean useLombok, String userClass, boolean generateSecurity) {
        Path parentDir = modelDir.getParent();
        String modelDirString = modelDir.toString();
        String parentDirString = parentDir.toString();
        try (Stream<Path> paths = Files.walk(modelDir)) {
            paths.filter(Files::isRegularFile).forEach(this::readModelFile);
            models.values().forEach(modelInfo -> {
                makeFiles(modelInfo, parentDirString, useLombok, userClass, generateSecurity, modelDirString, modelDir);
                System.out.println("modelInfo name: " + modelInfo.getName());
                System.out.println("modelInfo fields: " + modelInfo.getFields());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeFiles(ModelInfo modelInfo, String parentDirString, boolean useLombok, String userClass, boolean generateSecurity, String modelDirString, Path modelDir) {
        Map<String, Path> layerDirs = Map.of(
                "Service", serviceDir,
                "Controller", controllerDir,
                "Repository", repositoryDir
        );
        layerDirs.forEach((layer, dirPath) -> {
            String className = modelInfo.getName() + layer + ".java";
            Path path = dirPath.resolve(className);
            String content = generateLayerContent(modelInfo, layer.toLowerCase(), parentDirString, useLombok, modelDirString, userClass, generateSecurity);

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(content);
            } catch (IOException e) {
                System.err.println("Failed to write " + layer + " file for " + modelInfo.getName() + ": " + e.getMessage());
            }
        });
        if (generateSecurity) {
            //TODO: append methods in userrepository and userdetails methods
            genSecurityFiles(userClass, useLombok, parentDirString, modelDirString, modelDir);
        }
    }

    public String appendUserSecurityMethods(String userClass, String modelDirString) {

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

    private String generateLayerContent(ModelInfo modelInfo, String layer, String parentDirString, boolean useLombok, String modelDirString, String userClass, boolean genSecurity) {
        switch (layer) {
            case "controller":
                return builderService.makeControllerLayer(modelInfo, parentDirString, useLombok, modelDirString);
            case "service":
                return builderService.makeServiceLayer(modelInfo, parentDirString, useLombok, userClass, modelDirString);
            case "repository":
                return builderService.makeRepositoryLayer(modelInfo, parentDirString, userClass, genSecurity, modelDirString);
            default:
                return "";
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

    public void genSecurityFiles(String userClass, boolean useLombok, String parentDirString, String modelDirString, Path modelDir) {
        String userIdType = getIdTypeForUser(userClass);
        List<FileContent> configFiles = securityService.makeConfigFiles(parentDirString, modelDirString, useLombok);
        for (FileContent file : configFiles) {
            writeSecurityFiles(file.getFileName(), configDir, file.getContent());
        }

        List<FileContent> filterFiles = securityService.makeFilterFiles(parentDirString, useLombok);
        for (FileContent file : filterFiles) {
            writeSecurityFiles(file.getFileName(), filterDir, file.getContent());
        }

        List<FileContent> serviceFiles = securityService.makeServiceFiles(parentDirString, modelDirString, userClass, useLombok);
        for (FileContent file : serviceFiles) {
            writeSecurityFiles(file.getFileName(), serviceDir, file.getContent());
        }

        List<FileContent> controllerFiles = securityService.makeControllerFiles(parentDirString, userClass, useLombok, modelDirString);
        for (FileContent file : controllerFiles) {
            writeSecurityFiles(file.getFileName(), controllerDir, file.getContent());
        }

        List<FileContent> modelFiles = securityService.makeModelFiles(modelDirString, useLombok, userClass);
        for (FileContent file : modelFiles) {
            writeSecurityFiles(file.getFileName(), modelDir, file.getContent());
        }

        List<FileContent> repositoryFiles = securityService.makeRepositoryFiles(modelDirString, parentDirString, userClass, userIdType);
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
