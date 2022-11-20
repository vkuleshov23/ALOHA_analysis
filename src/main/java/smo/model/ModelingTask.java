package smo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import smo.util.Consts;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class ModelingTask extends Thread {

    private final List<User> users;

    @Override
    public void run() {
        int i = 0;
        while (true) {
            if (i < Consts.selection) {
                users.forEach(User::createNewMessage); i++;
            } else if(!haveAnyMessages()) break;


            users.forEach(User::sumMessages);
            if (haveAnyMessages()) serveUsers();
            users.forEach(User::windowEnd);
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
        for(User user : users) {
            if(user.haveMessage()) {
                return true;
            }
        }
        return false;
    }

    public double D() {
        double res = this.users.stream().mapToDouble(User::calculateD).sum() / users.size();
        return Double.isNaN(res) || Double.isInfinite(res) ? 0.0 : res;
    }

    public double N() {
        double res = this.users.stream().mapToDouble(User::calculateN).sum() / users.size();
        return Double.isNaN(res) || Double.isInfinite(res) ? 0.0 : res;
    }

    public double lambda() {
        double res = this.users.stream().mapToDouble(User::calculateLambda).sum();
        return Double.isNaN(res) || Double.isInfinite(res) ? 0.0 : res;
    }
}
