package com.chrtc.textRecommend.recommend;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计算距离
 */
public class NewsKclusterSimitly {
    public static float getDoubleStrForCosValue(double[] ints1, double[] ints2) {
        BigDecimal fzSum = new BigDecimal(0);
        BigDecimal fmSum = new BigDecimal(0);
        int num;
        if (ints1.length >= ints2.length) {
            num = ints1.length;
        } else {
            num = ints2.length;
        }

        for (int i = 0; i < num; i++) {
            BigDecimal adb = new BigDecimal(ints1[i]).multiply(new BigDecimal(ints2[i]));
            fzSum = fzSum.add(adb);
        }
        BigDecimal seq1SumBigDecimal = new BigDecimal(0);
        BigDecimal seq2SumBigDecimal = new BigDecimal(0);
        for (int i = 0; i < num; i++) {
            seq1SumBigDecimal = seq1SumBigDecimal.add(new BigDecimal(Math.pow(ints1[i], 2)));
            seq2SumBigDecimal = seq2SumBigDecimal.add(new BigDecimal(Math.pow(ints2[i], 2)));
        }
        double sqrt1 = Math.sqrt(seq1SumBigDecimal.doubleValue());
        double sqrt2 = Math.sqrt(seq2SumBigDecimal.doubleValue());
        fmSum = new BigDecimal(sqrt1).multiply(new BigDecimal(sqrt2));
        return fzSum.divide(fmSum, 10, RoundingMode.HALF_UP).floatValue();
    }

}
