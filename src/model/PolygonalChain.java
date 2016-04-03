package model;

import java.awt.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Albert on 20.03.2016.
 */
public class PolygonalChain extends Curve {

    public static final AtomicLong count = new AtomicLong(1);

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        for (int i=0;i<getPoints().size()-1;++i){
            g.translate(0,0);
            g.drawLine(getPoints().get(i).getX(),//
                    getPoints().get(i).getY(),
                    getPoints().get(i+1).getX(),
                    getPoints().get(i+1).getY());
        }
    }
}
