package com.azimo.tukan.log.structure;

public interface Logger {
    String ROOT_LOGGER_NAME = "ROOT";

    String getName();

    void trace(String var1);

    void trace(String var1, Object... var2);

    void trace(String var1, Throwable var2);


    void debug(String var1);

    void debug(String var1, Object... var2);

    void debug(String var1, Throwable var2);


    void info(String var1);

    void info(String var1, Object... var2);

    void info(String var1, Throwable var2);


    void warn(String var1);

    void warn(String var1, Object... var2);

    void warn(String var1, Throwable var2);


    void error(String var1);

    void error(String var1, Object... var2);

    void error(String var1, Throwable var2);


    void critical(String var1);

    void critical(String var1, Object... var2);

    void critical(String var1, Throwable var2);
}

