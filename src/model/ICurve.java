package model;

import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Created by Albert on 12.03.2016.
 */
public interface ICurve {

    enum CurveType {
        Chain, Interpolated
    }
    String getName();

    void setName(String name);

    Color getColor();

    void setColor(Color color);

    List<IPoint> getPoints();

    void setPoints(List<IPoint> points);

    List<IPoint> getConvexHull();

    void setConvexHull(List<IPoint> convexHull);

    void draw(Graphics g);

    void drawConvexHull(Graphics g);
}
