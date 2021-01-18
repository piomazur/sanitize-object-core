package com.azimo.tukan.log.structure.utils;

import com.azimo.tukan.log.structure.AzimoLoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ExceptionMessagesBuilder {

    private static AzimoLoggerFactory log = new AzimoLoggerFactory(ExceptionMessagesBuilder.class);
    private static final int STACKTRACE_MAX_LENGTH = 10;
    private static final String COM_AZIMO = "com.azimo.";

    public static String build(final Throwable exception,
                               final int stackTraceMaxLength,
                               final String packagePrefix) {
        try {
            int recurrencyCounter = 0;
            List<String> output = new ArrayList<>();
            output.add(exception.getClass().getName() + "(" + exception.getMessage() + ")");

            collectCauseMessages(exception, output, recurrencyCounter);
            collectAzimoStacktrace(exception, output, stackTraceMaxLength, packagePrefix);

            return String.join(" ", output);
        } catch (Exception e) {
            // just in case
            log.warn("Could not collect exception messages with azimo stacktrace. " + e.getMessage(), e);
            log.warn("Original exception.", exception);

            return exception.getMessage();
        }
    }

    public static String build(final Throwable exception) {
        return build(exception, STACKTRACE_MAX_LENGTH, COM_AZIMO);
    }

    private static void collectAzimoStacktrace(final Throwable e,
                                               final List<String> output,
                                               final int stacktraceMaxLength,
                                               final String packagePrefix) {


        final Predicate<StackTraceElement> stackTraceElementPredicate = stackTraceElement -> {
            if ("*".equals(packagePrefix)) {
                return true;
            }
            return stackTraceElement.getClassName().startsWith(packagePrefix);
        };

        final List<String> azimoStacktrace = Stream.of(e.getStackTrace())
                .filter(stackTraceElementPredicate)
                .map(element -> System.lineSeparator() + element.getClassName() + "." + element.getMethodName() + ":" + element.getLineNumber())
                .limit(stacktraceMaxLength)
                .collect(Collectors.toList());

        if (azimoStacktrace.isEmpty()) {
            return;
        }

        output.add(System.lineSeparator());
        output.add(System.lineSeparator() + "Stacktrace:");
        output.addAll(azimoStacktrace);
        output.add(System.lineSeparator());
        output.add(System.lineSeparator());
    }

    private static void collectCauseMessages(final Throwable e, final List<String> messages, int recurrencyCounter) {
        if (e.getCause() == null) {
            return;
        }

        recurrencyCounter++;
        final Throwable cause = e.getCause();
        addMessageInNewLine(messages, cause);

        if (recurrencyCounter < 10) {
            collectCauseMessages(cause, messages, recurrencyCounter);
        } else {
            log.warn("Stop collecting cause messages, we are going to deep!");
        }

    }

    private static void addMessageInNewLine(final List<String> messages, final Throwable cause) {
        if (!StringUtils.isEmpty(cause.getMessage())) {
            messages.add(System.lineSeparator() + "caused by: " + cause.getMessage());
        }
    }

    private ExceptionMessagesBuilder() {
    }

}
