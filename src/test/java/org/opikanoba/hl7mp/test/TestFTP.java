package org.opikanoba.hl7mp.test;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

public class TestFTP {
    @DisplayName("Simple Test - ftp list")
    @Test
    public void testList() throws IOException {
        FTPClient ftpClient = new FTPClient( );
        ftpClient.connect ("ftp.cluster010.ovh.net");
        ftpClient.login ("opikanob", "H0bb3s367");
        System.out.println(ftpClient);

        FTPFile[] files = ftpClient.listFiles ("/www/data/ihe/palm");

        Arrays.stream (files)
                .filter(ftpFile -> ftpFile.isFile() && ftpFile.getName().matches(".*?[.]hl7"))
                .forEach(ftpFile -> System.out.println(ftpFile));

    }
    }
