package com.sigpipe.file;

import com.sigpipe.spellingbee.SpellingBeeEntry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputGenerator {

    private static final String[] HEADERS = {"word", "phonetic", "partOfSpeech", "definition", "example", "pronunciationUrl"};

    public void writeEntriesToCSV(final List<SpellingBeeEntry> entryList, final String output) {
        try {
            FileWriter out = new FileWriter(output);
            try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                    .withHeader(HEADERS))) {
                entryList.forEach(entry -> {
                    try {
                        printer.printRecord(
                                entry.word(),
                                entry.phonetic(),
                                entry.partOfSpeech(),
                                entry.definition(),
                                entry.example(),
                                entry.pronunciation()
                        );
                    } catch (IOException e) {
                        throw new SpellingBeeIORuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            throw new SpellingBeeIORuntimeException(e);
        }
    }

}
