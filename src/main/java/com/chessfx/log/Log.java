package com.chessfx.log;


import static com.chessfx.log.AnsiColourCode.*;
import static com.chessfx.log.Log.LogType.*;

/**
 * This implements the singleton pattern. See information here at
 * <a href="https://www.digitalocean.com/community/tutorials/java-singleton-design-pattern-best-practices-examples">digitalocean.com</a>.
 * This replaces my 'sout' statements.
 * This is basically a static class
 */
public enum Log {
    LOG;

    private static int threshold = 0;
    private static LogType prevState = DEBUG;

    public static void setThreshold(int threshold) {
        Log.threshold = threshold;
    }

    public static void cont(String s) {
        if (threshold(prevState.priority))
            System.out.println("-> " + s);
    }


    enum LogType {
        INFO("INFO", CYAN, Priority.MED),
        WARN("WARNING", YELLOW, Priority.MED),
        ERROR("ERROR", RED, Priority.HIGH),
        DEBUG("DEBUG", GREEN, Priority.LOW);

        private final Priority priority;
        private final String str;
        private final AnsiColourCode colour;

        LogType(String str, AnsiColourCode colour, Priority priority) {
            this.str = str;
            this.colour = colour;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return "[" + str + "]: ";
        }

        public String colour() {
            return "[" + colour + str + RESET + "]: ";
        }

        public Priority priority() {
            return priority;
        }
    }

    public enum Priority {
        LOW(1),
        MED(10),
        HIGH(100);

        private final int num;

        Priority(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }
    }

    private void output(LogType type, String s) {
        if (threshold(type.priority()))
            System.out.println(type.colour() + s);
    }

    public static void warn(String className, String s) {
        LOG.output(WARN, className, s);
    }

    public static void warn(String s) {
        LOG.output(WARN, s);
    }

    public static void info(String className, String s) {
        LOG.output(INFO, className, s);
    }

    public static void info(String s) {
        LOG.output(INFO, s);
    }

    public static void debug(String className, String s) {
        LOG.output(DEBUG, className, s);
    }

    public static void debug(String s) {
        LOG.output(DEBUG, s);
    }

    public static void debug(Priority priority, String className, String s) {
        LOG.output(priority, DEBUG, className, s);
    }

    private void output(Priority priority, LogType logType, String className, String s) {
        if (threshold(priority)) {
            output(logType, className, s);
        }
    }

    public static void debug(Priority priority, String s) {
        LOG.output(priority, DEBUG, s);
    }

    private void output(Priority priority, LogType logType, String s) {
        if (threshold(priority)) {
            output(logType, s);
        }
    }

    private static boolean threshold(Priority priority) {
        return priority.getNum() > threshold;
    }

    public static void error(String className, String s) {
        LOG.output(ERROR, className, s);
    }

    private void output(LogType type, String className, String message) {
        if (threshold(type.priority))
            System.out.println(type.colour() + className + ": " + message);
    }
}
