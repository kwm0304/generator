package com.kwm0304.cli.model;

public class FileContent {
    private final String fileName;
    private final String content;

    public FileContent(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }
}
