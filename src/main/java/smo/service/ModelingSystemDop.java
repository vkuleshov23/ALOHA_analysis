package smo.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import smo.model.ModelingTask;
import smo.model.ModelingTask2;
import smo.model.User;
import smo.model.User2;
import smo.util.Consts;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Service
@RequiredArgsConstructor
public class ModelingSystemDop {

    private final LinkedList<LinkedList<LinkedList<List<User>>>> mpUsers = new LinkedList<>();
    private final List<Double> lambdas = new ArrayList<>();

    private final Plotter plotter;

    public void create() {
        for(int x = 0; x < Consts.M.length; x++) {
            mpUsers.addLast(new LinkedList<>());
            for(int y = 0; y < Consts.ps.length; y++) {
                mpUsers.get(x).addLast(new LinkedList<>());
                double lambda = Consts.lambdaMin;
                while (lambda <= Consts.lambdaMax) {
                    List<User> usersTmp = new ArrayList<>(Consts.M[x]);
                    for (int i = 0; i < Consts.M[x]; i++) {
                        usersTmp.add(new User(lambda / Consts.M[x]));
                    }
                   mpUsers.get(x).get(y).addLast(usersTmp);
                    lambdas.add(lambda);
                    lambda += Consts.lambdaStep;
                }
            }
        }
        lambdas.clear();
        double lambda = Consts.lambdaMin;
        while (lambda <= Consts.lambdaMax) {
            lambdas.add(lambda);
            lambda += Consts.lambdaStep;
        }
    }

    @PostConstruct
    public void model() {
        this.create();
        for(int x = 0; x < Consts.M.length; x++) {
            LinkedList<List<ModelingTask>> tasks = new LinkedList<>();
            for(int y = 0; y < Consts.ps.length; y++) {
                Consts.p = Consts.ps[y];
                tasks.addLast(getTasks(x, y));
                tasks.getLast().forEach(ModelingTask::run);
                tasks.getLast().forEach(task -> {try {task.join();} catch (Exception ignored) {}});
            }
            plotter.plotDopD(lambdas, tasks, String.valueOf(Consts.M[x]), "M[D]");
            plotter.plotDopN(lambdas, tasks, String.valueOf(Consts.M[x]), "M[N]");
            plotter.plotDopLambda(lambdas, tasks, String.valueOf(Consts.M[x]), "Lambda(out)");
            tasks.clear();
        }
    }

    private List<ModelingTask> getTasks(int x, int y) {
        List<ModelingTask> tasks = new ArrayList<>();
        for (List<User> userList : mpUsers.get(x).get(y)) {
            tasks.add(new ModelingTask(userList));
        }
        return tasks;
    }
}
