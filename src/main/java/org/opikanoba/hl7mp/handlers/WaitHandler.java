package org.opikanoba.hl7mp.handlers;

import ca.uhn.hl7v2.model.AbstractMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class WaitHandler extends LoggerHandler {
    public static final String NAME = "wait-handler";
    private static final Logger logger = LogManager.getLogger(WaitHandler.class);
    private int delay;
    private double scope;

    public WaitHandler(int delay, double scope) {
        super();
        logger.info("Init Wait Handler with delay=" + delay + " seconds, scope=" + scope);
        this.delay = delay;
        this.scope = scope;
    }

    @Override
    public AbstractMessage handle(AbstractMessage message) {
        double rand = Math.random();
        logger.debug("Proba " + rand + " / " + this.scope + " Wait for " + this.delay + " seconds");

        if (rand < this.scope) {
            if (this.delay > 0) {
                logger.debug("Wait for " + this.delay + " seconds");
                log("Wait for " + this.delay + " seconds");
                try {
                    TimeUnit.SECONDS.sleep(this.delay);
                } catch (InterruptedException e) {
                    logger.error(e.getLocalizedMessage());
                }
            }
        }
        return message;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
