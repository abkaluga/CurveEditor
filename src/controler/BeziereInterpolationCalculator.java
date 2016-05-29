package controler;

import model.BeziereInterpolated;
import model.ICurve;
import model.IPoint;
import model.Point;

import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.sqrt;

/**
 * Created by Albert on 16.05.2016.
 */
public class BeziereInterpolationCalculator implements ICalculator {

    private static final ICalculator anInstance = new BeziereInterpolationCalculator();
    private static final double smooth_value = 0.5;

    private BeziereInterpolationCalculator() {

    }

    static ICalculator getAnInstance() {
        return anInstance;
    }

    @Override
    public void calculate(ICurve curve, AtomicBoolean dirtyIndicator) {
        if (curve instanceof BeziereInterpolated) {
            BeziereInterpolated interpolated = (BeziereInterpolated) curve;
            List<CubicCurve2D> curves = new ArrayList<>();
            List<IPoint> points = curve.getPoints();
            IPoint p0, p1, p2, p3, ctrl1, ctrl2;

            for (int i = 0; i < points.size() - 1; ++i) {
                p0 = points.get(i - 1 < 0 ? i : i - 1);
                p1 = points.get(i);
                p2 = points.get(i + 1);
                p3 = points.get(i + 2 >= points.size() ? i + 1 : i + 2);
                ctrl1 = new Point();
                ctrl2 = new Point();
                calculateControlPoints(p0, p1, p2, p3, ctrl1, ctrl2);
                curves.add(new CubicCurve2D.Double(p1.getX(), p1.getY(), ctrl1.getX(), ctrl1.getY(), ctrl2.getX(), ctrl2.getY(), p2.getX(), p2.getY()));
            }

            interpolated.setCurves(curves);
            dirtyIndicator.compareAndSet(false, true);
        }


    }

    private void calculateControlPoints(IPoint p0, IPoint p1, IPoint p2, IPoint p3, IPoint ctrl1, IPoint ctrl2) {
        // Assume we need to calculate the control
        // points between (x1,y1) and (x2,y2).
        // Then x0,y0 - the previous vertex,
        //      x3,y3 - the next one.

        double xc1 = (p0.getX() + p1.getX()) / 2.0;
        double yc1 = (p0.getY() + p1.getY()) / 2.0;
        double xc2 = (p1.getX() + p2.getX()) / 2.0;
        double yc2 = (p1.getY() + p2.getY()) / 2.0;
        double xc3 = (p2.getX() + p3.getX()) / 2.0;
        double yc3 = (p2.getY() + p3.getY()) / 2.0;

        double len1 = sqrt((p1.getX() - p0.getX()) * (p1.getX() - p0.getX()) + (p1.getY() - p0.getY()) * (p1.getY() - p0.getY()));
        double len2 = sqrt((p2.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (-p1.getY()) * (-p1.getY()));
        double len3 = sqrt((p3.getX() - p2.getX()) * (p3.getX() - p2.getX()) + (p3.getY() - p3.getY()) * (p3.getY() - p2.getY()));

        double k1 = len1 / (len1 + len2);
        double k2 = len2 / (len2 + len3);

        double xm1 = xc1 + (xc2 - xc1) * k1;
        double ym1 = yc1 + (yc2 - yc1) * k1;

        double xm2 = xc2 + (xc3 - xc2) * k2;
        double ym2 = yc2 + (yc3 - yc2) * k2;

        // Resulting control points. Here smooth_value is mentioned
        // above coefficient K whose value should be in range [0...1].

        ctrl1.setX((int) (xm1 + (xc2 - xm1) * smooth_value + p1.getX() - xm1));
        ctrl1.setY((int) (ym1 + (yc2 - ym1) * smooth_value + p1.getY() - ym1));

        ctrl2.setX((int) (xm2 + (xc2 - xm2) * smooth_value + p2.getX() - xm2));
        ctrl2.setY((int) (ym2 + (yc2 - ym2) * smooth_value + p2.getY() - ym2));
    }

}
