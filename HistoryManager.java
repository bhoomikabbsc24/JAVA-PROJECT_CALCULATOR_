import java.io.*;
import java.nio.file.*;
import java.util.*;

public class HistoryManager {
    private final Path historyFile;

    public HistoryManager() {
        this("history.txt");
    }// to add code later
