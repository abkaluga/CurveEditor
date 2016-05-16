package model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Albert on 16.05.2016.
 */
public class BeziereInterpolated extends Interpolated {
    public static final AtomicLong count = new AtomicLong(1);

    @Override
    public CurveType getType() {
        return ICurve.CurveType.BeziereInterpolated;
    }
}
