package org.opikanoba.hl7mp.consumer;

import ca.uhn.hl7v2.model.AbstractMessage;

import java.io.IOException;

public interface MessageConsumer {

    void start() throws IOException;

    void newMessage(AbstractMessage msg);
}
