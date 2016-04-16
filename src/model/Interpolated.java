package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Albert on 02.04.2016.
 */
@XmlRootElement(name = "Interpolated")
abstract public class Interpolated extends Curve implements ICurve {


    private List<IPoint> interpolatedPoints =   new ArrayList<>();


    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        for (int i = 0; i< getInterpolatedPoints().size()-1; ++i){
            g.translate(0,0);
            g.drawLine(getInterpolatedPoints().get(i).getX(),//
                    getInterpolatedPoints().get(i).getY(),
                    getInterpolatedPoints().get(i+1).getX(),
                    getInterpolatedPoints().get(i+1).getY());
        }
    }

    public List<IPoint> getInterpolatedPoints() {
        return interpolatedPoints;
    }

    public void setInterpolatedPoints(List<IPoint> interpolatedPoints) {
        this.interpolatedPoints = interpolatedPoints;
    }
}
