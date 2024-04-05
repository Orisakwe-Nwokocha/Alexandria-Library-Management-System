package africa.Semicolon.alexandria.utils;

import lombok.Data;

@Data
public final class Cleaner {
    public static String lowerCaseValueOf(String string) {
        return string.toLowerCase();
    }

    public static String upperCaseValueOf(String string) {
        return string.toUpperCase();
    }
}