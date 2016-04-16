package controler;

import model.ICurve;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Albert on 16.04.2016.
 */
public class CubicSpineInterpolationCalculator implements ICalculator {

    private static ExecutorService executor = Executors.newFixedThreadPool(2);

    private static final ICalculator anInstance = new CubicSpineInterpolationCalculator();

    private CubicSpineInterpolationCalculator() {

    }

    public static ICalculator getAnInstance() {
        return anInstance;
    }

    @Override
    public void calculate(ICurve curve, AtomicBoolean dirtyIndicator) {

        dirtyIndicator.compareAndSet(false, true);
    }
}
