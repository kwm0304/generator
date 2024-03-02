package com.kwm0304.cli.service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.kwm0304.cli.StringUtils;
import com.kwm0304.cli.model.ModelField;
import com.kwm0304.cli.model.ModelInfo;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Service
public class ParserService {
    public void modifyUserMethods(Path modelDir, String userClass, String modelDirString) {
        String lowercaseModel = userClass.toLowerCase();
        String cleanUserClass = StringUtils.cleanClassName(userClass);
        if (verifyUserPath(userClass, modelDirString, modelDir)) {
            try {
                String filePath = modelDirString + "/" + userClass + ".java";
                FileInputStream in = new FileInputStream(filePath);
                System.out.println("filePath string: " + filePath);
                CompilationUnit compilationUnit = StaticJavaParser.parse(in);

                addImports(compilationUnit);

                ClassOrInterfaceDeclaration target = compilationUnit.getClassByName(cleanUserClass).orElse(null);
                if (target != null) {
                    amendUserClassDeclaration(target);
                    FieldDeclaration roleField = target.addField("Role", "role", Modifier.Keyword.PRIVATE);
                    roleField.addAnnotation(new NormalAnnotationExpr(
                            StaticJavaParser.parseName("Enumerated"),
                            new NodeList<>(
                                    new MemberValuePair("value", new NameExpr("EnumType.STRING")))
                    ));

                    FieldDeclaration tokenField = target.addField("List<Token>", "tokens", Modifier.Keyword.PRIVATE);
                    tokenField.addAnnotation(new NormalAnnotationExpr(
                            new Name("OneToMany"),
                            NodeList.nodeList(new MemberValuePair("mappedBy", new StringLiteralExpr(lowercaseModel))
                            )
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
        getAuthorities.setBody(StaticJavaParser.parseBlock("{ return List.of(new SimpleGrantedAuthority(role.name())); }"));
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
}
