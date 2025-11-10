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
