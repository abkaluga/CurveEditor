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
        g.drawOval(getX() - 2, getY() - 2, 4, 4);
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

