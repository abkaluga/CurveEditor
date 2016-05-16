package controler;

import model.*;
import utils.ParametrizationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Albert on 16.05.2016.
 */
public class BeziereInterpolationCalculator implements ICalculator {

    private static final ICalculator anInstance = new BeziereInterpolationCalculator();
    private static final BeziereHornerCalculator helper = (BeziereHornerCalculator) BeziereHornerCalculator.getAnInstance();
    private static final int SEGMENTS_PER_CURVE = 100;

    private BeziereInterpolationCalculator() {

    }

    static ICalculator getAnInstance() {
        return anInstance;
    }

    @Override
    public void calculate(ICurve curve, AtomicBoolean dirtyIndicator) {
        if (curve instanceof BeziereInterpolated) {
            List<IPoint> points = curve.getPoints();
            List<IPoint> newPoints = new ArrayList<>();


            for (int i = 0; i < points.size() - 3; i += 3) {
                IPoint p0 = points.get(i);
                IPoint p1 = points.get(i + 1);
                IPoint p2 = points.get(i + 2);
                IPoint p3 = points.get(i + 3);
                for (int j = 0; j <= SEGMENTS_PER_CURVE; j++) {
                    double t = ParametrizationHelper.getInstance().translate(j, SEGMENTS_PER_CURVE);
                    newPoints.add(calculateBezierPoint(t, p0, p1, p2, p3));
                }

            }
            Interpolated interpolated = (Interpolated) curve;
            interpolated.setInterpolatedPoints(newPoints);
            dirtyIndicator.compareAndSet(false, true);
        }


    }

    public IPoint calculateBezierPoint(double t, IPoint p0, IPoint p1, IPoint p2, IPoint p3) {
        double u = 1.0 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;

        double x = uuu * p0.getX(); //first term
        x += 3 * uu * t * p1.getX(); //second term
        x += 3 * u * tt * p2.getX(); //third term
        x += ttt * p3.getX(); //fourth term
        double y = uuu * p0.getY(); //first term
        y += 3 * uu * t * p1.getY(); //second term
        y += 3 * u * tt * p2.getY(); //third term
        y += ttt * p3.getY(); //fourth term

        IPoint p = new Point();
        p.setX((int) x);
        p.setY((int) y);

        return p;
    }

}
