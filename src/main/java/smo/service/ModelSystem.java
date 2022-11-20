package smo.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import smo.model.ModelingTask;
import smo.model.User;
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
    private final List<Double> lambdas = new ArrayList<>();

    private final Plotter plotter;

    public void create() {
        double lambda = Consts.lambdaMin;
        while (lambda <= Consts.lambdaMax) {
            List<User> usersTmp = new ArrayList<>(Consts.m);
            for (int i = 0; i < Consts.m; i++) {
                usersTmp.add(new User(lambda / Consts.m));
            }
            users.addLast(usersTmp);
            lambdas.add(lambda);
            lambda += Consts.lambdaStep;
        }
    }

    @PostConstruct
    public void model() {
        this.create();
        List<ModelingTask> tasks = getTasks();
        tasks.forEach(ModelingTask::run);
        tasks.forEach(task -> {try {task.join();} catch (Exception ignored) {}});
        plotter.plot(lambdas, tasks.stream().map(ModelingTask::D).collect(Collectors.toList()), "D", "M[D]");
        plotter.plot(lambdas, tasks.stream().map(ModelingTask::N).collect(Collectors.toList()), "N", "M[N]");
        plotter.plotLambda(lambdas, tasks.stream().map(ModelingTask::lambda).collect(Collectors.toList()), "Lambda", "Lambda(out)");
    }

    private List<ModelingTask> getTasks() {
        List<ModelingTask> tasks = new ArrayList<>();
        for (List<User> userList : users) {
            tasks.add(new ModelingTask(userList));
        }
        return tasks;
    }

}
