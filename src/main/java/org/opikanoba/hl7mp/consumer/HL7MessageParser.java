package org.opikanoba.hl7mp.consumer;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * HL7 Message Parser
 * - Parse an HL7 message v2 into an object
 * - get value according to a terser expression
 */
public class HL7MessageParser {
    private static final Logger logger = LogManager.getLogger(HL7MessageParser.class);

    private PipeParser pparser;

    public HL7MessageParser() {
        HapiContext context = new DefaultHapiContext();
        this.pparser = context.getPipeParser();
    }

    /**
     * Parse the String content : HL7 v2 message with pipes
     *
     * @param content : HL7 V2 message
     * @return Message object
     */
    public AbstractMessage parse(String content) {
        AbstractMessage msg = null;
        try {
            msg = (AbstractMessage) this.pparser.parse(content);

        } catch (HL7Exception e) {
            logger.error("HL7 Parsing Error : " + e.getLocalizedMessage());

        }
        return msg;
    }

    /**
     * Get the value of the field defined by the terser
     *
     * @param message HL7 message object
     * @param expr    Terser expression
     * @return Value of the parser, empty string if something goes wrong
     */
    public String getValue(AbstractMessage message, String expr) {
        Terser t = new Terser(message);
        try {
            return t.get(expr);
        } catch (HL7Exception e) {
            logger.warn("Error in Terser [" + expr + "] evaluation : " + e.getLocalizedMessage());
            return "";
        }
    }
}
