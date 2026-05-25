package edu.pjwstk.s35267.workaholic.domain;

import edu.pjwstk.s35267.workaholic.infrastructure.contract.Identifiable;
import edu.pjwstk.s35267.workaholic.infrastructure.contract.IdentifiableThread;
import edu.pjwstk.s35267.workaholic.infrastructure.contract.ILogger;

import java.io.IOException;
import java.time.LocalDateTime;

public abstract class ActionLogger {
    private static ILogger logger;

    public static void register(ILogger logger) {
        ActionLogger.logger = logger;
    }

    public static void unregister() {
        ActionLogger.logger = null;
    }

    public static void saveAction(String description)
    {
        ActionLogger.saveAction(description, new Object[0]);
    }

    public static void saveAction(String description, Object[] objects)
    {
        if (ActionLogger.logger == null) {
            return;
        }

        try {
            ActionLogger.logger.log(
                "[" +  LocalDateTime.now() + "] " + description
                + (objects.length > 0 ?  ", related objects: " + ActionLogger.generateClassesDescription(objects) : "")
            );
        } catch (IOException e) {
            System.err.println("Log was not saved: " + e.getMessage());
        }
    }

    private static String generateClassesDescription(Object[] objects) {
        StringBuilder builder = new StringBuilder();
        for (Object object : objects) {
            builder.append(object.getClass().getSimpleName() + " ");
            if (object instanceof Identifiable) {
                builder.append("[#" + ((Identifiable) object).getUnique() + "] ");
            } else if (object instanceof IdentifiableThread) {
                builder.append("[#" + ((IdentifiableThread) object).getUnique() + "] ");
            }
        }

        return builder.toString();
    }
}
