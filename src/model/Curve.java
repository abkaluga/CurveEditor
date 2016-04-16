package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Albert on 12.03.2016.
 */
@XmlRootElement(name = "Curve")
public abstract class Curve implements ICurve {

    @XmlElement
    private String name;
    @XmlElement
    private Color color;
    @XmlElement
    private List<IPoint> points = new ArrayList<>();
    private List<IPoint> convexHull = new ArrayList<>();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public List<IPoint> getPoints() {
        return points;
    }

    @Override
    public void setPoints(List<IPoint> points) {
        this.points = points;

    }

    @Override
    public List<IPoint> getConvexHull() {
        return convexHull;
    }

    @Override
    public void setConvexHull(List<IPoint> convexHull) {
        this.convexHull = convexHull;
    }


    public String toString() {
        return getName();
    }

    @Override
    public void drawExtras(Graphics g) {
        g.setColor(Color.ORANGE);
        if (getConvexHull().size() > 2) {
            g.drawLine(getConvexHull().get(0).getX(),//
                    getConvexHull().get(0).getY(),
                    getConvexHull().get(getConvexHull().size() - 1).getX(),
                    getConvexHull().get(getConvexHull().size() - 1).getY());
            for (int i = 0; i < getConvexHull().size() - 1; ++i) {
                g.translate(0, 0);
                g.drawLine(getConvexHull().get(i).getX(),//
                        getConvexHull().get(i).getY(),
                        getConvexHull().get(i + 1).getX(),
                        getConvexHull().get(i + 1).getY());
            }
        }

    }
}
