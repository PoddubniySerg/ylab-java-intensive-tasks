package io.ylab.intensive.lesson05.messagefilter.processors;

import io.ylab.intensive.lesson05.messagefilter.clients.MessageBrokerClient;
import io.ylab.intensive.lesson05.messagefilter.clients.WordsDbClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

@Component
public class MessageProcessor {

    private final WordsDbClient validator;
    private final MessageBrokerClient messageClient;
    private final Set<Character> limitingChars;

    public MessageProcessor(WordsDbClient validator, MessageBrokerClient messageClient) {
        this.validator = validator;
        this.messageClient = messageClient;
        this.limitingChars = getLimitingChars();
    }

    public void processSingleMessage() throws IOException, TimeoutException, SQLException {
        String message = messageClient.getMessage();
        if (message == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int length = message.length();
        int first = -1;
        for (int i = 0; i < length; i++) {
            Character character = message.charAt(i);
            if (first < 0 && !limitingChars.contains(character)) {
                first = i;
            } else if (first < 0) {
                stringBuilder.append(character);
            } else if (first >= 0 && limitingChars.contains(character) || i == length - 1) {
                StringBuilder word = nextWord(first, i, message);
                if (validator.isForbidden(word.toString())) {
                    appendConverted(word, stringBuilder);
                } else {
                    stringBuilder.append(word);
                }
                stringBuilder.append(character);
                first = -1;
            }
        }
        messageClient.sendMessage(stringBuilder.toString());
    }

    private StringBuilder nextWord(int firstCharIndex, int lastCharIndex, String message) {
        final StringBuilder word = new StringBuilder();
        for (int j = firstCharIndex; j < lastCharIndex; j++) {
            word.append(message.charAt(j));
        }
        return word;
    }

    private void appendConverted(StringBuilder word, StringBuilder stringBuilder) {
        final int length = word.length();
        for (int i = 0; i < length; i++) {
            if (i == 0 || i == length - 1) {
                stringBuilder.append(word.charAt(i));
            } else {
                stringBuilder.append('*');
            }
        }
    }

    private Set<Character> getLimitingChars() {
        final Set<Character> chars = new HashSet<>();
        chars.add(' ');
        chars.add('.');
        chars.add(',');
        chars.add(';');
        chars.add('?');
        chars.add('!');
        return chars;
    }
}
