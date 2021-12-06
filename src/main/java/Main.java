import com.sigpipe.file.InputReader;
import com.sigpipe.file.OutputGenerator;
import com.sigpipe.spellingbee.EntryClient;
import com.sigpipe.spellingbee.SpellingBeeGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(final String[] args) {
        final var inputFile = args[0];
        final var outputFile = args[1];

        logger.info("Reading Input File: {}", inputFile);
        logger.info("Writing to Output File: {}", outputFile);

        final var inputReader = new InputReader();
        final var entryClient = new EntryClient();
        final var spellingBeeGenerator = new SpellingBeeGenerator(entryClient);
        final var outputGenerator = new OutputGenerator();

        final var words = inputReader.readWordList(inputFile);
        final var entries = spellingBeeGenerator.generateSpellingBee(words);

        logger.trace("Fetched Entries: {}", entries);

        outputGenerator.writeEntriesToCSV(entries, outputFile);

        logger.info("Write to {} is complete", outputFile);

        System.exit(0);
    }
}
