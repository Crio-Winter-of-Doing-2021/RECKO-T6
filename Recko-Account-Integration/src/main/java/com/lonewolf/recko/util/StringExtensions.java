package com.lonewolf.recko.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringExtensions {
    private StringExtensions() {
    }

    public static String capitalize(String message) {
        return Arrays.stream(message.trim().toLowerCase()
                .split(" "))
                .filter((w) -> !w.isEmpty())
                .map((w) -> String.valueOf(w.charAt(0)).toUpperCase().concat(w.substring(1)).concat(" "))
                .collect(Collectors.joining());
    }
}
