package model.dtoModel;

import model.*;
import model.beziere.BeziereHornerCurve;
import model.beziere.BezieredeCastelCurve;
import model.beziere.RationalBeziereHornerCurve;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Albert on 16.04.2016.
 */
@XmlRootElement
public class CurveDTO {


    private String name;

    private int color;

    private ICurve.CurveType type;

    private List<PointDTO> points = new ArrayList<>();

    public static CurveDTO toDTO(ICurve c) {
        CurveDTO dto = new CurveDTO();
        dto.setName(c.getName());
        dto.setColor(c.getColor().getRGB());
        dto.setPoints(c.getPoints().stream().map(PointDTO::toDTO).collect(Collectors.toList()));
        dto.setType(c.getType());
        return dto;
    }

    public static ICurve fromDTO(CurveDTO dto) {
        ICurve curve = null;
        switch (dto.getType()) {
            case NewtonInterpolated:
                curve = new NewtonInterpolated();
                break;
            case Chain:
                curve = new PolygonalChain();
                break;
            case BeziereHorner:
                curve = new BeziereHornerCurve();
                break;
            case CubicSpineInterpolated:
                curve = new CubicSpineInterpolated();
                break;
            case RationalBeziereHorner:
                curve = new RationalBeziereHornerCurve();
                break;
            case BezieredeCastel:
                curve = new BezieredeCastelCurve();
                break;
            case RationalBezieredeCastel:
                curve = new RationalBeziereHornerCurve();
                break;
            case BeziereInterpolated:
                curve = new BeziereInterpolated();
        }
        curve.setColor(new Color(dto.getColor()));
        curve.setName(dto.getName());
        curve.setPoints(dto.getPoints().stream().map(PointDTO::fromDTO).collect(Collectors.toList()));

        return curve;
    }

    public String getName() {
        return name;
    }

    @XmlElement(nillable = true, required = true)
    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    @XmlElement(nillable = true, required = true)
    public void setColor(int color) {
        this.color = color;
    }

    public ICurve.CurveType getType() {
        return type;
    }

    @XmlElement(nillable = true, required = true)
    public void setType(ICurve.CurveType type) {
        this.type = type;
    }

    public List<PointDTO> getPoints() {
        return points;
    }

    @XmlElement(nillable = true, required = true)
    public void setPoints(List<PointDTO> points) {
        this.points = points;
    }
}
