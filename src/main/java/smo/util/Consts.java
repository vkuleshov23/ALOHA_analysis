package smo.util;

public class Consts {

    public static final int m = 2;

    public static final double p = 1/(double)m;

    public static final double accuracy = 0.005;

    public static final int selection = (int) (9 / Math.pow(4 * accuracy, 2));

    public static final double lambdaStep = 1e-1;

    public static final double lambdaMin = 0;

    public static final double lambdaMax = 0.9;

}
