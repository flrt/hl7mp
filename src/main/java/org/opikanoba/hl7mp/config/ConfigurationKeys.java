package org.opikanoba.hl7mp.config;

/**
 * Keys used in the configuration files
 */

public enum ConfigurationKeys {
    HL7Consumer("hl7-consumer"),
    MLLPClientHandler("mllp-client-handler"),
    DIRClient("dir-client"),
    ArchiveClient("archive-client"),
    DIRPath("dir-path"),
    MLLPServer("mllp-server"),
    ListenPORT("listen-port"),
    ListenADDRESS("listen-address"),
    ChainHANFLERS("run-handlers"),
    LoggerHandler("logger-handler"),
    TerserLoggerHandler("terser-logger-handler"),
    Tersers("tersers"),
    WaitHandler("wait-handler"),
    BackupHandler("backup-handler"),
    Filename("filename"),
    FilenamePattern("filename-pattern"),
    Directory("directory"),
    Delay("delay"),
    ACKHandler("ack-handler"),
    Scope("scope"),
    ACK_AA("ack-aa"),
    ACK_AE("ack-ae"),
    ACK_AR("ack-ar");

    private final String label;

    ConfigurationKeys(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
