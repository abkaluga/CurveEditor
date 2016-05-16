package model.beziere;

import model.ICurve;

/**
 * Created by Albert on 16.05.2016.
 */
public class RationalBezieredeCastelCurve extends BeziereCurve {
    @Override
    public CurveType getType() {
        return CurveType.RationalBezieredeCastel;
    }
}
