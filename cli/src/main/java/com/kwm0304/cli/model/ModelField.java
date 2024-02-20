package com.kwm0304.cli.model;

public class ModelField {
    private String type;
    private String name;
    private boolean isId;
    public ModelField(String name, String type) {
        this.type = type;
        this.name = name;
        this.isId = false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isId() {
        return isId;
    }

    public void setId(boolean id) {
        isId = id;
    }
}
