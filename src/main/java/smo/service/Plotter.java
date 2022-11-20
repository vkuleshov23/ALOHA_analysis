package smo.service;
import com.github.sh0nk.matplotlib4j.Plot;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Plotter {

    @SneakyThrows
    public void plot(List<Double> x, List<Double> data, String title, String ylabel) {

        Plot plt = Plot.create();
        plt.plot().add(x, data, "-").label("Practice");
        plt.title(title);
        plt.xlabel("$lambda$");
        plt.ylabel(ylabel);
        plt.legend();
        plt.show();
    }

    @SneakyThrows
    public void plotLambda(List<Double> x, List<Double> data, String title, String ylabel) {

        Plot plt = Plot.create();
        plt.plot().add(x, data, "-").label("Practice");
        plt.title(title);
        plt.xlabel("$lambda$");
        plt.ylabel(ylabel); plt.ylim(0.0, 1.0);
        plt.legend();
        plt.show();
    }
}