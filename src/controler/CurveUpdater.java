package controler;

import model.ICurve;
import model.Interpolated;
import model.PolygonalChain;

/**
 * Created by Albert on 02.04.2016.
 */
class CurveUpdater {

    private CurveUpdater(){

    }

     static void update(ICurve curve){
         if (curve.getPoints().size()>1){
             if (curve instanceof PolygonalChain){
                 update((PolygonalChain) curve);
             }
             if (curve instanceof Interpolated){
                 update((Interpolated) curve);
             }
         }
    }


    private static void update(PolygonalChain curve){
        ConvexHullCalculator.getAnInstance().calculate(curve);
    }


    private static void update(Interpolated curve){
        ConvexHullCalculator.getAnInstance().calculate(curve);
        NewtonInterpolationCalculator.getAnInstance().calculate(curve);
    }
}
