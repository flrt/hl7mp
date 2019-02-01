package org.opikanoba.hl7mp.consumer;

import ca.uhn.hl7v2.model.AbstractMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opikanoba.hl7mp.handlers.Handler;

import java.util.List;

public abstract class BasicMessageConsumer implements MessageConsumer {
    private static final Logger logger = LogManager.getLogger(BasicMessageConsumer.class);

    private List<Handler> handlers;


    public BasicMessageConsumer(List<Handler> handlers) {
        this.handlers = handlers;
    }

    public List<Handler> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<Handler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void newMessage(AbstractMessage msg) {
        logger.info("new message : " + msg.toString().length());
        logger.info("handlers count : " + handlers.size());

        handlers.forEach(handler -> handler.handle(msg));
    }

}
