package org.opikanoba.hl7mp.consumer;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opikanoba.hl7mp.handlers.ACKHandler;
import org.opikanoba.hl7mp.handlers.Handler;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

class MessageHandlerApp implements ReceivingApplication<Message> {
    private static final Logger logger = LogManager.getLogger(MLLPServer.class);

    private List<Handler> handlers;

    public MessageHandlerApp(List<Handler> handlers) {
        logger.debug("Build Handler with " + handlers.size() + " participants.");
        this.handlers = handlers;
    }

    @Override
    public Message processMessage(Message message, Map<String, Object> map) throws ReceivingApplicationException, HL7Exception {
        if (handlers != null) {

            Iterator<Handler> it = this.handlers.iterator();
            AbstractMessage msg = (AbstractMessage) message;

            Handler currentHdl = null;

            while (it.hasNext()) {
                currentHdl = it.next();
                logger.debug("Handle message by " + currentHdl.toString());
                msg = currentHdl.handle(msg);
            }

            if ((currentHdl != null) && (currentHdl.getName().equals(ACKHandler.NAME))) {
                return msg;
            } else {
                logger.info("Last handler IS NOT an ACK handler. Generate ACK message...");

                try {
                    logger.debug("ACK the message...");
                    return msg.generateACK();
                } catch (IOException ioe) {
                    logger.error("ACK gen error " + ioe.getLocalizedMessage());
                    throw new HL7Exception(ioe);
                }
            }

        } else {
            logger.warn("No handlers defined !");
            return null;
        }
    }

    @Override
    public boolean canProcess(Message message) {
        return true;
    }
}

public class MLLPServer extends BasicMessageConsumer {
    private static final Logger logger = LogManager.getLogger(MLLPServer.class);

    private int port;
    private String address;
    private boolean useTls;

    public MLLPServer(List<Handler> handlers) {
        super(handlers);
        this.port = 8888;
        this.address = "localhost";
        this.useTls = false;
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "MLLPServer{" +
                "port=" + port +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public void start() {
        logger.info("Starting " + this.toString());

        HapiContext context = new DefaultHapiContext();
        HL7Service server = context.newServer(port, useTls);

        server.registerApplication("*", "*", new MessageHandlerApp(this.getHandlers()));

        try {
            // start HL7 Server in a new thread
            server.startAndWait();

            // make the main thread wait (blocking run)
            while (server.isRunning()) {
                TimeUnit.SECONDS.sleep(10);
            }

        } catch (InterruptedException e) {
            logger.info("Service interrupted.");
        }
    }

    @Override
    public void newMessage(AbstractMessage msg) {

    }
}
