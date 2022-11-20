package smo.util;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class Poisson {

    private double lambda;

    public Poisson(double lambda) {
        this.lambda = lambda;
    }

    public int next() {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int result = 0;
        do {
            result++;
            p *= Math.random();
        } while (p > L);
        return result - 1;
    }
}
