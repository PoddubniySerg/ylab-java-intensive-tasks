package lesson03.transliterator;

import java.util.Map;

public class TransliteratorImpl implements Transliterator {

    private final Map<Character, String> map;

    public TransliteratorImpl() {
        TransliteratorUtils mapGenerator = new TransliteratorUtils();
        this.map = mapGenerator.getTransliterationMap();
    }

    @Override
    public String transliterate(String source) {
        StringBuilder stringBuilder = new StringBuilder();
        source.chars().forEach(c -> stringBuilder.append(transliterate(c)));
        return stringBuilder.toString();
    }

    private String transliterate(int c) {
        final char character = (char) c;
        return map.getOrDefault(character, String.valueOf(character));
    }
}
