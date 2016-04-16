package controler;

import model.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * Created by Albert on 02.04.2016.
 */
class CurveUpdater {

    static ExecutorService executor = Executors.newCachedThreadPool();

    private CurveUpdater() {

    }

    static void update(ICurve curve, AtomicBoolean dirtyIndicator) {
        if (curve.getPoints().size() > 1) {
            if (curve instanceof PolygonalChain) {
                update((PolygonalChain) curve, dirtyIndicator);
            }
            if (curve instanceof Interpolated) {
                Interpolated interpolated = (Interpolated) curve;
                if (interpolated instanceof NewtonInterpolated) {
                    update((NewtonInterpolated) interpolated, dirtyIndicator);
                }
                if (interpolated instanceof CubicSpineInterpolated) {
                    update((CubicSpineInterpolated) interpolated, dirtyIndicator);
                }
                if (interpolated instanceof BeziereCurve) {
                    update((BeziereCurve) interpolated, dirtyIndicator);
                }
                if (interpolated instanceof RationalBeziereCurve) {
                    update((RationalBeziereCurve) interpolated, dirtyIndicator);
                }

            }
        }
    }


    private static void update(PolygonalChain curve, AtomicBoolean dirtyIndicator) {
        executor.submit(() -> ConvexHullCalculator.getAnInstance().calculate(curve, dirtyIndicator));
    }


    private static void update(NewtonInterpolated curve, AtomicBoolean dirtyIndicator) {
        executor.submit(() -> ConvexHullCalculator.getAnInstance().calculate(curve, dirtyIndicator));
        executor.submit(() -> NewtonInterpolationCalculator.getAnInstance().calculate(curve, dirtyIndicator));
    }

    private static void update(BeziereCurve curve, AtomicBoolean dirtyIndicator) {
        executor.submit(() -> ConvexHullCalculator.getAnInstance().calculate(curve, dirtyIndicator));
        executor.submit(() -> BeziereCalculator.getAnInstance().calculate(curve, dirtyIndicator));
    }

    private static void update(RationalBeziereCurve curve, AtomicBoolean dirtyIndicator) {
        executor.submit(() -> ConvexHullCalculator.getAnInstance().calculate(curve, dirtyIndicator));
        executor.submit(() -> BeziereCalculator.getAnInstance().calculate(curve, dirtyIndicator));
    }

    private static void update(CubicSpineInterpolated curve, AtomicBoolean dirtyIndicator) {
        executor.submit(() -> ConvexHullCalculator.getAnInstance().calculate(curve, dirtyIndicator));
        executor.submit(() -> CubicSpineInterpolationCalculator.getAnInstance().calculate(curve, dirtyIndicator));
    }
}
