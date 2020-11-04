package com.rdesouter.utils;

public class StringHandling {

    public static String extract(String content, String sub, boolean isBefore) {
        if (isBefore) {
            return content.substring(0, content.indexOf(sub));
        } else {
            return content.substring(content.indexOf(sub) + sub.length());
        }
    }

    public static String[] splitNewLine(String content) {
        return content.split("\\r?\\n");
    }
}
