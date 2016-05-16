package controler;

import model.ICurve;
import model.IPoint;
import model.Interpolated;
import model.Point;
import model.beziere.BeziereCurve;
import model.beziere.BezieredeCastelCurve;
import model.beziere.RationalBezieredeCastelCurve;
import utils.ParametrizationHelper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * Created by Albert on 16.05.2016.
 */
public class BezieredeCastelCalculator implements ICalculator {

    private static final ICalculator anInstance = new BezieredeCastelCalculator();

    private BezieredeCastelCalculator() {

    }

    static ICalculator getAnInstance() {
        return anInstance;
    }

    @Override
    public void calculate(ICurve curve, AtomicBoolean dirtyIndicator) {
        if (curve instanceof BezieredeCastelCurve || curve instanceof RationalBezieredeCastelCurve) {
            Interpolated beziere = (BeziereCurve) curve;
            IPoint[] newPoints = getNewPoints(beziere.getPoints(), beziere instanceof RationalBezieredeCastelCurve);
            beziere.setInterpolatedPoints(Arrays.asList(newPoints));
            dirtyIndicator.compareAndSet(false, true);
        }
    }

    private IPoint[] getNewPoints(List<IPoint> ps, boolean rational) {
        int size = (ps.size()) * 100;
        IPoint newPoints[] = new IPoint[size + 1];
        IntStream.rangeClosed(0, size).parallel().forEach(i -> {
            Double translated = ParametrizationHelper.getInstance().translate(i, size);
            newPoints[i] = countValue(translated, ps, rational);
        });
        return newPoints;
    }

    private IPoint countValue(Double translated, List<IPoint> ps, boolean rational) {
        Double xs[] = new Double[ps.size()];
        Double w[] = rational ? new Double[ps.size()] : null;
        Double ys[] = new Double[ps.size()];

        for (int i = 0; i < ps.size(); ++i) {
            xs[i] = (double) ps.get(i).getX();
            ys[i] = (double) ps.get(i).getY();
            if (rational) {
                w[i] = (double) ps.get(i).getWeigh();
            }
        }

        Double xs1[] = new Double[ps.size()];
        Double w1[] = rational ? new Double[ps.size()] : null;
        Double ys1[] = new Double[ps.size()];

        for (int j = 1; j < ps.size(); ++j) {

            for (int i = 0; i < ps.size() - j; ++i) {
                double s = translated;
                if (rational) {
                    w1[i] = (1.0 - translated) * w[i] + translated * w[i + 1];
                    s = (s * w[i + 1]) / w1[i];
                }
                xs1[i] = (1 - s) * xs[i] + s * xs[i + 1];
                ys1[i] = (1 - s) * ys[i] + s * ys[i + 1];
            }
            xs = xs1;
            xs1 = new Double[ps.size()];
            ys = ys1;
            ys1 = new Double[ps.size()];
            w = w1;
            w1 = new Double[ps.size()];
        }

        IPoint p = new Point();
        p.setX(xs[0].intValue());
        p.setY(ys[0].intValue());
        return p;
    }

}
