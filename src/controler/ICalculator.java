package controler;

import model.ICurve;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Albert on 28.03.2016.
 */
interface ICalculator {

    void calculate(ICurve curve, AtomicBoolean dirtyIndicator);
}
