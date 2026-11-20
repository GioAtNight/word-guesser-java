package wordguesser.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class WordService {
    private static final String DATA_DIR = "data";
    private static final String WORD_FILE = DATA_DIR + File.separator + "words.txt";

    private final List<String> words = new ArrayList<>();
    private final Random random = new Random();

    public WordService() {
        ensureWordFile();
        loadWords();
    }

    private void ensureWordFile() {
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }

            Path wordPath = Paths.get(WORD_FILE);
            if (!Files.exists(wordPath)) {
                Files.createFile(wordPath);
                writeDefaultWords();
            } else if (Files.size(wordPath) == 0) {
                writeDefaultWords();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize word storage.", e);
        }
    }

    private void writeDefaultWords() {
        List<String> defaultWords = Arrays.asList(
            "game", "code", "java", "play", "grid",
            "apple", "grape", "mango", "planet", "rocket",
            "castle", "orange", "button", "window", "screen",
            "mouse", "keyboard", "puzzle", "bridge", "garden",
            "library", "diamond", "monster", "teacher", "battery",
            "science", "program", "network", "picture", "printer"
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WORD_FILE))) {
            for (String word : defaultWords) {
                writer.write(word.toLowerCase());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write default words.", e);
        }
    }

    private void loadWords() {
        words.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(WORD_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase();
                if (word.matches("[a-zA-Z]+") && word.length() >= 4 && word.length() <= 8) {
                    words.add(word);
                }
            }

            System.out.println("Loaded words: " + words.size());

        } catch (IOException e) {
            throw new RuntimeException("Failed to load words.", e);
        }
    }

    public List<Integer> getAvailableLengths() {
        List<Integer> lengths = new ArrayList<>();
        for (int i = 4; i <= 8; i++) {
            lengths.add(i);
        }
        return lengths;
    }

    public String getRandomWord(int length) {
        if (length < 4 || length > 8) {
            return null;
        }

        List<String> filtered = new ArrayList<>();
        for (String word : words) {
            if (word.length() == length) {
                filtered.add(word);
            }
        }

        if (filtered.isEmpty()) {
            return null;
        }

        return filtered.get(random.nextInt(filtered.size()));
    }

    public boolean isValidWord(String word, int length) {
        if (word == null) return false;
        word = word.toLowerCase().trim();
        if (length < 4 || length > 8) return false;
        if (word.length() != length) return false;
        return words.contains(word);
    }
}