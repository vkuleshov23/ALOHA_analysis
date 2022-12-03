package smo.util;

public class Consts {

    public static final int m = 10;

    public static double p = 1/(double)m;

    public static final double accuracy = 0.02;

    public static final int selection = (int) (9 / Math.pow(4 * accuracy, 2));

    public static final double lambdaStep = 1e-1;

    public static final double lambdaMin = 0;

    public static final double lambdaMax = 0.9;

    public static final double[] ps = {0.01, 0.05, 0.1, 0.2, 0.25};
    public static final int[] M = {2, 5, 10, 20};

}
