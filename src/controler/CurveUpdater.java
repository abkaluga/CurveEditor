package controler;

import model.ICurve;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Albert on 02.04.2016.
 */
public class CurveUpdater {

    static ExecutorService executor = Executors.newCachedThreadPool();

    private static Map<ICurve.CurveType, ICalculator> calculators = new HashMap<>();

    static {
        calculators.put(ICurve.CurveType.NewtonInterpolated, NewtonInterpolationCalculator.getAnInstance());
        calculators.put(ICurve.CurveType.CubicSpineInterpolated, CubicSpineInterpolationCalculator.getAnInstance());
        calculators.put(ICurve.CurveType.BeziereHorner, BeziereHornerCalculator.getAnInstance());
        calculators.put(ICurve.CurveType.RationalBeziereHorner, BeziereHornerCalculator.getAnInstance());
        calculators.put(ICurve.CurveType.BezieredeCastel, BezieredeCastelCalculator.getAnInstance());
        calculators.put(ICurve.CurveType.RationalBezieredeCastel, BezieredeCastelCalculator.getAnInstance());
        calculators.put(ICurve.CurveType.BeziereInterpolated, BeziereInterpolationCalculator.getAnInstance());
    }

    private CurveUpdater() {
    }

    static public void update(ICurve curve, AtomicBoolean dirtyIndicator) {
        ThreadPoolExecutor exe = (ThreadPoolExecutor) executor;
        System.out.println(exe.getTaskCount());
        if (curve.getPoints().size() > 1) {
            executor.execute(() -> ConvexHullCalculator.getAnInstance().calculate(curve, dirtyIndicator));
            ICalculator specyficCalculator = calculators.get(curve.getType());
            if (specyficCalculator != null) {
                executor.execute(() -> specyficCalculator.calculate(curve, dirtyIndicator));
            }
        }
    }

}
