package controler;

import model.CubicSpineInterpolated;
import model.ICurve;
import model.IPoint;
import model.Point;
import utils.ParametrizationHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * Created by Albert on 16.04.2016.
 */
class CubicSpineInterpolationCalculator implements ICalculator {

    private static final ICalculator anInstance = new CubicSpineInterpolationCalculator();
    private static ExecutorService executor = CurveUpdater.executor;
    Future<List<Double>> xsF;
    Future<List<Double>> ysF;

    private CubicSpineInterpolationCalculator() {

    }

    static ICalculator getAnInstance() {
        return anInstance;
    }

    @Override
    public void calculate(ICurve curve, AtomicBoolean dirtyIndicator) {

        if (curve instanceof CubicSpineInterpolated && curve.getPoints().size() > 2) {
            CubicSpineInterpolated interpolated = (CubicSpineInterpolated) curve;
            List<Double> xs = new ArrayList<>();
            List<Double> ys = new ArrayList<>();
            List<Double> px = ParametrizationHelper.getInstance().createParametrization(curve.getPoints().size());
            interpolated.getPoints().stream()
                    .forEach(p -> {
                        xs.add((double) p.getX());
                        ys.add((double) p.getY());
                    });
            if (xsF != null && !xsF.isDone()) {
                xsF.cancel(false);
                ysF.cancel(false);
            }

            xsF = executor.submit(() -> interpolate(Collections.unmodifiableList(px), Collections.unmodifiableList(xs)));
            ysF = executor.submit(() -> interpolate(Collections.unmodifiableList(px), Collections.unmodifiableList(ys)));
            int size = (px.size()) * 100;
            IPoint[] newPoints = getNewPoints(size);

            interpolated.setInterpolatedPoints(Arrays.asList(newPoints));
            dirtyIndicator.compareAndSet(false, true);
        }

    }

    private IPoint[] getNewPoints(int size) {
        List<Double> xs = Collections.emptyList();
        List<Double> ys = Collections.emptyList();
        if (xsF.isCancelled() || ysF.isCancelled()) {
            return new IPoint[0];
        } else {
            try {
                xs = xsF.get();
                ys = ysF.get();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        IPoint newPoints[] = new IPoint[size + 1];
        List<Double> finalXs = xs;
        List<Double> finalYs = ys;
        IntStream.range(0, xs.size()).parallel().forEach(i -> {
            newPoints[i] = new Point();
            newPoints[i].setX(finalXs.get(i).intValue());
            newPoints[i].setY(finalYs.get(i).intValue());

        });
        return newPoints;
    }


    private List<Double> interpolate(List<Double> val, List<Double> ts) {
        int n = val.size();
        double F[] = new double[n];
        double h[] = new double[n];

        init(val, ts, F, h);

        double s[] = gaussElimination(n, F, h);

        int size = n * 100;
        Double vall[] = new Double[size + 1];

        IntStream.rangeClosed(0, size).parallel().forEach(i -> {
            double t = ParametrizationHelper.getInstance().translate(i, size);
            vall[i] = countValue(t, val, ts, h, s);
        });

        return Arrays.asList(vall);

    }

    private double countValue(double t, List<Double> ts, List<Double> val, double[] h, double[] s) {
        int n = val.size();
        for (int i = 0; i < n - 1; ++i) {
            if (ts.get(i) <= t && t <= ts.get(i + 1)) {
                double a = (s[i + 1] - s[i]) / (6 * h[i]);
                double b = s[i] / 2;
                double c = (val.get(i + 1) - val.get(i)) / h[i] - (2 * h[i] * s[i] + s[i + 1] * h[i]) / 6;
                double d = val.get(i);
                return a * Math.pow((t - ts.get(i)), 3) + b * Math.pow((t - ts.get(i)), 2) + c * (t - ts.get(i)) + d;
            }
        }
        return Double.NaN;
    }

    private double[] gaussElimination(int n, double[] f, double[] h) {
        double m[][] = new double[n][n];

        for (int i = 1; i < n - 1; ++i) {
            m[i][i] = 2 * (h[i - 1] + h[i]);
            if (i != 1) {
                m[i - 1][i] = m[i][i - 1] = h[i - 1];
            }
            m[i][n - 1] = 6 * (f[i + 1] - f[i]);
        }

        for (int i = 1; i < n - 2; ++i) {

            double temp = (m[i + 1][i] / m[i][i]);
            for (int j = 1; j <= n - 1; ++j)
                m[i + 1][j] -= temp * m[i][j];
        }

        double sum;
        double s[] = new double[n];
        for (int i = n - 2; i > 0; --i) {
            sum = 0;
            for (int j = i; j <= n - 2; j++)
                sum += m[i][j] * s[j];
            s[i] = (m[i][n - 1] - sum) / m[i][i];
        }

        return s;
    }

    private void init(List<Double> t, List<Double> val, double[] f, double[] h) {
        int n = val.size();
        for (int i = n - 1; i > 0; --i) {
            f[i] = (val.get(i) - val.get(i - 1)) / (t.get(i) - t.get(i - 1));
            h[i - 1] = t.get(i) - t.get(i - 1);
        }
    }


}
