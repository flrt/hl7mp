package org.opikanoba.hl7mp.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opikanoba.hl7mp.consumer.ArchiveFileConsumer;
import org.opikanoba.hl7mp.consumer.BasicMessageConsumer;
import org.opikanoba.hl7mp.consumer.DirectoryConsumer;
import org.opikanoba.hl7mp.consumer.MLLPServer;
import org.opikanoba.hl7mp.handlers.*;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Build the configuration according to the configuration file
 */
public class ConfigurationManager {
    private static final Logger logger = LogManager.getLogger(ConfigurationManager.class);

    private String filename;

    public ConfigurationManager(String filename) {
        this.filename = filename;
    }

    /**
     * Create the message consumer.
     *
     * @param config_data : keys/values containing configuration data
     * @param handlers    : Handler list, that will handle each message
     * @return MessageConsumer
     */
    public BasicMessageConsumer createConsumer(Map<String, Object> config_data, List<Handler> handlers) {
        String consumer = (String) config_data.get(ConfigurationKeys.HL7Consumer.getLabel());
        logger.info("Configure Consumer : " + consumer);

        if (ConfigurationKeys.MLLPServer.getLabel().equals(consumer)) {
            Map<String, Object> mllpSettings = (Map<String, Object>) config_data.get(ConfigurationKeys.MLLPServer.getLabel());

            MLLPServer cons = new MLLPServer(handlers);

            // set PORT
            if (mllpSettings.get(ConfigurationKeys.ListenPORT.getLabel()) != null) {
                cons.setPort((Integer) mllpSettings.get(ConfigurationKeys.ListenPORT.getLabel()));
            }

            // set ADDRESS App
            if (mllpSettings.get(ConfigurationKeys.ListenADDRESS.getLabel()) != null) {
                cons.setAddress((String) mllpSettings.get(ConfigurationKeys.ListenADDRESS.getLabel()));
            }
            return cons;
        } else if (ConfigurationKeys.DIRClient.getLabel().equals(consumer)) {
            Map<String, Object> dirSettings = (Map<String, Object>) config_data.get(ConfigurationKeys.DIRClient.getLabel());

            DirectoryConsumer dirc = new DirectoryConsumer(handlers);

            // set Path
            if (dirSettings.get(ConfigurationKeys.DIRPath.getLabel()) != null) {
                dirc.setDirectory((String) dirSettings.get(ConfigurationKeys.DIRPath.getLabel()));
            }
            // set Filename Pattern
            if (dirSettings.get(ConfigurationKeys.FilenamePattern.getLabel()) != null) {
                dirc.setFilenamePattern((String) dirSettings.get(ConfigurationKeys.FilenamePattern.getLabel()));
            }
            return dirc;
        } else if (ConfigurationKeys.ArchiveClient.getLabel().equals(consumer)) {
            Map<String, Object> archSettings = (Map<String, Object>) config_data.get(ConfigurationKeys.ArchiveClient.getLabel());

            ArchiveFileConsumer arc = new ArchiveFileConsumer(handlers);

            // set Path
            if (archSettings.get(ConfigurationKeys.Filename.getLabel()) != null) {
                arc.setFilename((String) archSettings.get(ConfigurationKeys.Filename.getLabel()));
            }
            return arc;
        }

        return null;
    }

    /**
     * Create Message handler. Parses configuration items.
     *
     * @param config_data
     * @param handlerName
     * @return Handler created
     */
    public Handler createHandler(Map<String, Object> config_data, String handlerName) {
        logger.info(" Create " + handlerName);
        Handler hdl = null;
        Map<String, Object> settings;
        String logFileName = null;


        switch (handlerName) {
            case LoggerHandler.NAME:

                settings = (Map<String, Object>) config_data.get(ConfigurationKeys.LoggerHandler.getLabel());
                if (settings.get(ConfigurationKeys.Filename.getLabel()) != null) {
                    logFileName = (String) settings.get(ConfigurationKeys.Filename.getLabel());
                }
                hdl = new LoggerHandler(logFileName);

                break;
            case ACKHandler.NAME:
                double aa = 1, ae = 0, ar = 0;

                settings = (Map<String, Object>) config_data.get(ConfigurationKeys.ACKHandler.getLabel());
                if (settings.get(ConfigurationKeys.ACK_AA.getLabel()) != null) {
                    aa = (Double) settings.get(ConfigurationKeys.ACK_AA.getLabel());
                }
                if (settings.get(ConfigurationKeys.ACK_AE.getLabel()) != null) {
                    ae = (Double) settings.get(ConfigurationKeys.ACK_AE.getLabel());
                }
                if (settings.get(ConfigurationKeys.ACK_AR.getLabel()) != null) {
                    ar = (Double) settings.get(ConfigurationKeys.ACK_AR.getLabel());
                }
                if (settings.get(ConfigurationKeys.Filename.getLabel()) != null) {
                    logFileName = (String) settings.get(ConfigurationKeys.Filename.getLabel());
                }

                ACKHandler ackhdl = new ACKHandler(aa, ae, ar);
                ackhdl.setLoggerFileName(logFileName);

                hdl = ackhdl;

                break;
            case TerserLoggerHandler.NAME:
                settings = (Map<String, Object>) config_data.get(ConfigurationKeys.TerserLoggerHandler.getLabel());
                List<String> tersers = null;

                if (settings.get(ConfigurationKeys.Filename.getLabel()) != null) {
                    logFileName = (String) settings.get(ConfigurationKeys.Filename.getLabel());
                }
                if (settings.get(ConfigurationKeys.Tersers.getLabel()) != null) {
                    logger.info(settings.get(ConfigurationKeys.Tersers.getLabel()));
                    tersers = (List) settings.get(ConfigurationKeys.Tersers.getLabel());
                }
                hdl = new TerserLoggerHandler(logFileName);
                ((TerserLoggerHandler) hdl).setTersers(tersers);

                break;
            case WaitHandler.NAME:
                int delay = 0;
                double scope = 1;

                settings = (Map<String, Object>) config_data.get(ConfigurationKeys.WaitHandler.getLabel());
                if (settings.get(ConfigurationKeys.Delay.getLabel()) != null) {
                    delay = (Integer) settings.get(ConfigurationKeys.Delay.getLabel());
                }
                if (settings.get(ConfigurationKeys.Scope.getLabel()) != null) {
                    scope = (Double) settings.get(ConfigurationKeys.Scope.getLabel());
                }
                if (settings.get(ConfigurationKeys.Filename.getLabel()) != null) {
                    logFileName = (String) settings.get(ConfigurationKeys.Filename.getLabel());
                }

                WaitHandler waithdl = new WaitHandler(delay, scope);
                waithdl.setLoggerFileName(logFileName);

                hdl = waithdl;

                break;
            case BackupHandler.NAME:
                String directory = null;

                settings = (Map<String, Object>) config_data.get(ConfigurationKeys.BackupHandler.getLabel());
                if (settings.get(ConfigurationKeys.Directory.getLabel()) != null) {
                    directory = (String) settings.get(ConfigurationKeys.Directory.getLabel());
                }
                hdl = new BackupHandler(directory);

                break;
            case MLLPClientHandler.NAME:
                MLLPClientHandler mllphdl = new MLLPClientHandler();

                settings = (Map<String, Object>) config_data.get(ConfigurationKeys.MLLPClientHandler.getLabel());
                if (settings.get(ConfigurationKeys.ListenADDRESS.getLabel()) != null) {
                    mllphdl.setServerAddress((String) settings.get(ConfigurationKeys.ListenADDRESS.getLabel()));
                }
                if (settings.get(ConfigurationKeys.ListenPORT.getLabel()) != null) {
                    mllphdl.setPort((Integer) settings.get(ConfigurationKeys.ListenPORT.getLabel()));
                }
                mllphdl.setUseTls(false);

                hdl = mllphdl;
                break;
            default:
                logger.warn("Unknow handler : " + handlerName);
        }

        return hdl;
    }

    /**
     * Read the YAML file, and build the configuration
     *
     * @return Configuration object
     */
    public Configuration read() {
        Yaml yaml = new Yaml();
        Configuration cfg = new Configuration();

        InputStream input;
        try {
            input = new FileInputStream(new File(this.filename));

            Map<String, Object> data = yaml.load(input);
            logger.debug(data);

            // Create Chain of handlers
            logger.debug("chain :" + data.get(ConfigurationKeys.ChainHANFLERS.getLabel()));
            //List<Map<String, String>> handlers = (List<Map<String, String>>) data.get(ConfigurationKeys.ChainHANFLERS.getLabel());
            List<String> handlers = (List<String>) data.get(ConfigurationKeys.ChainHANFLERS.getLabel());
            handlers.forEach(handler -> {
                        Handler hd = createHandler(data, handler);
                        if ((hd != null) && (hd.isValid())) {
                            cfg.addHandler(hd);
                        } else {
                            logger.error("Handler " + handler + " invalid, not added...");
                        }
                    }
            );
            logger.debug(data);


            BasicMessageConsumer bmc = createConsumer(data, cfg.getHandlerList());
            if (bmc != null) {
                cfg.setMessageConsumer(createConsumer(data, cfg.getHandlerList()));
            } else {
                logger.warn("No valid messages consumer found.");
            }

            logger.info("Configuration >> " + cfg);

        } catch (FileNotFoundException e) {
            logger.error("Reading Configuration file KO : " + e.getLocalizedMessage());
        }
        return cfg;
    }

}
