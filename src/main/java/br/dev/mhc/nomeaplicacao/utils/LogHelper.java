package br.dev.mhc.nomeaplicacao.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Objects;

public class LogHelper {

    private final Logger LOG;

    public LogHelper(Class<?> classType) {
        this.LOG = LogManager.getLogger(classType);
    }

    public void fatal(Object object) {
        this.LOG.fatal(object);
    }

    public void fatal(String message, Object... references) {
        String reference = Arrays.stream(references)
                .map(Objects::toString)
                .reduce("", (a, b) -> a.concat("[").concat(b).concat("]"));
        this.LOG.fatal(message.concat(reference));
    }

    public void error(Object object) {
        this.LOG.error(object);
    }

    public void error(String message, Object... references) {
        String reference = Arrays.stream(references)
                .map(Objects::toString)
                .reduce("", (a, b) -> a.concat("[").concat(b).concat("]"));
        this.LOG.error(message.concat(reference));
    }

    public void warn(Object object) {
        this.LOG.warn(object);
    }

    public void warn(String message, Object... references) {
        String reference = Arrays.stream(references)
                .map(Objects::toString)
                .reduce("", (a, b) -> a.concat("[").concat(b).concat("]"));
        this.LOG.warn(message.concat(reference));
    }

    public void info(Object object) {
        this.LOG.info(object);
    }

    public void info(String message, Object... references) {
        String reference = Arrays.stream(references)
                .map(Objects::toString)
                .reduce("", (a, b) -> a.concat("[").concat(b).concat("]"));
        this.LOG.info(message.concat(reference));
    }

    public void debug(Object object) {
        this.LOG.debug(object);
    }

    public void debug(String message, Object... references) {
        String reference = Arrays.stream(references)
                .map(Objects::toString)
                .reduce("", (a, b) -> a.concat("[").concat(b).concat("]"));
        this.LOG.debug(message.concat(reference));
    }

    public void trace(Object object) {
        this.LOG.trace(object);
    }

    public void trace(String message, Object... references) {
        String reference = Arrays.stream(references)
                .map(Objects::toString)
                .reduce("", (a, b) -> a.concat("[").concat(b).concat("]"));
        this.LOG.trace(message.concat(reference));
    }
}
