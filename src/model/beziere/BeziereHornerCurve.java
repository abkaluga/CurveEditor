package model.beziere;

import model.Interpolated;

import java.awt.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Albert on 16.04.2016.
 */
public class BeziereHornerCurve extends BeziereCurve {
    public static final AtomicLong count = new AtomicLong(1);


    @Override
    public CurveType getType() {
        return CurveType.BeziereHorner;
    }


}
