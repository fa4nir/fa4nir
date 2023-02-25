package com.github.interceptors.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class TestFileUtils {

    public static String loadJavaFileAsString(String pathToFile) throws URISyntaxException, IOException {
        Path path = Path.of(ClassLoader.getSystemResource(pathToFile).toURI());
        return Files.lines(path).collect(Collectors.joining(System.lineSeparator()));
    }

}
