package org.opikanoba.hl7mp.handlers;

import ca.uhn.hl7v2.AcknowledgmentCode;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ACKHandler extends LoggerHandler {
    public static final String NAME = "ack-handler";
    private static final Logger logger = LogManager.getLogger(ACKHandler.class);
    private double ack_aa;
    private double ack_ae;
    private double ack_ar;
    private JSONObject ackMessages;

    public ACKHandler(double aa, double ae, double ar) {
        super();
        this.ack_aa = aa;
        this.ack_ae = ae;
        this.ack_ar = ar;

        loadErrorMessages();
        logger.debug("New ACKHAndler : AA=" + aa + ", AE=" + ae + " AR=" + ar);
    }

    @Override
    public void init() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean isValid() {
        return this.ack_aa + this.ack_ae + this.ack_ar <= 1.0;
    }

    protected void loadErrorMessages() {
        InputStream msgIS = getClass().getResourceAsStream("/ack.json");
        StringBuilder resultStringBuilder = new StringBuilder();

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(msgIS));
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException io) {
            logger.warn("Can not read ACK error messages ! => " + io.getLocalizedMessage());
            resultStringBuilder.append("{\"AE\": [\"App Error\"],\"AR\": [\"unsupported something\"]}");
        }

        this.ackMessages = new JSONObject(resultStringBuilder.toString());
    }

    protected AcknowledgmentCode generateACKCode() {
        double r = Math.random();

        if (r < this.ack_aa) return AcknowledgmentCode.AA;

        if (r > this.ack_aa & r < (this.ack_aa + this.ack_ae)) return AcknowledgmentCode.AE;

        if (r > (this.ack_aa + this.ack_ae)) return AcknowledgmentCode.AR;

        return null;
    }

    protected HL7Exception generateACKErrorMessage(AcknowledgmentCode errCode) {
        JSONArray err = this.ackMessages.getJSONArray(errCode.toString());

        int rand = (int) Math.random() * err.length();
        return new HL7Exception((String) err.opt(rand));
    }


    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public AbstractMessage handle(AbstractMessage message) {

        try {
            AcknowledgmentCode ackCode = generateACKCode();
            logger.debug("ACK the message : " + ackCode);

            HL7Exception hl7err = null;
            if (!AcknowledgmentCode.AA.equals(ackCode)) {
                hl7err = generateACKErrorMessage(ackCode);
                log("ACK message [" + ackCode + "] - " + hl7err.getMessage());
            } else {
                log("ACK message [" + ackCode + "]");
            }

            return (AbstractMessage) message.generateACK(ackCode, hl7err);
        } catch (HL7Exception he) {
            logger.error("ACK gen error : " + he.getLocalizedMessage());
            return null;
        } catch (IOException ioe) {
            logger.error("ACK gen error " + ioe.getLocalizedMessage());
            return null;
        }
    }


    @Override
    public String toString() {
        return "ACKHandler{" +
                "AA=" + ack_aa +
                ", AE=" + ack_ae +
                ", AR=" + ack_ar +
                '}';
    }
}
