package org.opikanoba.hl7mp.test;


import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTerser {
    String msgStr_a01;
    String msgStr_a03;
    AbstractMessage aMsg;
    Parser pparser;

    @BeforeEach
    void setUp() {
        msgStr_a01 = "MSH|^~\\&|HIS|RIH|EKG|EKG|199904140038||ADT^A01|12345|P|2.2\r"
                + "PID|0001|00009874|00001122|A00977|SMITH^JOHN^M|MOM|19581119|F|NOTREAL^LINDA^M|C|564 SPRING ST^^NEEDHAM^MA^02494^US|0002|(818)565-1551|(425)828-3344|E|S|C|0000444444|252-00-4414||||SA|||SA||||NONE|V1|0001|I|D.ER^50A^M110^01|ER|P00055|11B^M011^02|070615^BATMAN^GEORGE^L|555888^NOTREAL^BOB^K^DR^MD|777889^NOTREAL^SAM^T^DR^MD^PHD|ER|D.WT^1A^M010^01|||ER|AMB|02|070615^NOTREAL^BILL^L|ER|000001916994|D||||||||||||||||GDD|WA|NORM|02|O|02|E.IN^02D^M090^01|E.IN^01D^M080^01|199904072124|199904101200|199904101200||||5555112333|||666097^NOTREAL^MANNY^P\r"
                + "NK1|0222555|NOTREAL^JAMES^R|FA|STREET^OTHER STREET^CITY^ST^55566|(222)111-3333|(888)999-0000|||||||ORGANIZATION\r"
                + "PV1|0001|I|D.ER^1F^M950^01|ER|P000998|11B^M011^02|070615^BATMAN^GEORGE^L|555888^OKNEL^BOB^K^DR^MD|777889^NOTREAL^SAM^T^DR^MD^PHD|ER|D.WT^1A^M010^01|||ER|AMB|02|070615^VOICE^BILL^L|ER|000001916994|D||||||||||||||||GDD|WA|NORM|02|O|02|E.IN^02D^M090^01|E.IN^01D^M080^01|199904072124|199904101200|||||5555112333|||666097^DNOTREAL^MANNY^P\r"
                + "PV2|||0112^TESTING|55555^PATIENT IS NORMAL|NONE|||19990225|19990226|1|1|TESTING|555888^NOTREAL^BOB^K^DR^MD||||||||||PROD^003^099|02|ER||NONE|19990225|19990223|19990316|NONE\r"
                + "AL1||SEV|001^POLLEN\r"
                + "GT1||0222PL|NOTREAL^BOB^B||STREET^OTHER STREET^CITY^ST^77787|(444)999-3333|(222)777-5555||||MO|111-33-5555||||NOTREAL GILL N|STREET^OTHER STREET^CITY^ST^99999|(111)222-3333\r"
                + "IN1||022254P|4558PD|BLUE CROSS|STREET^OTHER STREET^CITY^ST^00990||(333)333-6666||221K|LENIX|||19980515|19990515|||PATIENT01 TEST D||||||||||||||||||02LL|022LP554";

        msgStr_a03 = "MSH|^~\\&|ADT_MIPIH|MIPIH|MESA_OP|XYZ_HOSPITAL|20180426152043||ADT^A03^ADT_A03|54929_EH07_350|P|2.5|||||FR|8859/1|FR\r"
                + "EVN|A03|20180426152043|||P075352|20180415210000\r"
                + "PID|1||5001000671^^^APHM IPP^PI||SILICE^BERENICE^^^Mme^^L~^BERENICE^^^Mme^^D||19770318|F|||10 bd des arbres^^AUCH^^32000^FRA^H~1 rue de la plage^^PERPIGNAN^^66000^FRA^SA~^^AUCH^^32000^FRA^BDL||^PRN^PH~~^ORN^CP~^NET^Internet^|~^WPN^PH||S||70101010|||^^^APHM IPP||AUCH|||^FRA||^FRA||N||PROV\r"
                + "PD1||U||||||||||N\r"
                + "PV1|1|I|1258|R||||||01||||3||P|||70101010^^^^PI|||N||||||||||||||3|||||N|||20180412151800|20180415210000||||||A\r"
                + "PV2||||||||||||||||||||||N||||||||||||||||1\r"
                + " ZBE|35847|20180415210000||INSERT|N||^^^^^^UF^^^1258||MH\r"
                + "ZFV||1";

        HapiContext context = new DefaultHapiContext ( );
        this.pparser = context.getPipeParser ( );
        try {
            this.aMsg = (AbstractMessage) this.pparser.parse (this.msgStr_a03);
        } catch (HL7Exception e) {
            e.printStackTrace ( );
        }
    }

    @DisplayName("Simple Test Terser : MSH-9-2")
    @Test
    void testTerserMSH10() {
        System.out.println ("testTerserMSH10");

        try {
            Terser t = new Terser (this.aMsg);
            System.out.println (t.get ("MSH-9-1"));
            System.out.println (t.get ("MSH-9-2"));
            System.out.println (t.get ("MSH-9-3"));
            assertEquals (t.get ("MSH-9-2"), "A03");

            System.out.println (t.get ("MSH-7"));
            assertEquals (t.get ("MSH-7"), "20180426152043");

        } catch (HL7Exception e) {
            e.printStackTrace ( );
        }

    }

}
