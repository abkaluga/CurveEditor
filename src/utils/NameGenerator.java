package utils;

import model.*;

/**
 * Created by Albert on 28.03.2016.
 */
public  class NameGenerator {

    private NameGenerator(){
        // Intentionally left empty
    }

    public static void generateName(ICurve c){
        if (c instanceof  PolygonalChain){
            c.setName("PolygonalChain #"+PolygonalChain.count.getAndIncrement());
            return;
        }
        if (c instanceof NewtonInterpolated) {
            c.setName("Interpolated #" + NewtonInterpolated.count.getAndIncrement());
            return;
        }

        if (c instanceof CubicSpineInterpolated) {
            c.setName("Cubic spine #" + CubicSpineInterpolated.count.getAndIncrement());
            return;
        }

        c.setName("ERROR 404");
    }
}
