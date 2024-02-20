package com.kwm0304.cli.service;

import com.kwm0304.cli.model.ModelInfo;
import com.kwm0304.cli.template.ServiceTemplate;
import org.springframework.stereotype.Service;

@Service
public class BuilderService {
    private ServiceTemplate serviceTemplate;

    public BuilderService(ServiceTemplate serviceTemplate) {
        this.serviceTemplate = serviceTemplate;
    }
    public String makeControllerLayer(ModelInfo modelInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("controller");
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
