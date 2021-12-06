package com.sigpipe.spellingbee;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SpellingBeeGenerator {

    private static final Logger logger = LogManager.getLogger();
    private final EntryClient entryClient;

    public SpellingBeeGenerator(final EntryClient entryClient) {
        this.entryClient = entryClient;
    }

    public List<SpellingBeeEntry> generateSpellingBee(final List<String> words) {
        return words.stream()
                .map(
                        word -> {
                            try {
                                TimeUnit.MILLISECONDS.sleep(100);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                            }
                            logger.trace("Beginning retrieval of word: {}", word);
                            return entryClient.getEntry(word);
                        }
                )
                .collect(Collectors.toList());
    }

}
