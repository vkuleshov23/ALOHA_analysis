package smo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import smo.util.Consts;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class ModelingTask2 extends Thread {

    private final List<User2> users;

    @Override
    public void run() {
        int i = 0;
        while (true) {
            if (i < Consts.selection) {
                users.forEach(User2::createNewMessage); i++;
            } else if(!haveAnyMessages()) break;


            users.forEach(User2::sumMessages);
            if (haveAnyMessages()) serveUsers();
            users.forEach(User2::windowEnd);
        }
    }

    private void serveUsers() {
        int index = -1;
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).getMessage()) {
                if(index < 0) {
                    index = i;
                } else {
                    return;
                }
            }
        }
        if(index >= 0) {
            users.get(index).messageWasDelivered();
        }
    }

    private boolean haveAnyMessages() {
        for(User2 user2 : users) {
            if(user2.haveMessage()) {
                return true;
            }
        }
        return false;
    }

    public double D() {
        double res = this.users.stream().mapToDouble(User2::calculateD).sum() / users.size();
        return Double.isNaN(res) || Double.isInfinite(res) ? 0.0 : res;
    }

    public double N() {
        double res = this.users.stream().mapToDouble(User2::calculateN).sum() / users.size();
        return Double.isNaN(res) || Double.isInfinite(res) ? 0.0 : res;
    }

    public double lambda() {
        double res = this.users.stream().mapToDouble(User2::calculateLambda).sum();
        return Double.isNaN(res) || Double.isInfinite(res) ? 0.0 : res;
    }
}
