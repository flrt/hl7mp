package org.opikanoba.hl7mp.test;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJsonResource {
    @DisplayName("Test JSON Resource load")
    @Test

    public void testLoadResource() {


        InputStream is = getClass ( ).getResourceAsStream ("/ack.json");
        StringBuilder resultStringBuilder = new StringBuilder ( );

        try {

            BufferedReader br = new BufferedReader (new InputStreamReader (is));
            String line;
            while ((line = br.readLine ( )) != null) {
                resultStringBuilder.append (line).append ("\n");
            }
        } catch (IOException io) {
            assertTrue (io==null);
        }

        JSONObject jobj = new JSONObject (resultStringBuilder.toString ( ));

        assertEquals (6, jobj.getJSONArray ("AE").length ( ));
        assertEquals ("Patient unknown", jobj.getJSONArray ("AE").opt (4));

    }

}
