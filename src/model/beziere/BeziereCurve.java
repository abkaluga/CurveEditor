package model.beziere;

import model.Interpolated;

import java.awt.*;

/**
 * Created by Albert on 16.05.2016.
 */
public abstract class BeziereCurve extends Interpolated {

    @Override
    public void drawSelected(Graphics g) {
        super.drawSelected(g);
        Color tempColor = new Color(getColor().getRed(), getColor().getGreen(), getColor().getBlue(), 100);
        g.setColor(tempColor);
        for (int i = 0; i < getPoints().size() - 1; ++i) {
            g.translate(0, 0);
            g.drawLine(getPoints().get(i).getX(),//
                    getPoints().get(i).getY(),
                    getPoints().get(i + 1).getX(),
                    getPoints().get(i + 1).getY());
        }
    }
}
