package controler;

import model.*;
import utils.ParametrizationHelper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * Created by Albert on 16.04.2016.
 */
public class BeziereCalculator implements ICalculator {

    private static final ICalculator anInstance = new BeziereCalculator();

    private BeziereCalculator() {

    }

    public static ICalculator getAnInstance() {
        return anInstance;
    }

    @Override
    public void calculate(ICurve curve, AtomicBoolean dirtyIndicator) {
        if (curve instanceof BeziereCurve || curve instanceof RationalBeziereCurve) {
            Interpolated beziere = (Interpolated) curve;
            IPoint[] newPoints = getNewPoints(beziere.getPoints(), beziere instanceof RationalBeziereCurve);
            beziere.setInterpolatedPoints(Arrays.asList(newPoints));
            dirtyIndicator.compareAndSet(false, true);
        }


    }

    private IPoint[] getNewPoints(List<IPoint> ps, boolean rational) {
        int size = (ps.size()) * 100;
        IPoint newPoints[] = new IPoint[size + 1];
        IntStream.rangeClosed(0, size).sequential().forEach(i -> {
            Double translated = ParametrizationHelper.getInstance().translate(i, size);
            newPoints[i] = countValue(translated, ps, rational);
        });
        return newPoints;
    }

    private IPoint countValue(Double t, List<IPoint> values, boolean rational) {

        Double px = getX(values, 0, rational);
        Double py = getY(values, 0, rational);
        Double pd = (double) values.get(0).getWeigh();

        double e = t;
        double n = values.size();
        double b = n;
        double s = 1.0 - t;

        for (int i = 1; i <= n; ++i) {
            px = s * px + b * e * getX(values, i - 1, rational);
            py = s * py + b * e * getY(values, i - 1, rational);
            pd = s * pd + b * e * ((double) values.get(i - 1).getWeigh());
            e *= t;
            b = b * ((n - i) / (i + 1));
        }

        IPoint p = new Point();
        if (rational) {
            p.setX((int) Math.round(px / pd));
            p.setY((int) Math.round(py / pd));
        } else {
            p.setX((int) Math.round(px));
            p.setY((int) Math.round(py));
        }

        return p;
    }

    private double getX(List<IPoint> values, int i, boolean rational) {
        return rational ? (double) values.get(i).getX() * (double) values.get(i).getWeigh() : (double) values.get(i).getX();
    }

    private double getY(List<IPoint> values, int i, boolean rational) {
        return rational ? (double) values.get(i).getY() * (double) values.get(i).getWeigh() : (double) values.get(i).getY();
    }

}
