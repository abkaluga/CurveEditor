package model;


import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Albert on 16.05.2016.
 */
public class BeziereInterpolated extends Curve {
    public static final AtomicLong count = new AtomicLong(1);
    private java.util.List<CubicCurve2D> curves = new ArrayList<>();

    public void setCurves(java.util.List<CubicCurve2D> curves) {
        this.curves = curves;
    }

    @Override
    public ICurve.CurveType getType() {
        return ICurve.CurveType.BeziereInterpolated;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g.setColor(getColor());
        curves.forEach(g2D::draw);
    }
}
