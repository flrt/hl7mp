package org.opikanoba.hl7mp;


import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opikanoba.hl7mp.config.Configuration;
import org.opikanoba.hl7mp.config.ConfigurationManager;
import org.opikanoba.hl7mp.consumer.MessageConsumer;

import java.io.File;
import java.io.IOException;
import java.util.Date;


public class App {
    private static final Logger logger = LogManager.getLogger(App.class);
    Configuration config;

    public App(Configuration cfg) {
        this.config = cfg;
    }

    public static void main(String[] args) {
        System.out.println("App starting " + new Date());
        System.setProperty("log4j.configurationFile", "log4j2.xml");
        logger.info("App starts...");

        Options options = new Options();
        options.addRequiredOption("cfg", "config", true, "configuration file name");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            String configFilename = cmd.getOptionValue("cfg");

            File configFile = new File(configFilename);
            if (configFile.exists()) {

                logger.debug("Reading : " + configFile.getAbsolutePath());
                ConfigurationManager cfgr = new ConfigurationManager(configFile.getAbsolutePath());
                Configuration cfg = cfgr.read();
                logger.debug("Configuration : " + cfg);

                App mainApp = new App(cfg);
                mainApp.registerKeyboardInterrupt();

                mainApp.process();
                mainApp.shutdown();
            } else {
                logger.error("File " + configFilename + " does not exist.");
            }
        } catch (ParseException e) {
            logger.error("Error in command line : " + e.getLocalizedMessage());
        }
    }

    public void process() {
        // starts message consumer
        MessageConsumer msgcons = this.config.getMessageConsumer();
        logger.info("Consumer = " + msgcons);

        if (msgcons == null) {
            logger.error("No message Consumer defined.");
            return;
        }

        if (this.config.getHandlerList().size() == 0) {
            logger.error("No handler configured.");
            return;
        }

        logger.info("Starts Messages Consumer : " + msgcons);
        try {
            logger.debug("Init handlers");
            this.config.getHandlerList().forEach(hd -> hd.init());

            logger.debug("Starts consumer processing : " + msgcons);
            msgcons.start();

        } catch (IOException e) {
            logger.error("Process Error : " + e.getLocalizedMessage(), e);
        }
    }

    /**
     * Shutdown the main app, dispose all the handlers
     */
    public void shutdown() {
        logger.debug("Dispose Handlers");
        this.config.getHandlerList().forEach(hd -> hd.dispose());
    }

    /**
     * Safe shutdown if Ctrl-C has interrupted main app
     */
    public void registerKeyboardInterrupt() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Interrupt received, shut down app...");
            shutdown();
        }));
    }


}
