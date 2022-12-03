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
public class ModelSystem {

    private final LinkedList<List<User>> users = new LinkedList<>();
    private final LinkedList<List<User2>> users2 = new LinkedList<>();
    private final List<Double> lambdas = new ArrayList<>();

    private final Plotter plotter;

    public void create() {
        double lambda = Consts.lambdaMin;
        while (lambda <= Consts.lambdaMax) {
            List<User> usersTmp = new ArrayList<>(Consts.m);
            List<User2> usersTmp2 = new ArrayList<>(Consts.m);
            for (int i = 0; i < Consts.m; i++) {
                usersTmp.add(new User(lambda / Consts.m));
                usersTmp2.add(new User2(lambda / Consts.m));
            }
            users.addLast(usersTmp);
            users2.addLast(usersTmp2);
            lambdas.add(lambda);
            lambda += Consts.lambdaStep;
        }
    }

//    @PostConstruct
    public void model() {
        this.create();
        List<ModelingTask> tasks = getTasks();
        List<ModelingTask2> tasks2 = getTasks2();
        tasks.forEach(ModelingTask::run);
        tasks2.forEach(ModelingTask2::run);
        tasks.forEach(task -> {try {task.join();} catch (Exception ignored) {}});
        tasks2.forEach(task2 -> {try {task2.join();} catch (Exception ignored) {}});

//        List<Double> graphic = new ArrayList<>();
//        for(int i = 0; i < tasks.size(); i++) {
//            double res = tasks.get(i).D() / tasks2.get(i).D();
//            graphic.add(Double.isNaN(res) ? 0.0 : res);
//        }

        plotter.plot(lambdas,
                tasks.stream().map(ModelingTask::D).collect(Collectors.toList()),
                tasks2.stream().map(ModelingTask2::D).collect(Collectors.toList()),
                "D", "M[D]","A", "B");

        plotter.plot(lambdas,
                tasks.stream().map(ModelingTask::N).collect(Collectors.toList()),
                tasks2.stream().map(ModelingTask2::N).collect(Collectors.toList()),
                "N", "M[N]","A", "B");

        plotter.plotLambda(lambdas,
                tasks.stream().map(ModelingTask::lambda).collect(Collectors.toList()),
                tasks2.stream().map(ModelingTask2::lambda).collect(Collectors.toList()),
                "Lambda", "Lambda(out)","A", "B");

//        plotter.plot(lambdas, graphic, "dalayA/delay2", "");
    }

    private List<ModelingTask> getTasks() {
        List<ModelingTask> tasks = new ArrayList<>();
        for (List<User> userList : users) {
            tasks.add(new ModelingTask(userList));
        }
        return tasks;
    }

    private List<ModelingTask2> getTasks2() {
        List<ModelingTask2> tasks = new ArrayList<>();
        for (List<User2> userList : users2) {
            tasks.add(new ModelingTask2(userList));
        }
        return tasks;
    }

}
