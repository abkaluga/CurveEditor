package model;

import java.awt.*;

/**
 * Created by Albert on 20.03.2016.
 */
public interface IPoint extends Comparable<IPoint> {

    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    float getWeigh();

    void setWeigh(float weigh);

    Color getColor();

    void setColor(Color color);


    void draw(Graphics g);
}
