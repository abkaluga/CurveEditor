package model;

import java.awt.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Albert on 16.04.2016.
 */
public class BeziereCurve extends Interpolated {
    public static final AtomicLong count = new AtomicLong(1);


    @Override
    public CurveType getType() {
        return CurveType.Beziere;
    }

    @Override
    public void drawExtras(Graphics g) {
        Color tempColor = new Color(getColor().getRed(), getColor().getGreen(), getColor().getBlue(), 100);
        g.setColor(tempColor);
        for (int i = 0; i < getPoints().size() - 1; ++i) {
            g.translate(0, 0);
            g.drawLine(getPoints().get(i).getX(),//
                    getPoints().get(i).getY(),
                    getPoints().get(i + 1).getX(),
                    getPoints().get(i + 1).getY());
        }
        super.drawExtras(g);
    }
}
