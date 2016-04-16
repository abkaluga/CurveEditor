package model.dtoModel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Albert on 16.04.2016.
 */
@XmlRootElement(name = "Project")
public class Project {


    private List<CurveDTO> curves = new ArrayList<>();


    private Long rationalCounter = 0L;

    private Long chainCounter = 0L;

    private Long beziereCounter = 0L;

    private Long interpolatedCounter = 0L;

    private String encodedBackGround = "";


    public Long getRationalCounter() {
        return rationalCounter;
    }

    @XmlElement
    public void setRationalCounter(Long rationalCounter) {
        this.rationalCounter = rationalCounter;
    }

    public Long getChainCounter() {
        return chainCounter;
    }

    @XmlElement
    public void setChainCounter(Long chainCounter) {
        this.chainCounter = chainCounter;
    }

    public Long getBeziereCounter() {
        return beziereCounter;
    }

    @XmlElement
    public void setBeziereCounter(Long beziereCounter) {
        this.beziereCounter = beziereCounter;
    }

    public Long getInterpolatedCounter() {
        return interpolatedCounter;
    }

    @XmlElement
    public void setInterpolatedCounter(Long interpolatedCounter) {
        this.interpolatedCounter = interpolatedCounter;
    }

    public String getEncodedBackGround() {
        return encodedBackGround;
    }

    @XmlElement
    public void setEncodedBackGround(String encodedBackGround) {
        this.encodedBackGround = encodedBackGround;
    }

    public List<CurveDTO> getCurves() {
        return curves;
    }

    @XmlElement
    public void setCurves(List<CurveDTO> curves) {
        this.curves = curves;
    }
}
