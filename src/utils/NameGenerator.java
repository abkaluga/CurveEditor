package utils;

import model.*;
import model.beziere.BeziereHornerCurve;
import model.beziere.BezieredeCastelCurve;
import model.beziere.RationalBeziereHornerCurve;
import model.beziere.RationalBezieredeCastelCurve;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Albert on 28.03.2016.
 */
public class NameGenerator {

    private static final Map<ICurve.CurveType, Namer> formats = new EnumMap<>(ICurve.CurveType.class);

    static {
        formats.put(ICurve.CurveType.Chain, new Namer("PolygonalChain #%d", PolygonalChain.count));
        formats.put(ICurve.CurveType.NewtonInterpolated, new Namer("Newton Interpolated #%d", NewtonInterpolated.count));
        formats.put(ICurve.CurveType.CubicSpineInterpolated, new Namer("Cubic spine #%d", CubicSpineInterpolated.count));
        formats.put(ICurve.CurveType.BeziereHorner, new Namer("Beziere(Horner)  #%d", BeziereHornerCurve.count));
        formats.put(ICurve.CurveType.BezieredeCastel, new Namer("Beziere(deCastel)  #%d", BezieredeCastelCurve.count));
        formats.put(ICurve.CurveType.RationalBeziereHorner, new Namer("Rational Beziere (Horner)  #%d", RationalBeziereHornerCurve.count));
        formats.put(ICurve.CurveType.RationalBezieredeCastel, new Namer("Rational Beziere (deCastel) #%d", RationalBezieredeCastelCurve.count));
        formats.put(ICurve.CurveType.BeziereInterpolated, new Namer("Beziere Interpolated  #%d", BeziereInterpolated.count));
    }

    private NameGenerator() {
        // Intentionally left empty
    }

    public static void generateName(ICurve c) {
        Namer namer = formats.get(c.getType());
        if (namer != null) {
            c.setName(namer.getNextName());
        } else {
            c.setName("Class not found");
        }
    }
}
