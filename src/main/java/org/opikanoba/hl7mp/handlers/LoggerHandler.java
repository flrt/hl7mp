package org.opikanoba.hl7mp.handlers;

import ca.uhn.hl7v2.model.AbstractMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;


public class LoggerHandler implements Handler {
    public static final String NAME = "logger-handler";

    private static final Logger logger = LogManager.getLogger(LoggerHandler.class);

    private java.util.logging.Logger fileLogger;
    private String loggerFileName = null;

    public LoggerHandler() {
    }

    public LoggerHandler(String filename) {
        this.loggerFileName = filename;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void init() {
        this.fileLogger = java.util.logging.Logger.getLogger(getName());
        StreamHandler hdl;
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] %5$s %n");
        try {
            hdl = new FileHandler(this.loggerFileName);
        } catch (IOException e) {
            logger.warn("Error while creating file logger : " + e.getLocalizedMessage());
            hdl = new ConsoleHandler();
        }

        this.fileLogger.addHandler(hdl);
        SimpleFormatter formatter = new SimpleFormatter();
        hdl.setFormatter(formatter);
    }

    @Override
    public void dispose() {
    }

    public void log(String str) {
        if (this.fileLogger != null) {
            this.fileLogger.info(str);
        }
    }

    @Override
    public AbstractMessage handle(AbstractMessage message) {
        logger.debug("handle new Message");
        log("------------------- Message -------------------\n" +
                message.toString().replace('\r', '\n'));
        log("\n-----------------------------------------------");

        return message;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public String getLoggerFileName() {
        return loggerFileName;
    }

    public void setLoggerFileName(String loggerFileName) {
        this.loggerFileName = loggerFileName;
    }
}
