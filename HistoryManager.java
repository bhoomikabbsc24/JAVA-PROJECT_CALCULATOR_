import java.io.*;
import java.nio.file.*;
import java.util.*;

public class HistoryManager {
    private final Path historyFile;

    public HistoryManager() {
        this("history.txt");
    }
    public HistoryManager(String filename) {
        historyFile = Paths.get(filename);
        try {
            if (!Files.exists(historyFile)) Files.createFile(historyFile);
        } catch (IOException e) {
            // ignore - will create on append
        }
    }
public void append(String expr, String result) {
        String line = java.time.LocalDateTime.now() + " | " + expr + " = " + result + System.lineSeparator();
        try {
            Files.write(historyFile, line.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> readAll() {
        try {
            if (!Files.exists(historyFile)) return new ArrayList<>();
            return Files.readAllLines(historyFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
public void clear() {
        try {
            Files.write(historyFile, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
