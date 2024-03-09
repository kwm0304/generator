package com.kwm0304.cli;

import java.nio.file.Path;

public class GeneratorConfig {
    private Path targetDir;
    private Path modelDir;
    private String userClass;
    private boolean useLombok;
    private boolean generateSecurity;
    private String modelDirString;

    public String getModelDirString() {
        return modelDirString;
    }

    public void setModelDirString(String modelDirString) {
        this.modelDirString = modelDirString;
    }

    public Path getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(Path targetDir) {
        this.targetDir = targetDir;
    }

    public Path getModelDir() {
        return modelDir;
    }

    public void setModelDir(Path modelDir) {
        this.modelDir = modelDir;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public boolean isUseLombok() {
        return useLombok;
    }

    public void setUseLombok(boolean useLombok) {
        this.useLombok = useLombok;
    }

    public boolean isGenerateSecurity() {
        return generateSecurity;
    }

    public void setGenerateSecurity(boolean generateSecurity) {
        this.generateSecurity = generateSecurity;
    }
}
