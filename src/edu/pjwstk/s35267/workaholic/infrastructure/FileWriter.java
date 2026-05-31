package edu.pjwstk.s35267.workaholic.infrastructure;

import edu.pjwstk.s35267.workaholic.infrastructure.contract.IWriter;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileWriter implements AutoCloseable, IWriter {
    private BufferedWriter writer;

    public FileWriter(String filePath) throws IOException {
        this.writer = new BufferedWriter(new java.io.FileWriter(filePath, false));
    }

    public void write(String description) throws IOException {
        writer.write(description);
        writer.newLine();
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }
}
