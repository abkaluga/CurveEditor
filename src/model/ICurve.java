package model;

import java.awt.*;
import java.util.List;

/**
 * Created by Albert on 12.03.2016.
 */
public interface ICurve {

    CurveType getType();

    String getName();

    void setName(String name);

    Color getColor();

    void setColor(Color color);

    List<IPoint> getPoints();

    void setPoints(List<IPoint> points);

    List<IPoint> getConvexHull();

    void setConvexHull(List<IPoint> convexHull);

    void draw(Graphics g);

    void drawSelected(Graphics g);

    void drawConvexHull(Graphics g);

    enum CurveType {
        Chain, NewtonInterpolated, CubicSpineInterpolated, BeziereHorner, RationalBeziereHorner, BezieredeCastel, RationalBezieredeCastel
    }
}
