package org.opikanoba.hl7mp.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestRegex {
    Pattern pattern;
    final static String STR1 = "CORACORA{composant {{chrono {{nummes {{#text 64859875}}} {mesmetier {{#text 263}}}}} {patient {{ipp {{#text 0004148738}}}}} {mouvement {{dern_mvt {{#text O}}} {ufm {{#text 2958}}} {ufh {{#text 2958}}} {id_rm_pam {{#text {}}}} {cdmed_rm {{#text {}}}}}}}}MSH|^~\\&|ADT_MIPIH|MIPIH|CORA|CORA|20180916001830||ADT^A38^ADT_A38|64859876_EH08_263|P|2.5|||||FR|8859/1|FR\n";
    final static String STR2 = "PD1||U||||||||||N\n";

    @BeforeEach
    void setUp() {
        String newMessagePattern = ".*?\\}\\}(MSH.*)";
        pattern = Pattern.compile(newMessagePattern, Pattern.DOTALL);

    }

    @DisplayName("Simple Test - regex found")
    @Test
    public void testFound(){
        Matcher m = pattern.matcher(STR1);

        assertEquals(true, m.matches());
        assertEquals(1, m.groupCount());
        assertEquals("MSH", m.group(1).substring(0,3));
    }

    @DisplayName("Simple Test - regex not found")
    @Test
    public void testNotFound(){
        Matcher m = pattern.matcher(STR2);

        assertEquals(false, m.matches());
        assertEquals(1, m.groupCount());

    }

}