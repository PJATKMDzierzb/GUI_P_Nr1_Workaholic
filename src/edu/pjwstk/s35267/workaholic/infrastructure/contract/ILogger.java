package edu.pjwstk.s35267.workaholic.infrastructure.contract;

import java.io.IOException;

public interface ILogger {
    void save(String description) throws IOException;
    void close() throws IOException;
}
