package org.opikanoba.hl7mp.handlers;

import ca.uhn.hl7v2.model.AbstractMessage;


public interface Handler {
    void init();

    void dispose();

    AbstractMessage handle(AbstractMessage message);

    boolean isValid();

    String getName();
}
