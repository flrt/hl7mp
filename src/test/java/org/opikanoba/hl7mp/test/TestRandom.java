package org.opikanoba.hl7mp.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TestRandom {

    boolean inRange(int total, double percent, int number) {
        double inf = total * percent - total * 0.01;
        double sup = total * percent + total * 0.01;
        System.out.println (" - inf : " + inf + " - sup " + sup + " / number = " + number);
        return number > inf && number < sup;
    }

    @DisplayName("Test Repartition : 0,5 - 0,25 - 0,25")
    @Test
    void testRepartition() {
        int total = 10000;
        double[] randomResult = new double[total];
        int p1 = 0, p2 = 0, p3 = 0;

        for (int i = 0; i < total; i++) {
            randomResult[i] = Math.random ( );
        }
        System.out.println ("randomResult = " + randomResult[0]);
        System.out.println ("randomResult = " + randomResult[2]);
        System.out.println ("randomResult = " + randomResult[4]);

        for (double v : randomResult) {
            if (v < 0.5) p1 += 1;

            if (v > 0.5 && v < 0.75) p2 += 1;

            if (v > 0.75) p3 += 1;
        }

        System.out.println ("p1 = " + p1);
        System.out.println ("p2 = " + p2);
        System.out.println ("p3 = " + p3);


        assertTrue (inRange (total, 0.5, p1));
        assertTrue (inRange (total, 0.25, p2));
        assertTrue (inRange (total, 0.25, p3));
    }
}
