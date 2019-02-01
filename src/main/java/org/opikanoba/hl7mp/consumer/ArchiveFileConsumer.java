package org.opikanoba.hl7mp.consumer;

import ca.uhn.hl7v2.model.AbstractMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opikanoba.hl7mp.handlers.Handler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArchiveFileConsumer extends BasicMessageConsumer {
    private static final Logger logger = LogManager.getLogger(ArchiveFileConsumer.class);
    private final Pattern newMessagePattern = Pattern.compile(".*?\\}\\}(MSH.*)", Pattern.DOTALL);
    private String filename;
    private HL7MessageParser hl7Parser;


    public ArchiveFileConsumer(List<Handler> handlers) {
        super(handlers);
        this.hl7Parser = new HL7MessageParser();

    }

    /**
     * Use of StringBuilder because the parsing of the file is not multithreaded
     *
     * @throws IOException
     */
    @Override
    public void start() throws IOException {
        logger.info("Starts reading file : " + this.filename);

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.filename),
                    StandardCharsets.UTF_8));

            String line;

            StringBuilder currentMessage = new StringBuilder();

            while ((line = br.readLine()) != null) {
                Matcher currentMatch = this.newMessagePattern.matcher(line);

                if (currentMatch.matches()) {
                    if (currentMessage != null) {
                        // process the completed message
                        AbstractMessage msg = this.hl7Parser.parse(currentMessage.toString());
                        newMessage(msg);
                    }
                    // init a new message
                    currentMessage.delete(0, currentMessage.length());
                    // cut the beginning by getting only the matching group
                    currentMessage.append(currentMatch.group(1));
                } else {
                    currentMessage.append(line);
                }
            }
            br.close();

        } catch (IOException e) {
            logger.error("Error while reading file : " + e.getLocalizedMessage(), e);
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
