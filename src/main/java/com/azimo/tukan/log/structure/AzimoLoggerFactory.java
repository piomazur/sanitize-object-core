package com.azimo.tukan.log.structure;

import com.azimo.tukan.log.structure.model.ExceptionDetails;
import com.azimo.tukan.log.structure.model.ProcessDetails;
import com.azimo.tukan.log.structure.utils.ExceptionMessagesBuilder;
import net.logstash.logback.argument.StructuredArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.logstash.logback.argument.StructuredArguments.kv;

public final class AzimoLoggerFactory implements Logger {

    private static final Set PREDEFINED_KEYS = new HashSet(Arrays.asList("extra", "exception"));

    private final AzimoExtendedLogger logger;

    private final Map<String, Object> keyValueMap = new HashMap<>();

    public void put(final String key, final Object value) {

        if (PREDEFINED_KEYS.contains(key)) {
            throw new IllegalArgumentException("Provided key " + key + " is in predefined list. Use different key.");
        }

        keyValueMap.put(key, value);
    }

    public void putProcessDetails(final ProcessDetails processDetails) {
        keyValueMap.put("extra", processDetails);
    }

    public void removeProcessDetails() {
        keyValueMap.remove("extra");
    }

    public void clearDetails() {
        keyValueMap.clear();
    }

    public AzimoLoggerFactory(final Class<?> clazz) {
        logger = AzimoExtendedLogger.create(clazz);
    }

    public static AzimoExtendedLogger getLogger(String name) {
        return AzimoExtendedLogger.create(name);
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public void trace(final String var1) {
        logger.trace(var1, provideStructuredKeyValue());
    }

    @Override
    public void trace(final String var1, final Object... var2) {
        logger.trace(var1, provideStructuredKeyValue(var2));
    }

    @Override
    public void trace(final String var1, final Throwable var2) {
        logger.trace(var1, provideStructuredException(var2));
    }


    @Override
    public void debug(final String var1) {
        logger.debug(var1, provideStructuredKeyValue());
    }

    @Override
    public void debug(final String var1, final Object... var2) {
        logger.debug(var1, provideStructuredKeyValue(var2));
    }

    @Override
    public void debug(final String var1, final Throwable var2) {
        logger.debug(var1, provideStructuredException(var2));
    }


    @Override
    public void info(final String var1) {
        logger.info(var1, provideStructuredKeyValue());
    }

    @Override
    public void info(final String var1, final Object... var2) {
        logger.info(var1, provideStructuredKeyValue(var2));
    }

    @Override
    public void info(final String var1, final Throwable var2) {
        logger.info(var1, provideStructuredException(var2));
    }


    @Override
    public void warn(final String var1) {
        logger.warn(var1, provideStructuredKeyValue());
    }

    @Override
    public void warn(final String var1, final Object... var2) {
        logger.warn(var1, provideStructuredKeyValue(var2));
    }

    @Override
    public void warn(final String var1, final Throwable var2) {
        logger.warn(var1, provideStructuredException(var2));
    }


    @Override
    public void error(final String var1) {
        logger.error(var1, provideStructuredKeyValue());
    }

    @Override
    public void error(final String var1, final Object... var2) {
        logger.error(var1, provideStructuredKeyValue(var2));
    }

    @Override
    public void error(final String var1, final Throwable var2) {
        logger.error(var1, provideStructuredException(var2));
    }


    @Override
    public void critical(final String var1) {
        logger.critical(var1, provideStructuredKeyValue());
    }

    @Override
    public void critical(final String var1, final Object... var2) {
        logger.critical(var1, provideStructuredKeyValue(var2));
    }

    @Override
    public void critical(final String var1, final Throwable var2) {
        logger.critical(var1, provideStructuredException(var2));
    }

    private StructuredArgument provideStructuredException(final Throwable var2) {
        return kv("exception", new ExceptionDetails(var2.getMessage(), ExceptionMessagesBuilder.build(var2), null));
    }

    private Object[] provideStructuredKeyValue(final Object... vars) {
        return Stream.concat(Stream.of(vars),
                keyValueMap.entrySet().stream()
                        .map(entry -> kv(entry.getKey(), entry.getValue())))
                .collect(Collectors.toList())
                .toArray(new Object[0]);
    }
}

