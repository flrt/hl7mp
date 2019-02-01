package org.opikanoba.hl7mp.handlers;

import ca.uhn.hl7v2.model.AbstractMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opikanoba.hl7mp.consumer.HL7MessageParser;

import java.util.List;

public class TerserLoggerHandler extends LoggerHandler {
    public static final String NAME = "terser-logger-handler";
    private static final Logger logger = LogManager.getLogger(TerserLoggerHandler.class);
    private HL7MessageParser hl7m;
    private List<String> tersers;

    public TerserLoggerHandler(String filename) {
        super(filename);

        this.hl7m = new HL7MessageParser();
    }

    @Override
    public AbstractMessage handle(AbstractMessage message) {
        logger.debug("handle new Message");
        log("\n");
        if (this.tersers != null) {
            this.tersers.forEach(terser -> {
                String r = this.hl7m.getValue(message, terser);
                log(terser + "=" + this.hl7m.getValue(message, terser));
            });

        }
        return message;
    }

    public List<String> getTersers() {
        return tersers;
    }

    public void setTersers(List<String> tersers) {
        this.tersers = tersers;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
