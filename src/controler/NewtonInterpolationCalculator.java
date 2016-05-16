package controler;

import model.ICurve;
import model.IPoint;
import model.NewtonInterpolated;
import model.Point;
import utils.ParametrizationHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.IntStream;

/**
 * Created by Albert on 02.04.2016.
 */
class NewtonInterpolationCalculator implements ICalculator {

    private static final ICalculator anInstance = new NewtonInterpolationCalculator();
    private static ExecutorService executor = CurveUpdater.executor;
    Future<List<Double>> xDiff;
    Future<List<Double>> yDiff;

    private NewtonInterpolationCalculator() {

    }

    public static ICalculator getAnInstance() {
        return anInstance;
    }

    @Override
    public void calculate(ICurve curve, AtomicBoolean dirtyIndicator) {
        if (curve instanceof NewtonInterpolated) {
            NewtonInterpolated interpolated = (NewtonInterpolated) curve;
            List<Double> xs = new ArrayList<>();
            List<Double> ys = new ArrayList<>();
            List<Double> px = ParametrizationHelper.getInstance().createParametrization(curve.getPoints().size());
            interpolated.getPoints().stream()
                    .forEach(p -> {
                        xs.add((double) p.getX());
                        ys.add((double) p.getY());
                    });

            cancelOldstartNew(xs, ys, px);

            int size = (xs.size() - 1) * 100;
            IPoint[] newPoints = getNewPoints(px, size);
            interpolated.setInterpolatedPoints(new ArrayList<>(Arrays.asList(newPoints)));
            dirtyIndicator.compareAndSet(false, true);
        }
    }

    private void cancelOldstartNew(List<Double> xs, List<Double> ys, List<Double> px) {
        if (xDiff != null && !xDiff.isDone()) {
            xDiff.cancel(false);
        }
        if (yDiff != null && !yDiff.isDone()) {
            yDiff.cancel(false);
        }
        xDiff = executor.submit(() -> newtonInterpolation(Collections.unmodifiableList(px), Collections.unmodifiableList(xs)));
        yDiff = executor.submit(() -> newtonInterpolation(Collections.unmodifiableList(px), Collections.unmodifiableList(ys)));
    }

    private IPoint[] getNewPoints(List<Double> px, int size) {
        if (xDiff.isCancelled() || xDiff.isCancelled()) {
            return new IPoint[0];
        }

        IPoint newPoints[] = new IPoint[size + 1];
        IntStream.rangeClosed(0, size).parallel().forEach(i -> {
            IPoint p = new Point();
            try {
                Double translated = ParametrizationHelper.getInstance().translate(i, size);
                p.setX((countValue(translated, xDiff.get(), px)));
                p.setY((countValue(translated, yDiff.get(), px)));

            } catch (InterruptedException | ExecutionException e) {
                System.out.print(e.getMessage());
            }
            newPoints[i] = p;
        });
        return newPoints;
    }


    private List<Double> newtonInterpolation(List<Double> parameters, List<Double> values) {
        Double d[] = new Double[values.size()];
        values.toArray(d);
        for (int j = 0; j < d.length - 1; j++) {
            for (int i = d.length - 1; i > j; i--) {
                double div = parameters.get(i) - parameters.get(i - j - 1);
                d[i] = (d[i] - d[i - 1]) / div;
            }
        }
        return Arrays.asList(d);
    }

    private int countValue(Double x, List<Double> d, List<Double> parameters) {
        DoubleAdder sum = new DoubleAdder();
        IntStream.range(0, d.size()).mapToDouble(i -> {
            double m = 1;
            for (int j = 0; j < i; ++j)
                // Wasted over 3h because write i instead of j
                m *= (x - parameters.get(j));
            m *= d.get(i);
            return m;
        }).parallel().forEach(sum::add);
        return Math.round(sum.floatValue());
    }
}
