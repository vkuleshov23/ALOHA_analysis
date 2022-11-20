package smo.model;

import lombok.Getter;
import smo.util.Consts;
import smo.util.Poisson;

import java.util.LinkedList;

public class User {

    @Getter
    protected final Poisson p;
    protected final LinkedList<Double> messages = new LinkedList<>();

    protected final LinkedList<Integer> n = new LinkedList<>();

    protected final LinkedList<Double> d = new LinkedList<>();

    public User(double lambda) {
        this.p = new Poisson(lambda);
    }

    public boolean haveMessage() {
        return !messages.isEmpty();
    }

    public boolean getMessage() {
        return haveMessage() && (Math.random() <= Consts.p);
    }

    public void messageWasDelivered() {
        d.addLast(this.messages.removeFirst());
    }

    public void windowEnd() {
        this.messages.replaceAll(timeWeight -> timeWeight + 1.0);
    }

    public void createNewMessage() {
        int newMessages = this.p.next();
        for(int i = 0; i < newMessages; i++) {
            this.messages.addLast(Math.random());
        }
    }

    public void sumMessages() {
        n.addLast(messages.size());
    }

    public double calculateLambda() {
        double res = (double) d.size() / (double) n.size();
        return Double.isNaN(res) || Double.isInfinite(res) ? 0.0 : res;
    }

    public double calculateD() {
        double res =  d.stream().mapToDouble(i -> i).sum()/d.size();
        return Double.isNaN(res) || Double.isInfinite(res) ? 0.0 : res;
    }

    public double calculateN() {
        double res =  n.stream().mapToDouble(i -> i).sum()/n.size();
        return Double.isNaN(res) || Double.isInfinite(res) ? 0.0 : res;
    }

}
