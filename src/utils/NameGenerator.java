package utils;

import model.*;
import model.beziere.BeziereHornerCurve;
import model.beziere.BezieredeCastelCurve;
import model.beziere.RationalBeziereHornerCurve;
import model.beziere.RationalBezieredeCastelCurve;

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
        if (c instanceof BeziereHornerCurve) {
            c.setName(String.format("Beziere(Horner)  #%d", BeziereHornerCurve.count.getAndIncrement()));
            return;
        }
        if (c instanceof RationalBeziereHornerCurve) {
            c.setName(String.format("Rational Beziere (Horner) #%d", RationalBeziereHornerCurve.count.getAndIncrement()));
            return;
        }
        if (c instanceof BezieredeCastelCurve) {
            c.setName(String.format("Beziere(Castel)  #%d", BezieredeCastelCurve.count.getAndIncrement()));
            return;
        }
        if (c instanceof RationalBezieredeCastelCurve) {
            c.setName(String.format("Rational Beziere (Castel)  #%d", RationalBezieredeCastelCurve.count.getAndIncrement()));
            return;
        }
        if (c instanceof BeziereInterpolated) {
            c.setName(String.format("Beziere Interpolated  #%d", BeziereInterpolated.count.getAndIncrement()));
            return;
        }

        c.setName("ERROR 404");
    }
}
