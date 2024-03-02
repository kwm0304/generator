package com.kwm0304.cli.service;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import com.kwm0304.cli.StringUtils;
import com.kwm0304.cli.model.FileContent;
import com.kwm0304.cli.model.ModelField;
import com.kwm0304.cli.model.ModelInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.springframework.stereotype.Service;
import org.xml.sax.ext.LexicalHandler;

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

            genSecurityFiles(userClass, useLombok, parentDirString, modelDirString, modelDir);
        }
    }

    public boolean verifyUserPath(String userClass, String modelDirString, Path modelDir) {
        String userPathString = modelDirString + "/" + userClass + ".java";
        Path userPath = Paths.get(userPathString).toAbsolutePath().normalize();
        Path absoluteModelDir = modelDir.toAbsolutePath().normalize();

        boolean contains = userPath.startsWith(absoluteModelDir);
        if (!contains) {
            System.err.println("User file not found in model directory. userPath:  " + userPath + ", modelPath: " + absoluteModelDir);
            return false;
        }
        return true;
    }

    public void modifyUserMethods(Path modelDir, String userClass, String modelDirString) {
        String lowercaseModel = userClass.toLowerCase();
        String cleanUserClass = StringUtils.cleanClassName(userClass);
        if (verifyUserPath(userClass, modelDirString, modelDir)) {
            try {
            String filePath = modelDirString + "/" + userClass + ".java";
            FileInputStream in = new FileInputStream(filePath);
            CompilationUnit compilationUnit = StaticJavaParser.parse(in);

            addImports(compilationUnit);

            ClassOrInterfaceDeclaration target = compilationUnit.getClassByName(cleanUserClass).orElse(null);
            if (target != null) {
                amendUserClassDeclaration(target);
                FieldDeclaration roleField = target.addField("Role", "role", Modifier.Keyword.PRIVATE);
                roleField.addAnnotation(new NormalAnnotationExpr(
                        StaticJavaParser.parseName("Enumerated"),
                        new NodeList<>(
                                new MemberValuePair("value", new NameExpr("EnumType.String")))
                ));

                FieldDeclaration tokenField = target.addField("List<Token>", "tokens", Modifier.Keyword.PRIVATE);
                tokenField.addAnnotation(new SingleMemberAnnotationExpr(
                        StaticJavaParser.parseName("OneToMany"),
                        new StringLiteralExpr("mappedBy = \"" + lowercaseModel + "\"")
                ));
                addUserDetailsMethods(target);
                addGetterSetter(target, "Role", "role");
                addGetterSetter(target, "List<Token>", "tokens");

                saveCompilationUnit(compilationUnit, filePath);
                }
            in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void amendUserClassDeclaration(ClassOrInterfaceDeclaration targetClass) {
        boolean exists = targetClass.getImplementedTypes().stream()
                .anyMatch(t -> t.getNameAsString().equals("UserDetails"));
        if (!exists) {
            targetClass.addImplementedType(new ClassOrInterfaceType(null, "UserDetails"));
        }
    }

    private void addImports(CompilationUnit compilationUnit) {
        compilationUnit.addImport(new ImportDeclaration("org.springframework.security.core.GrantedAuthority", false, false));
        compilationUnit.addImport(new ImportDeclaration("org.springframework.security.core.authority.SimpleGrantedAuthority", false, false));
        compilationUnit.addImport(new ImportDeclaration("org.springframework.security.core.userdetails.UserDetails", false, false));
    }

    private void addUserDetailsMethods(ClassOrInterfaceDeclaration target) {
        String[] methodNames = {"isAccountNonExpired", "isAccountNonLocked", "isCredentialsNonExpired", "isEnabled"};
        for (String methodName : methodNames) {
            MethodDeclaration method = target.addMethod(methodName, Modifier.Keyword.PUBLIC);
            method.addAnnotation(Override.class);
            method.setType("boolean");
            method.setBody(StaticJavaParser.parseBlock("{ return true; }"));
        }

        MethodDeclaration getAuthorities = target.addMethod("getAuthorities", Modifier.Keyword.PUBLIC);
        getAuthorities.addAnnotation(Override.class);
        getAuthorities.setType("Collection<? extends GrantedAuthority>");
        getAuthorities.setBody(StaticJavaParser.parseBlock("{ return List.of(new SimpleGrantedAuthority(role.name())"));
    }

    private void addGetterSetter(ClassOrInterfaceDeclaration target, String fieldType, String fieldName) {
        String getterName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        target.addMethod(getterName, Modifier.Keyword.PUBLIC)
                .setType(fieldType)
                .setBody(StaticJavaParser.parseBlock("{ return this." + fieldName + "; }"));

        String setterName = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        target.addMethod(setterName, Modifier.Keyword.PUBLIC)
                .addParameter(fieldType, fieldName)
                .setBody(StaticJavaParser.parseBlock("{ this." + fieldName + " = " + fieldName + "; }"));
    }

    private void saveCompilationUnit(CompilationUnit compilationUnit, String filePath) {
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            out.write(compilationUnit.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
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
