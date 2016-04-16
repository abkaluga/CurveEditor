package controler;

import model.ICurve;
import model.IPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Albert on 12.03.2016.
 */
public  class ConvexHullCalculator implements ICalculator{

    private static final ICalculator anInstance = new ConvexHullCalculator();

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private ConvexHullCalculator(){
        //intentionally left empty
    }

    static ICalculator getAnInstance() {
        return anInstance;
    }

    public void calculate(ICurve curve) {


        List<IPoint> points = new ArrayList<>(curve.getPoints());
        List<IPoint> convexHull = Collections.emptyList();
        if (points.size() < 3){
            convexHull = new ArrayList<>(points);
        } else {

            try {
                convexHull = executor.submit(() -> quickHull(points)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        curve.setConvexHull(convexHull);
    }

    private List<IPoint> quickHull(List<IPoint> points) {
        List<IPoint> convexHull = new LinkedList<>();
        int minPoint = -1, maxPoint = -1;
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getX() < minX) {
                minX = points.get(i).getX();
                minPoint = i;
            }
            if (points.get(i).getX() > maxX) {
                maxX = points.get(i).getX();
                maxPoint = i;
            }
        }

        IPoint A = points.get(minPoint);
        IPoint B = points.get(maxPoint);
        convexHull.add(A);
        convexHull.add(B);
        points.remove(A);
        points.remove(B);

        List<IPoint> leftSet = new ArrayList<>();
        List<IPoint> rightSet = new ArrayList<>();

        for (IPoint p : points) {
            if (pointLocation(A, B, p) == -1)
                leftSet.add(p);
            else if (pointLocation(A, B, p) == 1)
                rightSet.add(p);
        }
        hullSet(A, B, rightSet, convexHull);
        hullSet(B, A, leftSet, convexHull);
        return convexHull;
    }

    private void hullSet(IPoint A, IPoint B, List<IPoint> set, List<IPoint> hull) {
        int insertPosition = hull.indexOf(B);
        if (set.size() == 0)
            return;
        if (set.size() == 1) {
            IPoint p = set.get(0);
            set.remove(p);
            hull.add(insertPosition, p);
            return;
        }
        int dist = Integer.MIN_VALUE;
        int furthestPoint = -1;
        for (int i = 0; i < set.size(); i++)
        {
            IPoint p = set.get(i);
            int distance = distance(A, B, p);
            if (distance > dist) {
                dist = distance;
                furthestPoint = i;
            }
        }
        IPoint P = set.get(furthestPoint);
        set.remove(furthestPoint);
        hull.add(insertPosition, P);

        List<IPoint> leftSetAP = new ArrayList<IPoint>();
        ArrayList<IPoint> leftSetPB = new ArrayList<>();
        for (IPoint M : set) {
            if (pointLocation(A, P, M) == 1) {
                leftSetAP.add(M);
            }
            if (pointLocation(P, B, M) == 1){
                leftSetPB.add(M);
            }
        }
        hullSet(A, P, leftSetAP, hull);
        hullSet(P, B, leftSetPB, hull);

    }


    private int distance(IPoint A, IPoint B, IPoint C) {
        int ABx = B.getX() - A.getX();
        int ABy = B.getY() - A.getY();
        int num = ABx * (A.getY() - C.getY()) - ABy * (A.getX() - C.getX());
        return Math.abs(num);
    }

    private int pointLocation(IPoint A, IPoint B, IPoint P) {
        int cp1 = (B.getX() - A.getX()) * (P.getY() - A.getY()) - (B.getY() - A.getY()) * (P.getX() - A.getX());
        if (cp1 > 0)
            return 1;
        else if (cp1 == 0)
            return 0;
        else
            return -1;
    }

}
