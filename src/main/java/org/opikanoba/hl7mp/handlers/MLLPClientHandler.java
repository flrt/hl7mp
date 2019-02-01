package org.opikanoba.hl7mp.handlers;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MLLPClientHandler implements Handler {
    public static final String NAME = "mllp-client-handler";
    private static final Logger logger = LogManager.getLogger(MLLPClientHandler.class);
    private String serverAddress;
    private int port;
    private boolean useTls;
    private Connection connection;
    private Parser parser;

    public MLLPClientHandler() {
        this("localhost", 8888, false);
    }

    public MLLPClientHandler(String server, int port, boolean useTls) {
        this.serverAddress = server;
        this.port = port;
        this.useTls = useTls;
    }

    @Override
    public AbstractMessage handle(AbstractMessage message) {
        if (this.connection != null) {

            logger.info("Sending HL7 message to " + this.serverAddress + " \n\n " + message);

            Initiator initiator = connection.getInitiator();
            try {
                Message response = initiator.sendAndReceive(message);
                String responseString = this.parser.encode(response);

                logger.info("Response received : \n\n" + responseString);

            } catch (HL7Exception e) {
                logger.error("HL7 Error while sending HL7 message " + e.getLocalizedMessage());
            } catch (LLPException llpe) {
                logger.error("MLLP Error while sending HL7 message " + llpe.getLocalizedMessage());
            } catch (IOException ioe) {
                logger.error("IO Error while sending HL7 message " + ioe.getLocalizedMessage());
            }
        } else {
            logger.error("Connection to " + this.serverAddress + " is NULL :(");
        }
        return message;
    }

    @Override
    public boolean isValid() {
        try {
            InetAddress address = InetAddress.getByName(this.serverAddress);
            logger.info("Server address : " + address.getHostAddress());

        } catch (UnknownHostException e) {
            logger.error("Invalid server address : " + e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @Override
    public void init() {
        HapiContext context = new DefaultHapiContext();
        try {
            this.connection = context.newClient(this.serverAddress, this.port, this.useTls);
            this.parser = context.getPipeParser();

        } catch (HL7Exception e) {
            logger.error("Error while creating HL7 client : " + e.getLocalizedMessage());
        }
    }

    @Override
    public void dispose() {
        if (this.connection != null) {
            this.connection.close();
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isUseTls() {
        return useTls;
    }

    public void setUseTls(boolean useTls) {
        this.useTls = useTls;
    }
}
