package vn.edu.hcmus.fit.sv18120113.BT4;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    public static String slugify(String input) {
        return input
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", "-");
    }

    public static String[] getLines(String str) {
        return str.split("\\r?\\n");
    }

    public static <V> void print(V v) {
        System.out.print(v);
    }

    public static <V> void println(V v) {
        System.out.println(v);
    }

    public static void println() {
        System.out.println();
    }

    public static String getWorkingDir () {
        Path currentRelativePath = Paths.get("");
        return currentRelativePath.toAbsolutePath().normalize().toString();
    }
}
