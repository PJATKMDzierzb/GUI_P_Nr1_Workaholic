import edu.pjwstk.s35267.workaholic.domain.ActionLogger;
import edu.pjwstk.s35267.workaholic.infrastructure.FileLogger;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (FileLogger logger = new FileLogger("./app.log")) {
            ActionLogger.register(logger);

            ActionLogger.saveAction("Script execution started...");
            ActionLogger.saveAction("Doing heavy work...");
            ActionLogger.saveAction("Script execution finished.");

            ActionLogger.unregister();
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
