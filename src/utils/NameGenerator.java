package utils;

import model.*;

/**
 * Created by Albert on 28.03.2016.
 */
public class NameGenerator {

    private NameGenerator() {
        // Intentionally left empty
    }

    public static void generateName(ICurve c) {
        if (c instanceof PolygonalChain) {
            c.setName(String.format("PolygonalChain #%d", PolygonalChain.count.getAndIncrement()));
            return;
        }
        if (c instanceof NewtonInterpolated) {
            c.setName(String.format("Interpolated #%d", NewtonInterpolated.count.getAndIncrement()));
            return;
        }

        if (c instanceof CubicSpineInterpolated) {
            c.setName(String.format("Cubic spine #%d", CubicSpineInterpolated.count.getAndIncrement()));
            return;
        }
        if (c instanceof BeziereCurve) {
            c.setName(String.format("Beziere  #%d", BeziereCurve.count.getAndIncrement()));
            return;
        }
        if (c instanceof RationalBeziereCurve) {
            c.setName(String.format("Rational Beziere #%d", RationalBeziereCurve.count.getAndIncrement()));
            return;
        }

        c.setName("ERROR 404");
    }
}
