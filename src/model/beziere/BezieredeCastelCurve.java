package model.beziere;

import model.ICurve;

/**
 * Created by Albert on 16.05.2016.
 */
public class BezieredeCastelCurve extends BeziereCurve {
    @Override
    public CurveType getType() {
        return CurveType.BezieredeCastel;
    }
}
