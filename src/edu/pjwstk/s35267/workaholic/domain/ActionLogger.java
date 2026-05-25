package edu.pjwstk.s35267.workaholic.domain;

import edu.pjwstk.s35267.workaholic.infrastructure.contract.Identifiable;
import edu.pjwstk.s35267.workaholic.infrastructure.contract.IdentifiableThread;
import edu.pjwstk.s35267.workaholic.infrastructure.contract.ILogger;

import java.io.IOException;

public abstract class ActionLogger {
    private static ILogger logger;

    public static void register(ILogger logger) {
        ActionLogger.logger = logger;
    }

    public static void unregister() {
        ActionLogger.logger = null;
    }

    public static void saveAction(String description, Object[] objects)
    {
        if (ActionLogger.logger == null) {
            throw new RuntimeException("No logger registered, cannot persist an action");
        }

        try {
            ActionLogger.logger.save(
                description + ", related objects: " + ActionLogger.generateClassesDescription(objects)
            );
        } catch (IOException e) {
            System.out.println("Log was not saved: " + e.getMessage());
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
