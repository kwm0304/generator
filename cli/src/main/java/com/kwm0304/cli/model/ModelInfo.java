package com.kwm0304.cli.model;

import java.util.ArrayList;
import java.util.List;

public class ModelInfo {
    private String name;
    List<ModelField> fields = new ArrayList<>();
    public ModelInfo() {}



    public ModelInfo(String name, List<ModelField> fields) {
        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ModelField> getFields() {
        return fields;
    }

    public void setFields(List<ModelField> fields) {
        this.fields = fields;
    }
}
