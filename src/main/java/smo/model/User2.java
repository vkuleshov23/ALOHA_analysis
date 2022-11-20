package smo.model;

import smo.util.Consts;

import java.util.LinkedList;

public class User2 extends User {

    private final LinkedList<Boolean> firstTime = new LinkedList<>();

    public User2(double lambda) {
        super(lambda);
    }

    @Override
    public boolean getMessage() {
        if(haveMessage()) {
            if(firstTime.getFirst()) {
                firstTime.set(0, false);
                return true;
            } else {
                return (Math.random() <= Consts.p);
            }
        }
        return false;
    }

    @Override
    public void createNewMessage() {
        int newMessages = this.p.next();
        for(int i = 0; i < newMessages; i++) {
            this.messages.addLast(Math.random());
            this.firstTime.addLast(true);
        }
    }

    @Override
    public void messageWasDelivered() {
        d.addLast(this.messages.removeFirst());
        firstTime.removeFirst();
    }
}
