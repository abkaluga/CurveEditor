package model;

import java.awt.*;

/**
 * Created by Albert on 20.03.2016.
 */
public class Point implements IPoint, Comparable<IPoint> {

    private int x;
    private int y;
    private float weigh = 1;
    private Color color = Color.black;

    public Point() {
        this(0, 0, 1, Color.BLACK);
    }

    private Point(int x, int y, float weigh, Color color) {
        this.x = x;
        this.y = y;
        this.weigh = weigh;
        this.color = color;
    }

    public Point(IPoint point) {
        this(point.getX(), point.getY(), point.getWeigh(), point.getColor());
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    public float getWeigh() {
        return weigh;
    }

    public void setWeigh(float weigh) {
        this.weigh = weigh;
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
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawOval(getX() - 4, getY() - 4, 8, 8);
    }

    @Override
    public int compareTo(IPoint o) {
        if (getX() != o.getX()) {
            return Integer.compare(getY(), o.getY());
        } else {
            return Integer.compare(getX(), o.getX());
        }
    }

    @Override
    public String toString() {
        return "[ " + getX() + " , " + getY() + "]";
    }
}

