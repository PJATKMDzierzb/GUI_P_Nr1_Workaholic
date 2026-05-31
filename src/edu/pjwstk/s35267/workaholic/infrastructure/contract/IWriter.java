package edu.pjwstk.s35267.workaholic.infrastructure.contract;

import java.io.IOException;

public interface IWriter {
    void write(String description) throws IOException;
}
