package org.opikanoba.hl7mp.consumer;

import ca.uhn.hl7v2.model.AbstractMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opikanoba.hl7mp.handlers.Handler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class DirectoryConsumer extends BasicMessageConsumer {
    private static final Logger logger = LogManager.getLogger(DirectoryConsumer.class);

    private String directory;
    private String filenamePattern;
    private HL7MessageParser hl7Parser;

    public DirectoryConsumer(List<Handler> handlers) {
        super(handlers);

        // default values
        this.directory = System.getProperty("user.home");
        this.filenamePattern = ".*[.]hl7";
        this.hl7Parser = new HL7MessageParser();
    }


    @Override
    public void start() throws IOException {
        logger.info("Starts walking directory : " + this.directory);

        try {
            Stream<Path> paths = Files.list(Paths.get(this.directory));
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().matches(this.filenamePattern))
                    .sorted().forEach(item -> processNewFile(item.toAbsolutePath().toString()));

        } catch (NoSuchFileException nsfe) {
            logger.error("Can not start directory consumer. Check " + nsfe.getLocalizedMessage());
        }
    }

    protected void processNewFile(String filename) {
        AbstractMessage msg = readFile(filename);
        newMessage(msg);
    }

    protected AbstractMessage readFile(String filename) {
        logger.info("Read file : " + filename);

        String content = null;
        AbstractMessage hl7m = null;

        try {
            content = new String(Files.readAllBytes(Paths.get(filename)));
            hl7m = this.hl7Parser.parse(content);

        } catch (IOException eio) {
            logger.error("Error while reading [" + filename + "] : " + eio.getLocalizedMessage());
        }

        return hl7m;
    }


    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getFilenamePattern() {
        return filenamePattern;
    }

    public void setFilenamePattern(String filenamePattern) {
        this.filenamePattern = filenamePattern;
    }

    @Override
    public String toString() {
        return "DirectoryConsumer{" +
                "directory='" + directory + '\'' +
                ", filenamePattern='" + filenamePattern + '\'' +
                '}';
    }
}
