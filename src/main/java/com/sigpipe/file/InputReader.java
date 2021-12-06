package com.sigpipe.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class InputReader {

    public List<String> readWordList(final String fileName) {
        final var path = Paths.get(fileName);
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new SpellingBeeIORuntimeException(e);
        }
    }

}
