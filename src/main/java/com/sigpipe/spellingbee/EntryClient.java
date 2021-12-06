package com.sigpipe.spellingbee;

import com.sigpipe.definition.dictionaryapi.DefinitionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class EntryClient {

    private static final Logger logger = LogManager.getLogger();
    private static final String API_FORMAT = "https://api.dictionaryapi.dev/api/v2/entries/en/%s";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final CollectionType DEFINITION_RESPONSE_LIST_CLASS = MAPPER.getTypeFactory().constructCollectionType(List.class, DefinitionResponse.class);
    private static final HttpClient INSTANCE = HttpClient.newBuilder().build();

    public SpellingBeeEntry getEntry(final String word) {
        final var apiString = String.format(API_FORMAT, word);
        final var apiUri = URI.create(apiString);
        final var request = HttpRequest.newBuilder(apiUri).GET().build();
        try {
            final var response = INSTANCE.send(request, HttpResponse.BodyHandlers.ofString());
            final var body = response.body();
            final var responseCode = response.statusCode();

            if (responseCode != 200) {
                return new SpellingBeeEntry(word, null, null, null, null, null);
            }

            logger.trace("Fetched definition: {}", body);

            final List<DefinitionResponse> definitionResponseList = MAPPER.readValue(body, DEFINITION_RESPONSE_LIST_CLASS);
            final var definitionResponse = definitionResponseList.get(0);
            final var primaryMeaning = definitionResponse.meanings().get(0);
            final var primaryDefinition = primaryMeaning.definitions().get(0);

            final String pronunciationUrl;
            if(definitionResponse.phonetics().isEmpty()) {
                pronunciationUrl = "";
            } else {
                final var primaryPhonetic = definitionResponse.phonetics().get(0);
                pronunciationUrl = "https:" + primaryPhonetic.audio();
            }



            final var entry = new SpellingBeeEntry(
                    word,
                    definitionResponse.phonetic(),
                    primaryMeaning.partOfSpeech(),
                    primaryDefinition.definition(),
                    primaryDefinition.example(),
                    pronunciationUrl
            );

            logger.info("Created Entry: {}", entry);

            return entry;
        } catch (IOException | InterruptedException e) {
            throw new SpellingBeeEntryRuntimeException(e);
        }
    }

}
