package com.kwm0304.cli;

public class StringUtils {
    public static String convertPath(String path) {
        int comIndex = path.indexOf("com");
        if (comIndex != -1) {
            return path.substring(comIndex).replace("\\", ".");
        }
        return null;
    }
}
