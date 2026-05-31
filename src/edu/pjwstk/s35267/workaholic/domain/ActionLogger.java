package edu.pjwstk.s35267.workaholic.domain;

import edu.pjwstk.s35267.workaholic.infrastructure.contract.Identifiable;
import edu.pjwstk.s35267.workaholic.infrastructure.contract.IdentifiableThread;
import edu.pjwstk.s35267.workaholic.infrastructure.contract.IWriter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * As we lack any DI and I refuse to just handle everything in one service,
 * I had to implement some singleton kind of static service that accepts
 * different kinds of loggers for the script execution.
 * By separating file handler from the logger we can actually still use the
 * try-catch mechanism.
 *
 * Not so sure about best practises in Java framework-less env though :<
 * but seems okay to me.
 */
public abstract class ActionLogger {
    private static IWriter logger;

    public static void register(IWriter logger) {
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
            ActionLogger.logger.write(
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
