package edu.pjwstk.s35267.workaholic.infrastructure;

import edu.pjwstk.s35267.workaholic.infrastructure.contract.ILogger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements AutoCloseable, ILogger {
    private BufferedWriter writer;

    public FileLogger(String filePath) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(filePath, false));
    }

    public void log(String description) throws IOException {
        writer.write(description);
        writer.newLine();
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }
}
