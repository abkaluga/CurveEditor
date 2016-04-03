package model;

import java.awt.*;
import java.beans.PropertyChangeListener;

/**
 * Created by Albert on 20.03.2016.
 */
public interface IPoint extends Comparable<IPoint> {

    int getX();

    int getY();

    void setX(int x);

    void setY(int y);

    float getWeigh();

    void setWeigh(float weigh);

    Color getColor();

    void setColor(Color color);


    void draw(Graphics g);
}
