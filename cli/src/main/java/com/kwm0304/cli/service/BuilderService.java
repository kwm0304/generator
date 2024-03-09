package com.kwm0304.cli.service;

import com.kwm0304.cli.GeneratorConfig;
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
    private final GeneratorConfig generatorConfig;

    public BuilderService(ServiceTemplate serviceTemplate, ControllerTemplate controllerTemplate, RepositoryTemplate repositoryTemplate, GeneratorConfig generatorConfig) {
        this.serviceTemplate = serviceTemplate;
        this.controllerTemplate = controllerTemplate;
        this.repositoryTemplate = repositoryTemplate;
        this.generatorConfig = generatorConfig;
    }
    public String makeControllerLayer(ModelInfo modelInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append(controllerTemplate.generateController(modelInfo));
        return builder.toString();
    }
    public String makeServiceLayer(ModelInfo modelInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append(serviceTemplate.generateService(modelInfo));
        return builder.toString();
    }
    public String makeRepositoryLayer(ModelInfo modelInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append(repositoryTemplate.generateRepository(modelInfo));
        return builder.toString();
    }
}
