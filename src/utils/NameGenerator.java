package utils;

import model.ICurve;
import model.Interpolated;
import model.PolygonalChain;

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
        if (c instanceof Interpolated){
            c.setName("Interpolated #"+Interpolated.count.getAndIncrement());
            return;
        }

        c.setName("ERROR 404");
    }
}
