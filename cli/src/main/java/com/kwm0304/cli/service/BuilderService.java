package com.kwm0304.cli.service;

import com.kwm0304.cli.model.ModelInfo;
import com.kwm0304.cli.template.ControllerTemplate;
import com.kwm0304.cli.template.ServiceTemplate;
import org.springframework.stereotype.Service;

@Service
public class BuilderService {
    private ServiceTemplate serviceTemplate;
    private ControllerTemplate controllerTemplate;

    public BuilderService(ServiceTemplate serviceTemplate, ControllerTemplate controllerTemplate) {
        this.serviceTemplate = serviceTemplate;
        this.controllerTemplate = controllerTemplate;
    }
    public String makeControllerLayer(ModelInfo modelInfo, String parentDirString) {
        StringBuilder builder = new StringBuilder();
        builder.append(controllerTemplate.generateController(modelInfo, parentDirString));
        return builder.toString();
    }
    public String makeServiceLayer(ModelInfo modelInfo, String parentDirString) {
        StringBuilder builder = new StringBuilder();
        builder.append(serviceTemplate.generateService(modelInfo, parentDirString));
        return builder.toString();
    }
    public String makeRepositoryLayer(ModelInfo modelInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("repository");
        return builder.toString();
    }
}
