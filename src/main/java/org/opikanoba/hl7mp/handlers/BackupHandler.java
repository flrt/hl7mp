package org.opikanoba.hl7mp.handlers;

import ca.uhn.hl7v2.model.AbstractMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opikanoba.hl7mp.consumer.HL7MessageParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupHandler extends LoggerHandler {
    public static final String NAME = "backup-handler";
    private static final Logger logger = LogManager.getLogger(BackupHandler.class);
    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String fileNameExtension = ".hl7";
    private String directory;
    private HL7MessageParser hl7m;


    public BackupHandler(String directory) {
        super();
        this.directory = directory;

        this.hl7m = new HL7MessageParser();
        logger.info("BackupHandler - directory=" + directory);
    }

    protected File getRootDirectory() {
        // root directory, either defined or temp dir
        File rootdir = new File(this.directory);

        if (!rootdir.exists()) {
            if (!rootdir.mkdirs()) {
                try {
                    rootdir = Files.createTempDirectory(NAME).toFile();
                } catch (IOException ioe) {
                    logger.warn("Error while creating temp dir : " + ioe.getLocalizedMessage());
                    rootdir = null;
                }
                logger.info("Creating directory : " + rootdir.getAbsolutePath());
            }
        }
        return rootdir;
    }

    @Override
    public void init() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean isValid() {
        File rootdir = getRootDirectory();
        return (rootdir != null);
    }

    public String getDirectory() {
        File rootdir = getRootDirectory();
        File completedir = null;

        // relative directory
        if (rootdir != null) {
            LocalDateTime d = LocalDateTime.now();
            String datedir = d.format(timeFormat);
            completedir = Paths.get(rootdir.getAbsolutePath(), datedir).toAbsolutePath().toFile();
            if (!completedir.exists()) {
                try {
                    logger.info("Creating directory : " + completedir.getAbsolutePath());
                    completedir.mkdirs();

                } catch (SecurityException se) {
                    logger.warn("Error creating dir : " + completedir.getAbsolutePath() + " : " + se.getLocalizedMessage());
                }
            }

        }

        if (completedir != null) {
            return completedir.getAbsolutePath();
        }
        return null;
    }

    public String getFileName(AbstractMessage message) {
        int nano = LocalDateTime.now().getNano();
        String root = this.hl7m.getValue(message, "MSH-7");

        return root + "_" + nano + fileNameExtension;
    }

    public void saveMessage(String directory, String filename, AbstractMessage message) {
        String dir = getDirectory();

        if (dir != null) {
            File outFile = Paths.get(directory, filename).toFile();
            logger.debug("Backuk message into : " + outFile.getAbsolutePath());

            try (final Writer writer = new BufferedWriter(new FileWriter(outFile))) {
                writer.write(message.toString());
            } catch (IOException e) {
                logger.warn("Error in writing message : " + e.getLocalizedMessage());
            }
        }

    }

    @Override
    public AbstractMessage handle(AbstractMessage message) {
        String fname = getFileName(message);
        String dir = getDirectory();

        if (dir != null) {
            logger.info("Backup message (" + dir + ")-> " + fname);
            saveMessage(dir, fname, message);
        }

        return message;
    }
}
