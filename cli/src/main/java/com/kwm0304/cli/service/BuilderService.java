package com.kwm0304.cli.service;

import com.kwm0304.cli.model.ModelInfo;
import com.kwm0304.cli.template.ControllerTemplate;
import com.kwm0304.cli.template.RepositoryTemplate;
import com.kwm0304.cli.template.ServiceTemplate;
import org.springframework.stereotype.Service;

@Service
public class BuilderService {
    private ServiceTemplate serviceTemplate;
    private ControllerTemplate controllerTemplate;
    private RepositoryTemplate repositoryTemplate;

    public BuilderService(ServiceTemplate serviceTemplate, ControllerTemplate controllerTemplate, RepositoryTemplate repositoryTemplate) {
        this.serviceTemplate = serviceTemplate;
        this.controllerTemplate = controllerTemplate;
        this.repositoryTemplate = repositoryTemplate;
    }
    public String makeControllerLayer(ModelInfo modelInfo, String parentDirString, boolean useLombok, String modelDirString) {
        StringBuilder builder = new StringBuilder();
        builder.append(controllerTemplate.generateController(modelInfo, parentDirString, useLombok, modelDirString));
        return builder.toString();
    }
    public String makeServiceLayer(ModelInfo modelInfo, String parentDirString, boolean useLombok, String userClass, String modelDirString) {
        StringBuilder builder = new StringBuilder();
        builder.append(serviceTemplate.generateService(modelInfo, parentDirString, useLombok, userClass, modelDirString));
        return builder.toString();
    }
    public String makeRepositoryLayer(ModelInfo modelInfo, String parentDirString, String userClass, boolean genSecurity, String modelDirString) {
        StringBuilder builder = new StringBuilder();
        builder.append(repositoryTemplate.generateRepository(modelInfo, parentDirString, genSecurity, userClass, modelDirString));
        return builder.toString();
    }
}
