package org.opikanoba.hl7mp.config;


import org.opikanoba.hl7mp.consumer.BasicMessageConsumer;
import org.opikanoba.hl7mp.handlers.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Build the Configuration
 * A configuration is :
 * - a message consumer
 * - a list of handlers, that will handle all the messages
 */
public class Configuration {
    // consumer
    private BasicMessageConsumer messageConsumer;

    // handlers list
    private List<Handler> handlerList;

    /**
     * Default constructor
     */
    public Configuration() {
        this.handlerList = new ArrayList<>();
    }

    public BasicMessageConsumer getMessageConsumer() {
        return messageConsumer;
    }

    public void setMessageConsumer(final BasicMessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    /**
     * Add an handler to the list
     *
     * @param hdl Handler to add
     */
    public void addHandler(final Handler hdl) {
        if (hdl != null) {
            this.handlerList.add(hdl);
        }
    }

    public List<Handler> getHandlerList() {
        return handlerList;
    }

    public void setHandlerList(final List<Handler> handlerList) {
        this.handlerList = handlerList;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "messageConsumer=" + messageConsumer +
                ", handlerList=" + handlerList +
                '}';
    }
}
