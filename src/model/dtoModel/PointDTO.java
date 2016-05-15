package model.dtoModel;

import com.sun.istack.internal.NotNull;
import model.IPoint;
import model.Point;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;

/**
 * Created by Albert on 16.04.2016.
 */
@XmlRootElement
public class PointDTO {

    private int x;
    private int y;
    private float weigh = 1;


    public static Point fromDTO(PointDTO dto) {
        Point p = new Point();
        p.setX(dto.getX());
        p.setY(dto.getY());
        p.setWeigh(dto.getWeigh());
        return p;
    }

    public static PointDTO toDTO(IPoint p) {
        PointDTO dto = new PointDTO();
        dto.setX(p.getX());
        dto.setY(p.getY());
        dto.setWeigh(p.getWeigh());
        return dto;
    }

    public int getX() {
        return x;
    }

    @XmlElement(nillable = true, required = true)
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    @XmlElement(nillable = true, required = true)
    public void setY(int y) {
        this.y = y;
    }

    public float getWeigh() {
        return weigh;
    }

    @XmlElement(nillable = true, required = true)
    public void setWeigh(float weigh) {
        this.weigh = weigh;
    }
}
