package com.prylutskyi.blackjack.utils;

import java.math.BigDecimal;

/**
 * Created by Patap on 30.11.2014.
 */
public class DoubleUtils {

    public static double multiply(double d1, double d2) {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(d1));
        BigDecimal multiplicand = new BigDecimal(Double.toString(d2));
        BigDecimal multiply = bigDecimal.multiply(multiplicand);
        return multiply.doubleValue();
    }

    public static double sum(double d1, double d2) {
        BigDecimal bigDecimal1 = new BigDecimal(Double.toString(d1));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(d2));
        BigDecimal sum = bigDecimal1.add(bigDecimal2);
        return sum.doubleValue();
    }

}
