package com.kwm0304.cli;

public class StringUtils {
    public static String convertPath(String path) {
        int comIndex = path.indexOf("com");
        if (comIndex != -1) {
            return path.substring(comIndex).replace("\\", ".");
        }
        return null;
    }

    public static String cleanClassName(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String first = input.substring(0,1).toUpperCase();
        String rest = input.substring(1).toLowerCase();
        return first + rest;
    }
}
